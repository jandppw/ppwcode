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
using System.Net;

#endregion

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Mock NOP implementation of <see cref="ISharePointClient"/>
    /// </summary>
    public class NopSharePointClient : ISharePointClient
    {
        #region ISharePointClient Members

        /// <inheritdoc cref="ISharePointClient.SharePointSiteUrl" />
        public string SharePointSiteUrl { get; set; }

        /// <inheritdoc cref="ISharePointClient.Credentials" />
        public ICredentials Credentials { get; set; }

        /// <inheritdoc cref="ISharePointClient.EnsureFolder" />
        public void EnsureFolder(string relativeUrl)
        {
            //NOP
        }

        /// <inheritdoc cref="ISharePointClient.DownloadDocument" />
        public SharePointDocument DownloadDocument(string relativeUrl)
        {
            // NOP
            return default(SharePointDocument);
        }

        /// <inheritdoc cref="ISharePointClient.UploadDocument" />
        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            //NOP
        }

        /// <inheritdoc cref="ISharePointClient.ValidateUri" />
        public bool ValidateUri(Uri sharePointUri)
        {
            //NOP
            return true;
        }

        /// <inheritdoc cref="ISharePointClient.OpenUri" />
        public void OpenUri(Uri uri)
        {
            //NOP
        }

        /// <inheritdoc cref="ISharePointClient.SearchFiles" />
        public List<SharePointSearchResult> SearchFiles(string url)
        {
            return new List<SharePointSearchResult>();
        }

        #endregion
    }
}