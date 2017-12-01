namespace Demo
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.bnInit = new System.Windows.Forms.Button();
            this.bnOpen = new System.Windows.Forms.Button();
            this.bnEnroll = new System.Windows.Forms.Button();
            this.bnFree = new System.Windows.Forms.Button();
            this.bnClose = new System.Windows.Forms.Button();
            this.textRes = new System.Windows.Forms.TextBox();
            this.picFPImg = new System.Windows.Forms.PictureBox();
            this.label1 = new System.Windows.Forms.Label();
            this.cmbIdx = new System.Windows.Forms.ComboBox();
            this.labelVideo = new System.Windows.Forms.Label();
            this.tscbxCameras = new System.Windows.Forms.ComboBox();
            this.btnConnect = new System.Windows.Forms.Button();
            this.btnClose = new System.Windows.Forms.Button();
            this.Photograph = new System.Windows.Forms.Button();
            this.videoSourcePlayer = new AForge.Controls.VideoSourcePlayer();
            this.btn_In_Identify = new System.Windows.Forms.Button();
            this.btn_Out_Identify = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.picFPImg)).BeginInit();
            this.SuspendLayout();
            // 
            // bnInit
            // 
            this.bnInit.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bnInit.Location = new System.Drawing.Point(12, 27);
            this.bnInit.Name = "bnInit";
            this.bnInit.Size = new System.Drawing.Size(122, 23);
            this.bnInit.TabIndex = 0;
            this.bnInit.Text = "初始化指纹仪";
            this.bnInit.UseVisualStyleBackColor = true;
            this.bnInit.Click += new System.EventHandler(this.bnInit_Click);
            // 
            // bnOpen
            // 
            this.bnOpen.Enabled = false;
            this.bnOpen.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bnOpen.Location = new System.Drawing.Point(12, 150);
            this.bnOpen.Name = "bnOpen";
            this.bnOpen.Size = new System.Drawing.Size(107, 23);
            this.bnOpen.TabIndex = 1;
            this.bnOpen.Text = "打开指纹仪";
            this.bnOpen.UseVisualStyleBackColor = true;
            this.bnOpen.Click += new System.EventHandler(this.bnOpen_Click);
            // 
            // bnEnroll
            // 
            this.bnEnroll.Enabled = false;
            this.bnEnroll.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bnEnroll.Location = new System.Drawing.Point(13, 227);
            this.bnEnroll.Name = "bnEnroll";
            this.bnEnroll.Size = new System.Drawing.Size(104, 23);
            this.bnEnroll.TabIndex = 2;
            this.bnEnroll.Text = "指纹登记";
            this.bnEnroll.UseVisualStyleBackColor = true;
            this.bnEnroll.Click += new System.EventHandler(this.bnEnroll_Click);
            // 
            // bnFree
            // 
            this.bnFree.Enabled = false;
            this.bnFree.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bnFree.Location = new System.Drawing.Point(145, 27);
            this.bnFree.Name = "bnFree";
            this.bnFree.Size = new System.Drawing.Size(122, 23);
            this.bnFree.TabIndex = 4;
            this.bnFree.Text = "断开指纹仪";
            this.bnFree.UseVisualStyleBackColor = true;
            this.bnFree.Click += new System.EventHandler(this.bnFree_Click);
            // 
            // bnClose
            // 
            this.bnClose.Enabled = false;
            this.bnClose.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bnClose.Location = new System.Drawing.Point(145, 150);
            this.bnClose.Name = "bnClose";
            this.bnClose.Size = new System.Drawing.Size(106, 23);
            this.bnClose.TabIndex = 5;
            this.bnClose.Text = "关闭指纹仪";
            this.bnClose.UseVisualStyleBackColor = true;
            this.bnClose.Click += new System.EventHandler(this.bnClose_Click);
            // 
            // textRes
            // 
            this.textRes.Location = new System.Drawing.Point(12, 399);
            this.textRes.Multiline = true;
            this.textRes.Name = "textRes";
            this.textRes.ReadOnly = true;
            this.textRes.Size = new System.Drawing.Size(429, 44);
            this.textRes.TabIndex = 7;
            // 
            // picFPImg
            // 
            this.picFPImg.Location = new System.Drawing.Point(300, 27);
            this.picFPImg.Name = "picFPImg";
            this.picFPImg.Size = new System.Drawing.Size(131, 161);
            this.picFPImg.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picFPImg.TabIndex = 8;
            this.picFPImg.TabStop = false;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("宋体", 12F);
            this.label1.Location = new System.Drawing.Point(12, 80);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(88, 16);
            this.label1.TabIndex = 9;
            this.label1.Text = "指纹仪列表";
            // 
            // cmbIdx
            // 
            this.cmbIdx.FormattingEnabled = true;
            this.cmbIdx.Location = new System.Drawing.Point(134, 76);
            this.cmbIdx.Name = "cmbIdx";
            this.cmbIdx.Size = new System.Drawing.Size(67, 20);
            this.cmbIdx.TabIndex = 10;
            // 
            // labelVideo
            // 
            this.labelVideo.AutoSize = true;
            this.labelVideo.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labelVideo.Location = new System.Drawing.Point(456, 27);
            this.labelVideo.Name = "labelVideo";
            this.labelVideo.Size = new System.Drawing.Size(104, 16);
            this.labelVideo.TabIndex = 11;
            this.labelVideo.Text = "视频输入设备";
            // 
            // tscbxCameras
            // 
            this.tscbxCameras.FormattingEnabled = true;
            this.tscbxCameras.Location = new System.Drawing.Point(576, 24);
            this.tscbxCameras.Name = "tscbxCameras";
            this.tscbxCameras.Size = new System.Drawing.Size(121, 20);
            this.tscbxCameras.TabIndex = 12;
            // 
            // btnConnect
            // 
            this.btnConnect.Font = new System.Drawing.Font("宋体", 12F);
            this.btnConnect.Location = new System.Drawing.Point(720, 24);
            this.btnConnect.Name = "btnConnect";
            this.btnConnect.Size = new System.Drawing.Size(98, 26);
            this.btnConnect.TabIndex = 13;
            this.btnConnect.Text = "打开摄像头";
            this.btnConnect.UseVisualStyleBackColor = true;
            this.btnConnect.Click += new System.EventHandler(this.btnConnect_Click);
            // 
            // btnClose
            // 
            this.btnClose.Font = new System.Drawing.Font("宋体", 12F);
            this.btnClose.Location = new System.Drawing.Point(824, 26);
            this.btnClose.Name = "btnClose";
            this.btnClose.Size = new System.Drawing.Size(99, 24);
            this.btnClose.TabIndex = 14;
            this.btnClose.Text = "关闭摄像头";
            this.btnClose.UseVisualStyleBackColor = true;
            this.btnClose.Click += new System.EventHandler(this.btnClose_Click);
            // 
            // Photograph
            // 
            this.Photograph.Font = new System.Drawing.Font("宋体", 16F);
            this.Photograph.Location = new System.Drawing.Point(929, 24);
            this.Photograph.Name = "Photograph";
            this.Photograph.Size = new System.Drawing.Size(85, 58);
            this.Photograph.TabIndex = 15;
            this.Photograph.Text = "拍照";
            this.Photograph.UseVisualStyleBackColor = true;
            this.Photograph.Click += new System.EventHandler(this.Photograph_Click);
            // 
            // videoSourcePlayer
            // 
            this.videoSourcePlayer.Location = new System.Drawing.Point(458, 91);
            this.videoSourcePlayer.Name = "videoSourcePlayer";
            this.videoSourcePlayer.Size = new System.Drawing.Size(556, 537);
            this.videoSourcePlayer.TabIndex = 10;
            this.videoSourcePlayer.Text = "videoSourcePlayer";
            this.videoSourcePlayer.VideoSource = null;
            // 
            // btn_In_Identify
            // 
            this.btn_In_Identify.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btn_In_Identify.Location = new System.Drawing.Point(300, 227);
            this.btn_In_Identify.Name = "btn_In_Identify";
            this.btn_In_Identify.Size = new System.Drawing.Size(75, 29);
            this.btn_In_Identify.TabIndex = 16;
            this.btn_In_Identify.Text = "进入";
            this.btn_In_Identify.UseVisualStyleBackColor = true;
            this.btn_In_Identify.Click += new System.EventHandler(this.btn_In_Identify_Click);
            // 
            // btn_Out_Identify
            // 
            this.btn_Out_Identify.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btn_Out_Identify.Location = new System.Drawing.Point(300, 334);
            this.btn_Out_Identify.Name = "btn_Out_Identify";
            this.btn_Out_Identify.Size = new System.Drawing.Size(75, 29);
            this.btn_Out_Identify.TabIndex = 17;
            this.btn_Out_Identify.Text = "离开";
            this.btn_Out_Identify.UseVisualStyleBackColor = true;
            this.btn_Out_Identify.Click += new System.EventHandler(this.btn_Out_Identify_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(1035, 665);
            this.Controls.Add(this.btn_Out_Identify);
            this.Controls.Add(this.btn_In_Identify);
            this.Controls.Add(this.videoSourcePlayer);
            this.Controls.Add(this.Photograph);
            this.Controls.Add(this.btnClose);
            this.Controls.Add(this.btnConnect);
            this.Controls.Add(this.tscbxCameras);
            this.Controls.Add(this.labelVideo);
            this.Controls.Add(this.cmbIdx);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.picFPImg);
            this.Controls.Add(this.textRes);
            this.Controls.Add(this.bnClose);
            this.Controls.Add(this.bnFree);
            this.Controls.Add(this.bnEnroll);
            this.Controls.Add(this.bnOpen);
            this.Controls.Add(this.bnInit);
            this.MaximizeBox = false;
            this.Name = "Form1";
            this.Text = "指纹考勤系统";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.picFPImg)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button bnInit;
        private System.Windows.Forms.Button bnOpen;
        private System.Windows.Forms.Button bnEnroll;
        private System.Windows.Forms.Button bnFree;
        private System.Windows.Forms.Button bnClose;
        private System.Windows.Forms.TextBox textRes;
        private System.Windows.Forms.PictureBox picFPImg;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox cmbIdx;
        private System.Windows.Forms.Label labelVideo;
        private System.Windows.Forms.ComboBox tscbxCameras;
        private System.Windows.Forms.Button btnConnect;
        private System.Windows.Forms.Button btnClose;
        private System.Windows.Forms.Button Photograph;
        private AForge.Controls.VideoSourcePlayer videoSourcePlayer;
        private System.Windows.Forms.Button btn_In_Identify;
        private System.Windows.Forms.Button btn_Out_Identify;
    }
}

