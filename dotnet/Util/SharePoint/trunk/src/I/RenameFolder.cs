using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PPWCode.Util.SharePoint.I
{
    public class RenameFolder : ISharepointAction
    {
        public RenameFolder(ISharePointClient iSharePointClient, string baseRelativeUrl, string oldFolderName, string newFolderName)
        {
            Sharepoint = iSharePointClient;
            BaseRelativeUrl = baseRelativeUrl;
            OldFolderName = oldFolderName;
            NewFolderName = newFolderName;
        }
        public void Do()
        {
           Sharepoint.RenameFolder(BaseRelativeUrl, OldFolderName, NewFolderName);
        }

        public void Undo()
        {
            Sharepoint.RenameFolder(BaseRelativeUrl, NewFolderName, OldFolderName);
        }
        public ISharePointClient Sharepoint { get; set; }
        public string BaseRelativeUrl { get; set; }
        public string OldFolderName { get; set; }
        public string NewFolderName { get; set; }
    }
      
}
