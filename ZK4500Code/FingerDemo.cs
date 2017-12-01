using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Demo
{
    class FingerDemo
    {
        public FingerDemo()
        {
        }
        /// <summary>
        /// 指纹序号
        /// </summary>
        private int index;
        public int Index
        {
            get { return index; }
            set { index = value; }
        }
        /// <summary>
        /// 指纹名称
        /// </summary>
        private string fingerName;
        public string FingerName
        {
            get { return fingerName; }
            set { fingerName = value; }
        }
        /// <summary>
        /// 指纹Base64字符串
        /// </summary>
        private string fingerStrBase64;
        public string FingerStrBase64
        {
            get { return fingerStrBase64; }
            set { fingerStrBase64 = value; }
        }

    }
}
