namespace Demo
{
    partial class FingerRegister
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
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
            this.bnEnroll = new System.Windows.Forms.Button();
            this.bnClose = new System.Windows.Forms.Button();
            this.textRes = new System.Windows.Forms.TextBox();
            this.picFPImg = new System.Windows.Forms.PictureBox();
            this.label1 = new System.Windows.Forms.Label();
            this.cmbIdx = new System.Windows.Forms.ComboBox();
            this.bnOpen = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.txt_Finger_Name = new System.Windows.Forms.TextBox();
            ((System.ComponentModel.ISupportInitialize)(this.picFPImg)).BeginInit();
            this.SuspendLayout();
            // 
            // bnEnroll
            // 
            this.bnEnroll.Enabled = false;
            this.bnEnroll.Location = new System.Drawing.Point(27, 80);
            this.bnEnroll.Name = "bnEnroll";
            this.bnEnroll.Size = new System.Drawing.Size(75, 23);
            this.bnEnroll.TabIndex = 2;
            this.bnEnroll.Text = "指纹登记";
            this.bnEnroll.UseVisualStyleBackColor = true;
            this.bnEnroll.Click += new System.EventHandler(this.bnEnroll_Click);
            // 
            // bnClose
            // 
            this.bnClose.Enabled = false;
            this.bnClose.Location = new System.Drawing.Point(119, 26);
            this.bnClose.Name = "bnClose";
            this.bnClose.Size = new System.Drawing.Size(75, 23);
            this.bnClose.TabIndex = 5;
            this.bnClose.Text = "关闭指纹仪";
            this.bnClose.UseVisualStyleBackColor = true;
            this.bnClose.Click += new System.EventHandler(this.bnClose_Click);
            // 
            // textRes
            // 
            this.textRes.Location = new System.Drawing.Point(1, 185);
            this.textRes.Multiline = true;
            this.textRes.Name = "textRes";
            this.textRes.ReadOnly = true;
            this.textRes.Size = new System.Drawing.Size(451, 115);
            this.textRes.TabIndex = 7;
            // 
            // picFPImg
            // 
            this.picFPImg.Location = new System.Drawing.Point(310, 12);
            this.picFPImg.Name = "picFPImg";
            this.picFPImg.Size = new System.Drawing.Size(131, 161);
            this.picFPImg.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.picFPImg.TabIndex = 8;
            this.picFPImg.TabStop = false;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(210, 32);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 12);
            this.label1.TabIndex = 9;
            this.label1.Text = "Index:";
            // 
            // cmbIdx
            // 
            this.cmbIdx.FormattingEnabled = true;
            this.cmbIdx.Location = new System.Drawing.Point(248, 29);
            this.cmbIdx.Name = "cmbIdx";
            this.cmbIdx.Size = new System.Drawing.Size(40, 20);
            this.cmbIdx.TabIndex = 10;
            // 
            // bnOpen
            // 
            this.bnOpen.Enabled = false;
            this.bnOpen.Location = new System.Drawing.Point(27, 26);
            this.bnOpen.Name = "bnOpen";
            this.bnOpen.Size = new System.Drawing.Size(75, 23);
            this.bnOpen.TabIndex = 1;
            this.bnOpen.Text = "打开指纹仪";
            this.bnOpen.UseVisualStyleBackColor = true;
            this.bnOpen.Click += new System.EventHandler(this.bnOpen_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(27, 130);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(101, 12);
            this.label2.TabIndex = 11;
            this.label2.Text = "登记指纹者的姓名";
            // 
            // txt_Finger_Name
            // 
            this.txt_Finger_Name.Location = new System.Drawing.Point(134, 127);
            this.txt_Finger_Name.Name = "txt_Finger_Name";
            this.txt_Finger_Name.Size = new System.Drawing.Size(138, 21);
            this.txt_Finger_Name.TabIndex = 12;
            // 
            // FingerRegister
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(453, 306);
            this.Controls.Add(this.txt_Finger_Name);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.cmbIdx);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.picFPImg);
            this.Controls.Add(this.textRes);
            this.Controls.Add(this.bnClose);
            this.Controls.Add(this.bnEnroll);
            this.Controls.Add(this.bnOpen);
            this.Name = "FingerRegister";
            this.Text = "指纹登记";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.picFPImg)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button bnEnroll;
        private System.Windows.Forms.Button bnClose;
        private System.Windows.Forms.TextBox textRes;
        private System.Windows.Forms.PictureBox picFPImg;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox cmbIdx;
        private System.Windows.Forms.Button bnOpen;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txt_Finger_Name;
    }
}

