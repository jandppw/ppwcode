using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Result returned by SearchFiles
    /// </summary>
    public class SharePointSearchResult
    {
        public string Url { get; set; }
        public Dictionary<string, object> Properties { get; set; }

        public SharePointSearchResult()
        {
            Properties = new Dictionary<string, object>();
        }
    }
}
