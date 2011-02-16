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
        void UploadDocument(string relativeUrl, SharePointDocument doc);
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

        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            Contract.Requires(SharePointSiteUrl != null && SharePointSiteUrl != string.Empty);
            Contract.Requires(relativeUrl != null);
            Contract.Requires(!relativeUrl.StartsWith(SharePointSiteUrl));
            Contract.Requires(doc != null);
        }
        #endregion
    }
}
