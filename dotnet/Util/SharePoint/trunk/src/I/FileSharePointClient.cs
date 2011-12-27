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
using System.Linq;
using System.Net;
using System.Security.AccessControl;
using System.Security.Principal;
using System.Threading;
using System.Xml.Serialization;

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
        #region Helper classes

        public class Document
        {
            public Document()
            {
            }

            public Document(string relativeUrl, string fileName)
            {
                RelativeUrl = relativeUrl;
                FileName = fileName;
            }

            [XmlAttribute(AttributeName = @"Url")]
            public string RelativeUrl { get; set; }

            [XmlAttribute(AttributeName = @"FileName")]
            public string FileName { get; set; }
        }

        [XmlRoot(ElementName = @"Root")]
        public class RootDocument
        {
            public RootDocument()
            {
            }

            public RootDocument(IEnumerable<KeyValuePair<string, string>> documentMap)
            {
                Documents = documentMap
                    .Select(kvp => new Document(kvp.Key, kvp.Value))
                    .ToArray();
            }

            public Document[] Documents { get; set; }
        }

        #endregion

        #region Fields

        private static readonly string s_SharepointMock;
        private static readonly Mutex s_Mutex = new Mutex(false, @"PPWCode.Util.SharePoint.I.FileSharePointClient");
        private static readonly XmlSerializer s_Serializer = new XmlSerializer(typeof(RootDocument));

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

        private static byte[] ReadFile(string fileName)
        {
            string filePath = Path.Combine(s_SharepointMock, fileName);
            using (FileStream fileStream = new FileStream(filePath, FileMode.Open, FileAccess.Read))
            {
                return fileStream.ConvertToByteArray();
            }
        }

        private static void WriteFile(string fileName, byte[] content)
        {
            string filePath = Path.Combine(s_SharepointMock, fileName);
            using (BinaryWriter writer = new BinaryWriter(File.Open(filePath, FileMode.Create)))
            {
                writer.Write(content);
            }
        }

        private static RootDocument GetRootDocument(string filePath)
        {
            s_Mutex.WaitOne();
            try
            {
                using (FileStream stream = new FileStream(filePath, FileMode.Open))
                {
                    return (RootDocument)s_Serializer.Deserialize(stream);
                }
            }
            finally
            {
                s_Mutex.ReleaseMutex();
            }
        }

        private static IDictionary<string, string> GetDocumentMap()
        {
            RootDocument rootDocument = null;
            string filePath = Path.Combine(s_SharepointMock, @"Documents.xml");
            if (File.Exists(filePath))
            {
                rootDocument = GetRootDocument(filePath);
            }

            IDictionary<string, string> result;
            if (rootDocument != null && rootDocument.Documents != null)
            {
                result = rootDocument
                    .Documents
                    .AsEnumerable()
                    .ToDictionary(d => d.RelativeUrl, d => d.FileName);
            }
            else
            {
                result = new Dictionary<string, string>();
            }

            return result;
        }

        private static void SaveDocumentMap(IEnumerable<KeyValuePair<string, string>> documentMap)
        {
            if (documentMap != null)
            {
                string filePath = Path.Combine(s_SharepointMock, @"Documents.xml");
                RootDocument rootDocument = new RootDocument(documentMap);
                s_Mutex.WaitOne();
                try
                {
                    using (StreamWriter writer = new StreamWriter(filePath))
                    {
                        s_Serializer.Serialize(writer, rootDocument);
                    }
                }
                finally
                {
                    s_Mutex.ReleaseMutex();
                }
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
        }

        /// <inheritdoc cref="ISharePointClient.DownloadDocument" />
        public SharePointDocument DownloadDocument(string relativeUrl)
        {
            string fileName;
            IDictionary<string, string> documentMap = GetDocumentMap();
            if (documentMap.TryGetValue(relativeUrl, out fileName))
            {
                byte[] buffer = ReadFile(fileName);
                return new SharePointDocument(buffer);
            }
            return null;
        }

        /// <inheritdoc cref="ISharePointClient.UploadDocument" />
        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            if (doc == null || doc.Content == null)
            {
                return;
            }

            IDictionary<string, string> documentMap = GetDocumentMap();
            string fileName;
            if (documentMap.ContainsKey(relativeUrl))
            {
                fileName = documentMap[relativeUrl];
            }
            else
            {
                string tempFullFileName = Path
                    .GetTempFileName()
                    .Replace(".tmp", ".bin");
                fileName = Path.GetFileName(tempFullFileName);
                documentMap.Add(relativeUrl, fileName);
                SaveDocumentMap(documentMap);
            }
            WriteFile(fileName, doc.Content);
        }

        /// <inheritdoc cref="ISharePointClient.ValidateUri" />
        public bool ValidateUri(Uri sharePointUri)
        {
            return true;
        }

        /// <inheritdoc cref="ISharePointClient.OpenUri" />
        public void OpenUri(Uri uri)
        {
            Process.Start(new ProcessStartInfo
            {
                UseShellExecute = true,
                FileName = s_SharepointMock,
                Verb = "Open",
                LoadUserProfile = true
            });
        }

        /// <inheritdoc cref="ISharePointClient.SearchFiles" />
        public List<SharePointSearchResult> SearchFiles(string url)
        {
            List<SharePointSearchResult> result = new List<SharePointSearchResult>();
            IDictionary<string, string> documentMap = GetDocumentMap();
            Uri fileUri = new Uri(url);
            string relativeUrl = @"/" + fileUri.GetComponents(UriComponents.Path, UriFormat.SafeUnescaped);
            var pairs = documentMap
                .Where(p => p.Key.StartsWith(relativeUrl));
            foreach (var pair in pairs)
            {
                SharePointSearchResult fileInformation = new SharePointSearchResult();
                string physicalPathName = Path.Combine(s_SharepointMock, pair.Value);
                FileSecurity fs = File.GetAccessControl(physicalPathName);
                IdentityReference sid = fs.GetOwner(typeof(SecurityIdentifier));
                IdentityReference ntAccount = sid.Translate(typeof(NTAccount));

                fileInformation.Properties.Add("FileName", Path.GetFileName(pair.Key));
                fileInformation.Properties.Add("ServerRelativeUrl", pair.Key);
                fileInformation.Properties.Add("Description", string.Empty);
                fileInformation.Properties.Add("MajorVersion", 1);
                fileInformation.Properties.Add("MinorVersion", 1);
                fileInformation.Properties.Add("ModifiedBy", ntAccount.Value);
                fileInformation.Properties.Add("DateModified", File.GetLastWriteTime(physicalPathName));
                fileInformation.Properties.Add("CreatedBy", ntAccount.Value);
                fileInformation.Properties.Add("DateCreated", File.GetCreationTime(physicalPathName));
                result.Add(fileInformation);
            }
            return result;
        }

        #endregion
    }
}