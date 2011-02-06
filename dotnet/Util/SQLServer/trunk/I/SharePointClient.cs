#region Using

using System;
using System.IO;

using log4net;

using Microsoft.SharePoint.Client;

#endregion

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Actual implementation of <see cref="ISharePointClient"/>.
    /// </summary>
    public class SharePointClient
        : ISharePointClient
    {
        #region fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(SharePointClient));

        #endregion

        #region Private helpers

        /// <summary>
        /// Gets a client context on which the rootweb is already loaded.
        /// User is responsible for disposing the context.
        /// </summary>
        private ClientContext GetSharePointClientContext()
        {
            ClientContext ctx = new ClientContext(SharePointSiteUrl);
            ctx.Load(ctx.Site.RootWeb);
            ctx.ExecuteQuery();
            return ctx;
        }

        private static void CreateFolder(ClientContext spClientContext, string relativeUrl)
        {
            var workUrl = (relativeUrl.StartsWith("/")) ? relativeUrl.Substring(1) : relativeUrl;
            string[] foldernames = workUrl.Split('/');

            spClientContext.Load(spClientContext.Site.RootWeb.RootFolder);
            spClientContext.ExecuteQuery();

            Folder parentfolder = spClientContext.Site.RootWeb.RootFolder;
            string workname = String.Empty;
            string parentfoldername = String.Empty;
            foreach (string folderName in foldernames)
            {
                try
                {
                    workname = String.Format("{0}/{1}", workname, folderName);
                    Folder workfolder = spClientContext.Site.RootWeb.GetFolderByServerRelativeUrl(workname);
                    spClientContext.Load(workfolder);
                    spClientContext.ExecuteQuery();
                    parentfolder = workfolder;
                }
                catch (ServerException se)
                {
                    if (se.ServerErrorTypeName == typeof(FileNotFoundException).FullName)
                    {
                        if (parentfolder == null)
                        {
                            parentfolder = spClientContext.Site.RootWeb.GetFolderByServerRelativeUrl(parentfoldername);
                            spClientContext.Load(parentfolder);
                        }
                        parentfolder.Folders.Add(folderName);
                        spClientContext.ExecuteQuery();
                        parentfolder = null;
                    }
                }
                parentfoldername = workname;
            }
        }

        #endregion

        #region ISharePointClient interface

        public string SharePointSiteUrl { get; set; }

        /// <inheritdoc cref="ISharePointClient.EnsureFolder" />
        public void EnsureFolder(string relativeUrl)
        {
            try
            {
                using (ClientContext spClientContext = GetSharePointClientContext())
                {
                    Web rootWeb = spClientContext.Site.RootWeb;

                    //Check if the url exists
                    try
                    {
                        rootWeb.GetFolderByServerRelativeUrl(relativeUrl);
                        spClientContext.ExecuteQuery();
                    }
                    catch (ServerException se)
                    {
                        // If not, create it.
                        if (se.ServerErrorTypeName == typeof(FileNotFoundException).FullName)
                        {
                            CreateFolder(spClientContext, relativeUrl);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                s_Logger.Error(string.Format("EnsureFolder({0}) failed using ClientContext {1}.", relativeUrl, SharePointSiteUrl), e);
            }
        }

        #endregion
    }
}