#region Using

using System.Diagnostics.Contracts;

#endregion

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Wrapper around selected remote Sharepoint
    /// functionality and added functionality.
    /// </summary>
    [ContractClass(typeof(ISharePointClientContract))]
    public interface ISharePointClient
    {
        /// <summary>
        /// Property that holds the base URL of the SharePoint
        /// site this instance will work on.
        /// </summary>
        string SharePointSiteUrl { get; set; }

        /// <summary>
        /// Ensure the folder whose name is given
        /// with a <paramref name="relativeUrl"/>
        /// exists.
        /// </summary>
        /// <remarks>
        /// <para>If the folder exists, nothing happens.</para>
        /// <para>Else, it and all intermediate missing folders,
        /// are created.</para>
        /// </remarks>
        /// <param name="relativeUrl">The URL of a folder in a SharePoint document library,
        /// relative to <see cref="SharePointSiteUrl"/>.</param>
        void EnsureFolder(string relativeUrl);
    }

    // ReSharper disable InconsistentNaming
    /// <exclude />
    [ContractClassFor(typeof(ISharePointClient))]
    public abstract class ISharePointClientContract :
        ISharePointClient
    {
        #region ISharePointClient Members

        public string SharePointSiteUrl
        {
            get { return default(string); }
            set
            {
                //NOP;
            }
        }

        public void EnsureFolder(string relativeUrl)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(relativeUrl));
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
        }

        #endregion
    }
}