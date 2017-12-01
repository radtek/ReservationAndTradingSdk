using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Xml.Linq;
using System.Text;
using System.Windows.Forms;
using libzkfpcsharp;
using System.Runtime.InteropServices;
using System.Threading;
using System.IO;
using Sample;
using System.Windows;
using System.Windows.Media.Imaging;
using AForge;
using AForge.Controls;
using AForge.Video;
using AForge.Video.DirectShow;
using Microsoft.VisualBasic;
using Size = System.Drawing.Size;
using System.Collections;

namespace Demo
{
    public partial class Form1 : Form
    {
        IntPtr mDevHandle = IntPtr.Zero; //指纹仪设备
        IntPtr mDBHandle = IntPtr.Zero; //已经采集到的指纹模板
        IntPtr FormHandle = IntPtr.Zero;
        private FilterInfoCollection videoDevices;
        private VideoCaptureDevice videoSource;
        List<FingerDemo> fingerDemos = new List<FingerDemo>();

        string xmlFileName = "Fingers.xml";
        string fingerName = ""; //指纹用户的姓名
        bool IsSignInOrOut = false; 
        bool bIsTimeToDie = false;
        bool IsRegister = false;
        bool bIdentify = true;
        bool isFingerHaveNamed = false;  //是否已经给在登记的指纹命名
        byte[] FPBuffer;
        int RegisterCount = 0;
        int DPI = 1000;
        const int REGISTER_FINGER_COUNT = 3;

        byte[][] RegTmps = new byte[3][];
        byte[] RegTmp = new byte[2048];
        byte[] CapTmp = new byte[2048];
        int cbCapTmp = 2048;
        int cbRegTmp = 0;
        int fid = 0, iFid = 1;
       

        private int mfpWidth = 0;
        private int mfpHeight = 0;

        const int MESSAGE_CAPTURED_OK = 0x0400 + 6;

        [DllImport("user32.dll", EntryPoint = "SendMessageA")]
        public static extern int SendMessage(IntPtr hwnd, int wMsg, IntPtr wParam, IntPtr lParam);

        public Form1()
        {
            InitializeComponent();
        }

        private void bnInit_Click(object sender, EventArgs e)
        {
            cmbIdx.Items.Clear();
            int ret = zkfperrdef.ZKFP_ERR_OK;
            if ((ret = zkfp2.Init()) == zkfperrdef.ZKFP_ERR_OK)
            {
                int nCount = zkfp2.GetDeviceCount();
                if (nCount > 0)
                {
                    for (int i = 0; i < nCount; i++)
                    {
                        cmbIdx.Items.Add(i.ToString());
                    }
                    cmbIdx.SelectedIndex = 0;
                    bnInit.Enabled = false;
                    bnFree.Enabled = true;
                    bnOpen.Enabled = true;
                    
                }
                else
                {
                    zkfp2.Terminate();
                    MessageBox.Show("No device connected!");
                }
            }
            else
            {
                MessageBox.Show("Initialize fail, ret=" + ret + " !");
            }
        }

        private void bnFree_Click(object sender, EventArgs e)
        {
            zkfp2.Terminate();
            cbRegTmp = 0;
            bnInit.Enabled = true;
            bnFree.Enabled = false;
            bnOpen.Enabled = false;
            bnClose.Enabled = false;
            bnEnroll.Enabled = false;
            btn_In_Identify.Enabled = false;
            btn_Out_Identify.Enabled = false;
            //bnVerify.Enabled = false;
            //bnIdentify.Enabled = false;
        }
      

        /// <summary>
        ///   插入一条指纹信息到XML文件
        /// </summary>
        /// <param name="mDBHandle"></param>
        private void writeFingerXML(int idx,String fingerName, String strBase64){
            XElement xe = XElement.Load(GetImagePath()+xmlFileName);
            XElement record = new XElement(
                   new XElement("finger",
                          new XAttribute("fingerID",idx),
                          new XAttribute("fingerName", fingerName),
                          new XAttribute("fingerStrBase64", strBase64))

                );
            xe.Add(record);
            xe.Save(GetImagePath() + xmlFileName);
            MessageBox.Show("指纹登记成功！");

        }
        /// <summary>
        /// 读取XML文件中的全部指纹信息
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void readFingerXML(ref List<FingerDemo> fingerDemos)
        {
            //XML文件不存在时，创建该文件
            if (!File.Exists(GetImagePath() + xmlFileName))
            {
                try
                {
                    //XDocument doc = new XDocument();
                    //doc.Declaration = new XDeclaration("1.0", "UTF-8", "");
                    XDocument doc = new XDocument(
                         new XElement("fingers", null
                             /**
                             new XAttribute("fingerID",0),
                             new XAttribute("fingerName", ""),
                             new XAttribute("fingerStrBase64", "")
                              * **/
                            )
                        );
                    doc.Save(GetImagePath() + xmlFileName);
                }
                catch(Exception ex)
                {
                    //Console.WriteLine(ex.ToString());
                }

            }
            else
            {

                XElement xe = XElement.Load(GetImagePath() + xmlFileName);
                IEnumerable<XElement> elements = from ele in xe.Elements("finger")
                                                 select ele;
                int idx = 1;
                foreach (var ele in elements)
                {
                    FingerDemo finger = new FingerDemo();
                    finger.Index = idx++;
                    finger.FingerName = ele.Attribute("fingerName").Value;
                    finger.FingerStrBase64 = ele.Attribute("fingerStrBase64").Value;
                    fingerDemos.Add(finger);
                }
            }
        }

