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

using System.Collections.Generic;

namespace PPWCode.Util.SharePoint.I
{
    /// <summary>
    /// Mock NOP implementation of <see cref="ISharePointClient"/>
    /// </summary>
    public class NopSharePointClient : ISharePointClient
    {
        #region ISharePointClient Members

        public string SharePointSiteUrl { get; set; }

        public void EnsureFolder(string relativeUrl)
        {
            //NOP
        }

        public void UploadDocument(string relativeUrl, SharePointDocument doc)
        {
            //NOP
        }

        public bool ValidateUri(System.Uri sharePointUri)
        {
            //NOP
            return true;
        }

        public void OpenUri(System.Uri uri)
        {
            //NOP
        }

        public List<SharePointSearchResult> SearchFiles(string url)
        {
            return new List<SharePointSearchResult>();
        }

        #endregion
    }
}
