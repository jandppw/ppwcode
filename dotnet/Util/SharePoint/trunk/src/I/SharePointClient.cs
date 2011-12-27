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
using System.Net;

using log4net;

using Microsoft.SharePoint.Client;

using PPWCode.Util.SharePoint.I.Helpers;

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

            if (Credentials != null)
            {
                s_Logger.Debug("GetSharePointClientContext: Credentials ok");
                ctx.Credentials = Credentials;
            }
            else
            {
                s_Logger.Debug("GetSharePointClientContext: Credentials not ok");
            }

            ctx.Load(ctx.Site.RootWeb);
            ctx.ExecuteQuery();
            s_Logger.Debug(string.Format("Connect to SharePoint using user {0}", ctx.Web.CurrentUser));
            return ctx;
        }

        private static void CreateFolder(ClientContext spClientContext, string relativeUrl)
        {
            string workUrl = relativeUrl.StartsWith("/")
                                 ? relativeUrl.Substring(1)
                                 : relativeUrl;
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

        public ICredentials Credentials { get; set; }

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

        public SharePointDocument DownloadDocument(string relativeUrl)
        {
            try
            {
                using (ClientContext spClientContext = GetSharePointClientContext())
                {
                    Web currentWeb = spClientContext.Web;

                    spClientContext.Load(currentWeb);
                    spClientContext.ExecuteQuery();

                    var fi = File.OpenBinaryDirect(spClientContext, relativeUrl);

                    if (fi == null)
                    {
                        throw new ApplicationException(@"fi == null");
                    }

                    if (fi.Stream == null)
                    {
                        throw new ApplicationException(@"fi.Stream == null");
                    }
                    return new SharePointDocument(fi.Stream.ConvertToByteArray());
                }
            }
            catch (Exception e)
            {
                s_Logger.Error(string.Format("DownloadDocument({0}) failed using ClientContext {1}.", relativeUrl, SharePointSiteUrl), e);
                throw;
            }
        }

        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            using (ClientContext spClientContext = GetSharePointClientContext())
            {
                //Check if the url exists
                int index = relativeUrl.LastIndexOf("/");
                string parentFolder = relativeUrl.Substring(0, index);

                //Create intermediate folders if not exist
                EnsureFolder(parentFolder);
                spClientContext.ExecuteQuery();

                try
                {
                    string targetUrl = string.Format("{0}{1}", SharePointSiteUrl, relativeUrl);

                    // Create a PUT Web request to upload the file.
                    WebRequest request = WebRequest.Create(targetUrl);

                    //Set credentials of the current security context
                    request.Credentials = CredentialCache.DefaultCredentials;
                    request.Method = "PUT";

                    // Create buffer to transfer file
                    byte[] fileBuffer = new byte[1024];

                    // Write the contents of the local file to the request stream.
                    using (Stream stream = request.GetRequestStream())
                    {
                        //Load the content from local file to stream
                        using (MemoryStream ms = new MemoryStream(doc.Content))
                        {
                            ms.Position = 0;

                            // Get the start point
                            int startBuffer = ms.Read(fileBuffer, 0, fileBuffer.Length);
                            for (int i = startBuffer; i > 0; i = ms.Read(fileBuffer, 0, fileBuffer.Length))
                            {
                                stream.Write(fileBuffer, 0, i);
                            }
                        }
                    }

                    // Perform the PUT request
                    WebResponse response = request.GetResponse();
                    if (response != null)
                    {
                        //Close response
                        response.Close();
                    }
                }
                catch (Exception e)
                {
                    s_Logger.Error(string.Format("UploadDocument({0}) failed using ClientContext {1}.", relativeUrl, SharePointSiteUrl), e);
                    throw;
                }
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
                        if (Credentials != null)
                        {
                            clientContext.Credentials = Credentials;
                        }

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

        public List<SharePointSearchResult> SearchFiles(string url)
        {
            try
            {
                using (ClientContext spClientContext = GetSharePointClientContext())
                {
                    Web rootWeb = spClientContext.Site.RootWeb;
                    Folder spFolder = rootWeb.GetFolderByServerRelativeUrl(url); // "/Shared%20Documents/MEERTENS%20MATHIEU%20-%2051062002701/Construo/Actua/");
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
                s_Logger.Error(string.Format("SearchFiles({0}) failed using ClientContext {1}.", url, SharePointSiteUrl), e);
                throw;
            }
        }

        #endregion
    }
}