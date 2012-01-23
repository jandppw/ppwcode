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
using System.Diagnostics.Contracts;
using System.Net;

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
        /// Optional credentials for authentication
        /// </summary>
        ICredentials Credentials { get; set; }

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

        SharePointDocument DownloadDocument(string relativeUrl);

        void UploadDocument(string relativeUrl, SharePointDocument doc);

        /// <summary>
        /// Validate existence of specified Uri
        /// </summary>
        bool ValidateUri(Uri sharePointUri);

        /// <summary>
        /// Open specified uri in default web browser
        /// MUDO: Move this method to other assembly?
        /// </summary>
        void OpenUri(Uri uri);

        /// <summary>
        /// Return files found at specified url
        /// </summary>
        List<SharePointSearchResult> SearchFiles(string url);

        void RenameFolder(string baseRelativeUrl, string originalFolderName, string newFolderName);

        void RenameAllOccurrencesOfFolder(string baseRelativeUrl, string oldFolderName, string newFolderName);

        /// <summary>
        /// Create folder with given path.
        /// <para>
        /// Depending on the parameter "createFullPath", also create all intermediate missing folders,
        /// or otherwise, throw an exception if any of the intermediate folders is missing.
        /// </para>
        /// </summary>
        /// <param name="folderPath">path of folder to be created</param>
        /// <param name="createFullPath">create all intermediate missing folders</param>
        void CreateFolder(string folderPath, bool createFullPath);

        /// <summary>
        /// Delete folder with given path.
        /// <para>
        /// Depending on the parameter "deleteChildren", also delete all children if any exist,
        /// or otherwise, throw an exception if any children exist.
        /// </para>
        /// </summary>
        /// <param name="folderPath">path of folder to be deleted</param>
        /// <param name="deleteChildren">delete all children of folder if any exist</param>
        void DeleteFolder(string folderPath, bool deleteChildren);

        int CountAllOccurencesOfFolderInPath(string baseRelativeUrl, string folderName);

        bool CheckExistenceOfFolderWithExactPath(string folderPath);
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

        public ICredentials Credentials
        {
            get { return default(ICredentials); }
            set
            {
                // NOP;
            }
        }

        /// <summary>
        /// Create all folder in the given folder path, that do not yet exist in SharePoint.
        /// </summary>
        /// <param name="relativeUrl">the folder path </param>
        public void EnsureFolder(string relativeUrl)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(relativeUrl));
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
        }

        public SharePointDocument DownloadDocument(string relativeUrl)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(relativeUrl));
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
            return default(SharePointDocument);
        }

        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(relativeUrl != null);
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
            Contract.Requires(doc != null);
        }

        public bool ValidateUri(Uri sharePointUri)
        {
            Contract.Requires(sharePointUri != null);
            Contract.Requires(sharePointUri.OriginalString.StartsWith(SharePointSiteUrl));
            return default(bool);
        }

        public void OpenUri(Uri uri)
        {
            Contract.Requires(uri != null);
        }

        public List<SharePointSearchResult> SearchFiles(string url)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(url != null);
            return new List<SharePointSearchResult>();
        }

        /// <summary>
        /// Rename the folder in the given base path that has the given name.
        /// </summary>
        /// <param name="baseRelativeUrl">base folder</param>
        /// <param name="originalFolderName">folder in base path that has to be renamed</param>
        /// <param name="newFolderName">new name of the folder</param>
        public void RenameFolder(string baseRelativeUrl, string originalFolderName, string newFolderName)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(originalFolderName));
            Contract.Requires(!string.IsNullOrEmpty(newFolderName));
        }

        /// <summary>
        /// Rename all folders within the given base path, that have the given name.
        /// </summary>
        /// <param name="baseRelativeUrl">base path</param>
        /// <param name="oldFolderName">folder that has to be renamed</param>
        /// <param name="newFolderName">new name of the folder</param>
        public void RenameAllOccurrencesOfFolder(string baseRelativeUrl, string oldFolderName, string newFolderName)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(baseRelativeUrl));
            Contract.Requires(!string.IsNullOrEmpty(oldFolderName));
            Contract.Requires(!string.IsNullOrEmpty(newFolderName));
        }

        public void CreateFolder(string folderPath, bool createFullPath)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(folderPath));
        }

        public void DeleteFolder(string folderPath, bool deleteChildren )
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(folderPath));
        }

        /// <summary>
        /// Search and count the occurrences of folders with the given name in the given path.
        /// </summary>
        /// <param name="baseRelativeUrl">path to search</param>
        /// <param name="folderName">name of folder</param>
        /// <returns>number of occurrences</returns>
        public int CountAllOccurencesOfFolderInPath(string baseRelativeUrl, string folderName)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(baseRelativeUrl));
            Contract.Requires(!string.IsNullOrEmpty(folderName));
            return default(int);
        }

        /// <summary>
        /// Check whether the folder with the given path exists in SharePoint.
        /// </summary>
        /// <param name="folderPath">folder path</param>
        /// <returns></returns>
        public bool CheckExistenceOfFolderWithExactPath(string folderPath)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(folderPath));
            return default(bool);
        }

        #endregion
    }
}