        /// <summary>
        /// 加载指纹模板库到内存中
        /// </summary>
        /// <param name="mDBHandler"></param>
        /// <param name="fingerDemos"></param>
        private void loadFingers(ref IntPtr mDBHandler, List<FingerDemo> fingerDemos)
        {
            int ret;
            byte[] registerTmp;
            foreach (FingerDemo ele in fingerDemos)
            {
                registerTmp = zkfp.Base64String2Blob(ele.FingerStrBase64);
                if (registerTmp != null && zkfp.ZKFP_ERR_OK == (ret = zkfp2.DBAdd(mDBHandler, ele.Index, registerTmp)))
                {

                }
                else
                {
                    MessageBox.Show("加载 "+ele.FingerName+" 指纹模板库报错！");
                }
            }
        }

        private void bnOpen_Click(object sender, EventArgs e)
        {
            int ret = zkfp.ZKFP_ERR_OK;
            if (IntPtr.Zero == (mDevHandle = zkfp2.OpenDevice(cmbIdx.SelectedIndex)))
            {
                MessageBox.Show("打开指纹仪失败！");
                return;
            }
            if (IntPtr.Zero == (mDBHandle = zkfp2.DBInit()))
            {
                MessageBox.Show("Init DB fail");
                zkfp2.CloseDevice(mDevHandle);
                mDevHandle = IntPtr.Zero;
                return;
            }
            bnInit.Enabled = false;
            bnFree.Enabled = true;
            bnOpen.Enabled = false;
            bnClose.Enabled = true;
            bnEnroll.Enabled = true;
           // bnVerify.Enabled = true;
           // bnIdentify.Enabled = true;

            RegisterCount = 0;
            cbRegTmp = 0;
            iFid = 1;
            for (int i = 0; i < 3; i++)
            {
                RegTmps[i] = new byte[2048];
            }
            //设置指纹图像的宽和高
            byte[] paramValue = new byte[4];
            int size = 4;
            zkfp2.GetParameters(mDevHandle, 1, paramValue, ref size);
            zkfp2.ByteArray2Int(paramValue, ref mfpWidth);
            size = 4;
            zkfp2.GetParameters(mDevHandle, 2, paramValue, ref size);
            zkfp2.ByteArray2Int(paramValue, ref mfpHeight);
            FPBuffer = new byte[mfpWidth * mfpHeight];
            fingerDemos.Clear();
            readFingerXML(ref fingerDemos);
            loadFingers(ref mDBHandle, fingerDemos);

            Thread captureThread = new Thread(new ThreadStart(DoCapture));
            captureThread.IsBackground = true;
            captureThread.Start();
            bIsTimeToDie = false;
            textRes.Text = "成功打开指纹仪！";
        }

        private void DoCapture()
        {
            while (!bIsTimeToDie)
            {
                cbCapTmp = 2048;
                int ret = zkfp2.AcquireFingerprint(mDevHandle, FPBuffer, CapTmp, ref cbCapTmp);
                if (ret == zkfp.ZKFP_ERR_OK)
                {
                    SendMessage(FormHandle, MESSAGE_CAPTURED_OK, IntPtr.Zero, IntPtr.Zero);
                }
                Thread.Sleep(200);
            }
        }


