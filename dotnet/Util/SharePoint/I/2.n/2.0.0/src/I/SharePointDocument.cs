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

using System.Collections.Generic;

#endregion

namespace PPWCode.Util.SharePoint.I
{
    public class SharePointDocument
    {
        public byte[] Content { get; set; }
        public Dictionary<string, object> Properties { get; set; }

        public SharePointDocument() :
            this(null)
        {
        }

        public SharePointDocument(byte[] content) :
            this(content, new Dictionary<string, object>())
        {
        }

        public SharePointDocument(byte[] content, Dictionary<string, object> props)
        {
            Content = content;
            Properties = props;
        }
    }
}