/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#region Using

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;

using log4net;

using Microsoft.SharePoint.Client;

using File = Microsoft.SharePoint.Client.File;

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
            s_Logger.Info(string.Format("Connect to SharePoint using user {0}", ctx.Web.CurrentUser));
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
                throw;
            }
        }

        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            try
            {
                using (ClientContext spClientContext = GetSharePointClientContext())
                {
                    Web rootWeb = spClientContext.Site.RootWeb;

                    //Check if the url exists
                    int index = relativeUrl.LastIndexOf("/");
                    string parentFolder = relativeUrl.Substring(0, index);
                    string filename = relativeUrl.Substring(index + 1);

                    //Create intermediate folders if not exist
                    EnsureFolder(parentFolder);
                    Folder fldr = rootWeb.GetFolderByServerRelativeUrl(parentFolder);
                    spClientContext.ExecuteQuery();

                    //Create File information
                    var fciNewFileFromComputer = new FileCreationInformation
                    {
                        Content = doc.Content,
                        Url = filename,
                        Overwrite = true
                    };

                    //Upload the file
                    File uploadedFile = fldr.Files.Add(fciNewFileFromComputer);
                    spClientContext.Load(uploadedFile);
                    spClientContext.ExecuteQuery();
                }
            }
            catch (Exception e)
            {
                s_Logger.Error(string.Format("UploadDocument({0}) failed using ClientContext {1}.", relativeUrl, SharePointSiteUrl), e);
                throw;
            }
        }

        public bool ValidateUri(Uri sharePointUri)
        {
            if (sharePointUri != null)
            {
                string baseUrl = sharePointUri.GetLeftPart(UriPartial.Authority);

                if (!string.IsNullOrEmpty(baseUrl))
                {
                    using (ClientContext clientContext = new ClientContext(baseUrl))
                    {
                        //get the site collection
                        Web site = clientContext.Web;

                        string localPath = sharePointUri.LocalPath;

                        if (!string.IsNullOrEmpty(localPath))
                        {
                            //get the document library folder
                            site.GetFolderByServerRelativeUrl(localPath);
                            try
                            {
                                clientContext.ExecuteQuery();
                                return true;
                            }
                            catch (ServerException)
                            {
                                return false;
                            }
                            catch (Exception)
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        }

        public void OpenUri(Uri uri)
        {
            string url = uri.OriginalString;
            if (!string.IsNullOrEmpty(url))
            {
                Process.Start(new ProcessStartInfo
                {
                    UseShellExecute = true,
                    FileName = url,
                    Verb = "Open",
                    LoadUserProfile = true
                });
            }
        }

        public List<SharePointSearchResult> SearchFiles(string relativeUrl)
        {
            try
            {
                using (ClientContext spClientContext = GetSharePointClientContext())
                {
                    Web rootWeb = spClientContext.Site.RootWeb;
                    Folder spFolder = rootWeb.GetFolderByServerRelativeUrl(relativeUrl); // "/Shared%20Documents/MEERTENS%20MATHIEU%20-%2051062002701/Construo/Actua/");
                    spClientContext.Load(spFolder.Files);
                    spClientContext.ExecuteQuery();

                    List<SharePointSearchResult> result = new List<SharePointSearchResult>();
                    foreach (File spFile in spFolder.Files)
                    {
                        var fileInformation = new SharePointSearchResult();
                        fileInformation.Properties.Add("FileName", spFile.Name);
                        fileInformation.Properties.Add("Description", spFile.CheckInComment);
                        fileInformation.Properties.Add("MajorVersion", spFile.MajorVersion);
                        fileInformation.Properties.Add("MinorVersion", spFile.MinorVersion);
                        fileInformation.Properties.Add("ModifiedBy", spFile.ModifiedBy);
                        fileInformation.Properties.Add("DateModified", spFile.TimeLastModified);
                        fileInformation.Properties.Add("CreatedBy", spFile.Author);
                        fileInformation.Properties.Add("DateCreated", spFile.TimeCreated);
                        fileInformation.Properties.Add("ServerRelativeUrl", spFile.ServerRelativeUrl);
                        result.Add(fileInformation);
                    }
                    return result;
                }
            }
            catch (Exception e)
            {
                s_Logger.Error(string.Format("SearchFiles({0}) failed using ClientContext {1}.", relativeUrl, SharePointSiteUrl), e);
                throw;
            }
        }

        #endregion
    }
}