        protected override void DefWndProc(ref Message m)
        {
            switch (m.Msg)
            {
                case MESSAGE_CAPTURED_OK:
                    {
                        MemoryStream ms = new MemoryStream();
                        BitmapFormat.GetBitmap(FPBuffer, mfpWidth, mfpHeight, ref ms);
                        Bitmap bmp = new Bitmap(ms);
                        this.picFPImg.Image = bmp;

                        if (IsRegister )
                        {
                            int ret = zkfp.ZKFP_ERR_OK;
                            int fid = 0, score = 0;
                            ret = zkfp2.DBIdentify(mDBHandle, CapTmp, ref fid, ref score);
                            if (zkfp.ZKFP_ERR_OK == ret)
                            {
                                textRes.Text = " 该指纹已经注册 " + fid + "!";
                                return;
                            }
                            if (RegisterCount > 0 && zkfp2.DBMatch(mDBHandle, CapTmp, RegTmps[RegisterCount - 1]) <= 0)
                            {
                                textRes.Text = "指纹登记时，请使用同一个手指输入三次指纹！";
                                return;
                            }
                            //CapTmp为当前采集到指纹模板
                            Array.Copy(CapTmp, RegTmps[RegisterCount], cbCapTmp);
                            RegisterCount++;
                            if (RegisterCount >= REGISTER_FINGER_COUNT)
                            {
                                RegisterCount = 0;
                                if (zkfp.ZKFP_ERR_OK == (ret = zkfp2.DBMerge(mDBHandle, RegTmps[0], RegTmps[1], RegTmps[2], RegTmp, ref cbRegTmp)) &&
                                       zkfp.ZKFP_ERR_OK == (ret = zkfp2.DBAdd(mDBHandle, fingerDemos.Count+1, RegTmp)))
                                {
                                    //把RegTmp作为图像存起来，以指纹用户的姓名来命名
                                    iFid++;
                                    fingerName = Interaction.InputBox("请输入该指纹用户的姓名", "请输入您的姓名", "张三", -1, -1);
                                    textRes.Text = fingerName + "　指纹登记成功";
                                    String strFingerBase64 = "";
                                    int retCode = zkfp.Blob2Base64String(RegTmp, 2048, ref strFingerBase64);
                                    //String strFingerBase64 = zkfp2.BlobToBase64(RegTmp, cbRegTmp); 
                                    if (retCode > 0)
                                    {
                                        writeFingerXML(fingerDemos.Count+1,fingerName, strFingerBase64);
                                        FingerDemo finger = new FingerDemo();
                                        finger.Index = fingerDemos.Count + 1;
                                        finger.FingerName = fingerName;
                                        finger.FingerStrBase64 = strFingerBase64;
                                        fingerDemos.Add(finger);
                                    }
                                }
                                else
                                {
                                    textRes.Text = "指纹登记失败, 错误码为" + ret;
                                }
                                IsRegister = false;
                                return;
                            }
                            else
                            {
                                textRes.Text = "您还需要输入 " + (REGISTER_FINGER_COUNT - RegisterCount) + " 次指纹 ";
                            }
                        }
                        else
                        {
                            if (cbRegTmp <= 0 &&  fingerDemos.Count == 0)
                            {
                                textRes.Text = "请先登记指纹";
                                return;
                            }
                            if (bIdentify)//1:N识别
                            {
                                int ret = zkfp.ZKFP_ERR_OK;
                                int  score = 0;
                                ret = zkfp2.DBIdentify(mDBHandle, CapTmp, ref fid, ref score);
                                if (zkfp.ZKFP_ERR_OK == ret)
                                {
                                    //textRes.Text = "Identify succ, fid= " + fid + ",score=" + score + "!";
                                    textRes.Text = "识别成功，"+ fingerDemos[fid-1].FingerName + (IsSignInOrOut ? "进入":"离开");
                                    //System.Console.WriteLine("Sign="+IsSignInOrOut);
                                    return;
                                }
                                else
                                {
                                    textRes.Text = "Identify fail, ret= " + ret;
                                    return;
                                }
                            }
                            else // 1:1识别
                            {
                                int ret = zkfp2.DBMatch(mDBHandle, CapTmp, RegTmp);
                                if (0 < ret)
                                {
                                    textRes.Text = "Match finger succ, score=" + ret + "!";
                                    return;
                                }
                                else
                                {
                                    textRes.Text = "Match finger fail, ret= " + ret;
                                    return;
                                }
                            }
                        }
                    }
                    break;

                default:
                    base.DefWndProc(ref m);
                    break;
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            FormHandle = this.Handle;
            /**
            Rectangle ScreenArea = System.Windows.Forms.Screen.GetBounds(this);
            int width = ScreenArea.Width;
            int height = ScreenArea.Height;
            this.Top = 0;
            this.Left = 0;
            this.Width = width;
            this.Height = height;
            **/
            try
            {
                // 枚举所有视频输入设备
                videoDevices = new FilterInfoCollection(FilterCategory.VideoInputDevice);

                if (videoDevices.Count == 0)
                    throw new ApplicationException();

                foreach (FilterInfo device in videoDevices)
                {
                    tscbxCameras.Items.Add(device.Name);
                }

                tscbxCameras.SelectedIndex = 0;
                //CameraConn();   
            }
            catch (ApplicationException)
            {
                tscbxCameras.Items.Add("No local capture devices");
                videoDevices = null;
            }
        }


        private void bnClose_Click(object sender, EventArgs e)
        {
            bIsTimeToDie = true;
            RegisterCount = 0;
            Thread.Sleep(1000);
            zkfp2.DBFree(mDBHandle);
            zkfp2.CloseDevice(mDevHandle);
            bnInit.Enabled = false;
            bnFree.Enabled = true;
            bnOpen.Enabled = true;
            bnClose.Enabled = false;
            bnEnroll.Enabled = false;
            btn_In_Identify.Enabled = false;
            btn_Out_Identify.Enabled = false;
            //bnVerify.Enabled = false;
            //bnIdentify.Enabled = false;
            textRes.Text = "关闭指纹仪！";
        }
        /// <summary>
        /// 登记指纹仪
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void bnEnroll_Click(object sender, EventArgs e)
        {
            if (!IsRegister)
            {
                IsRegister = true;              
            }
            RegisterCount = 0;
            cbRegTmp = 0;
            textRes.Text = "请按3次指纹!";
        }
        /**
        private void bnIdentify_Click(object sender, EventArgs e) //1:N比对指纹
        {
            if (!bIdentify)
            {
                bIdentify = true;
                textRes.Text = "请按指纹!";
            }
        }
         ***/
        /// <summary>
        /// 进入刷指纹
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn_In_Identify_Click(object sender, EventArgs e)
        {
            if (!bIdentify)
            {
                bIdentify = true;
                
            }
            if (!IsSignInOrOut)
            {
                IsSignInOrOut = true;
            }
            textRes.Text = "请按指纹!";
        }

        /// <summary>
        /// 离开刷指纹
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn_Out_Identify_Click(object sender, EventArgs e)
        {
            if (!bIdentify)
            {
                bIdentify = true;               
            }
            if (IsSignInOrOut)
            {
                IsSignInOrOut = false;
            }
            textRes.Text = "请按指纹!";
        }
        /**
        private void bnVerify_Click(object sender, EventArgs e)//1:1比对指纹
        {
            if (bIdentify)
            {
                bIdentify = false;
                textRes.Text = "请按指纹!";
            }
        }
        **/

        private void btnConnect_Click(object sender, EventArgs e)
        {
            CameraConn();
        }
        //连接摄像头
        private void CameraConn()
        {
            VideoCaptureDevice videoSource = new VideoCaptureDevice(videoDevices[tscbxCameras.SelectedIndex].MonikerString);
            videoSource.DesiredFrameSize = new System.Drawing.Size(320, 240);
            videoSource.DesiredFrameRate = 1;

            videoSourcePlayer.VideoSource = videoSource;
            videoSourcePlayer.Start();
        }

        //关闭摄像头
        private void btnClose_Click(object sender, EventArgs e)
        {
            videoSourcePlayer.SignalToStop();
            videoSourcePlayer.WaitForStop();
        }

        //主窗体关闭
        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            btnClose_Click(null, null);
            bnClose_Click(null, null);
            bnFree_Click(null, null);
            this.Close();

        }

