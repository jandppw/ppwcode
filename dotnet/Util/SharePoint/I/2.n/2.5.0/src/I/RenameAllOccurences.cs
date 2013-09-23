namespace PPWCode.Util.SharePoint.I
{
    public class RenameAllOccurences : ISharepointAction
    {
        public RenameAllOccurences(ISharePointClient iSharePointClient, string baseRelativeUrl, string oldFolderName, string newFolderName)
        {
            Sharepoint = iSharePointClient;
            BaseRelativeUrl = baseRelativeUrl;
            OldFolderName = oldFolderName;
            NewFolderName = newFolderName;
        }

        public void Do()
        {
            Sharepoint.RenameAllOccurrencesOfFolder(BaseRelativeUrl, OldFolderName, NewFolderName);
        }

        public void Undo()
        {
            Sharepoint.RenameAllOccurrencesOfFolder(BaseRelativeUrl, NewFolderName, OldFolderName);
        }

        public ISharePointClient Sharepoint { get; set; }
        public string BaseRelativeUrl { get; set; }
        public string OldFolderName { get; set; }
        public string NewFolderName { get; set; }
    }
}