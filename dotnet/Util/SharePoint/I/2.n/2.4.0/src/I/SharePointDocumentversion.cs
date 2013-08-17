using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PPWCode.Util.SharePoint.I
{
    public class SharePointDocumentVersion
    {
        public string Version { get; set; }
        public DateTime CreationDate { get; set; }
        public string Url { get; set; }

        public SharePointDocumentVersion(string versionnr, DateTime creationDate, string url)
        {
            Version = versionnr;
            CreationDate = creationDate;
            Url = url;
        }
    }
}
