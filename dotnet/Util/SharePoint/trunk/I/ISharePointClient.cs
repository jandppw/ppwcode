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
            { // NOP;
            }
        }

        public void EnsureFolder(string relativeUrl)
        {
            Contract.Requires(!string.IsNullOrEmpty(SharePointSiteUrl));
            Contract.Requires(!string.IsNullOrEmpty(relativeUrl));
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
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

        #endregion
    }
}