        /// <summary>
        /// 
        /// 拍照后转图片存储
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Photograph_Click(object sender, EventArgs e)
        {
            try
            {
                if (videoSourcePlayer.IsRunning)
                {
                    BitmapSource bitmapSource = System.Windows.Interop.Imaging.CreateBitmapSourceFromHBitmap(
                                    videoSourcePlayer.GetCurrentVideoFrame().GetHbitmap(),
                                    IntPtr.Zero,
                                     Int32Rect.Empty,
                                    BitmapSizeOptions.FromEmptyOptions());
                    PngBitmapEncoder pE = new PngBitmapEncoder();
                    pE.Frames.Add(BitmapFrame.Create(bitmapSource));
                    string picName = GetImagePath()+ DateTime.Now.ToString("yyyy-MM-dd")+fingerName +(IsSignInOrOut ? "进入":"离开")+ ".jpg";
                    if (File.Exists(picName))
                    {
                        File.Delete(picName);
                    }
                    using (Stream stream = File.Create(picName))
                    {
                        pE.Save(stream);
                        stream.Close();
                    }
                    /**
                    //拍照完成后关摄像头并刷新同时关窗体
                    if (videoSourcePlayer != null && videoSourcePlayer.IsRunning)
                    {
                        videoSourcePlayer.SignalToStop();
                        videoSourcePlayer.WaitForStop();
                    }
                    
                    this.Close();
                     ***/
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("摄像头异常：" + ex.Message);
            }
        }

        private string GetImagePath()
        {
            string personImgPath = Path.GetDirectoryName(AppDomain.CurrentDomain.BaseDirectory)
                         + Path.DirectorySeparatorChar.ToString();
            if (!Directory.Exists(personImgPath))
            {
                Directory.CreateDirectory(personImgPath);
            }

            return personImgPath;
        }
      }
}
