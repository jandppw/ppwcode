namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Mock NOP implementation of <see cref="ISharePointClient"/>
    /// </summary>
    public class NopSharePointClient : ISharePointClient
    {
        #region ISharePointClient Members

        public string SharePointSiteUrl { get; set; }

        public void EnsureFolder(string relativeUrl)
        {
            //NOP
        }

        #endregion
    }
}