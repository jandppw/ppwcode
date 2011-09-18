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
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Net;

using PPWCode.Util.SharePoint.I.Helpers;

#endregion

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Mock implementation of <see cref="ISharePointClient"/>. 
    /// Instead of storing files on sharepoint it will be on disk.
    /// </summary>
    public class FileSharePointClient :
        ISharePointClient
    {
        #region Fields

        private static readonly string s_SharepointMock;

        #endregion

        #region Constructors

        static FileSharePointClient()
        {
            object valueFromConfig = ConfigurationManager.AppSettings[@"SharePointMock"];
            s_SharepointMock = (valueFromConfig != null
                                    ? Convert.ToString(valueFromConfig)
                                    : @"c:\SharepointMock\").ToUpper();
        }

        #endregion

        #region Private Helpers

        private static byte[] ReadFile(string filePath)
        {
            using(FileStream fileStream = new FileStream(filePath, FileMode.Open, FileAccess.Read))
            {
                return fileStream.ConvertToByteArray();
            }
        }


        #endregion

        #region Implementation of ISharePointClient

        /// <inheritdoc cref="ISharePointClient.SharePointSiteUrl" />
        public string SharePointSiteUrl { get; set; }

        /// <inheritdoc cref="ISharePointClient.Credentials" />
        public ICredentials Credentials { get; set; }

        /// <inheritdoc cref="ISharePointClient.EnsureFolder" />
        public void EnsureFolder(string relativeUrl)
        {
            if (string.IsNullOrEmpty(relativeUrl))
            {
                return;
            }

            string combinedPath = Path.Combine(s_SharepointMock, relativeUrl);
            if (!Directory.Exists(combinedPath))
            {
                Directory.CreateDirectory(combinedPath);
            }
        }

        /// <inheritdoc cref="ISharePointClient.DownloadDocument" />
        public SharePointDocument DownloadDocument(string relativeUrl)
        {
            string filePath = Path.Combine(s_SharepointMock, relativeUrl);
            byte[] buffer = ReadFile(filePath);
            return new SharePointDocument(buffer);
        }

        /// <inheritdoc cref="ISharePointClient.UploadDocument" />
        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            if (doc == null || doc.Content == null)
            {
                return;
            }

            string filePath = Path.Combine(s_SharepointMock, relativeUrl);
            using (BinaryWriter writer = new BinaryWriter(File.Open(filePath, FileMode.Create)))
            {
                writer.Write(doc.Content);
            }
        }

        /// <inheritdoc cref="ISharePointClient.ValidateUri" />
        public bool ValidateUri(Uri sharePointUri)
        {
            return true;
        }

        /// <inheritdoc cref="ISharePointClient.OpenUri" />
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

        /// <inheritdoc cref="ISharePointClient.SearchFiles" />
        public List<SharePointSearchResult> SearchFiles(string url)
        {
            Uri fileUri = new Uri(url);
            string pathName = fileUri.GetComponents(UriComponents.Path, UriFormat.SafeUnescaped);
            List<SharePointSearchResult> result = new List<SharePointSearchResult>();
            foreach (string fileName in Directory.GetFiles(pathName))
            {
                SharePointSearchResult fileInformation = new SharePointSearchResult();
                fileInformation.Properties.Add("FileName", fileName);
                result.Add(fileInformation);
            }
            return result;
        }

        #endregion
    }
}