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
using System.Data.SqlTypes;
using System.IO;
using System.Text;

using DM.Build.Yukon.Attributes;

using Microsoft.SqlServer.Server;

#endregion

namespace PPWCode.Util.SqlServer.I
{
    [Serializable]
    [SqlUserDefinedAggregate(
        Format.UserDefined, //use clr serialization to serialize the intermediate result
        IsInvariantToNulls = true, //optimizer property
        IsInvariantToDuplicates = false, //optimizer property
        IsInvariantToOrder = false, //optimizer property
        MaxByteSize = 8000) //maximum size in bytes of persisted value
    ]
    public class fnList : IBinarySerialize
    {
        private StringBuilder intermediateResult;

        public void Init()
        {
            intermediateResult = new StringBuilder();
        }

        public void Accumulate([SqlParamFacet(MaxSize = 128)] SqlString value, [SqlParamFacet(MaxSize = 1)] SqlString Separator)
        {
            if (value.IsNull)
            {
                return;
            }
            intermediateResult.Append(value.Value).Append(Separator.IsNull ? ' ' : Separator.Value[0]);
        }

        public void Merge(fnList other)
        {
            intermediateResult.Append(other.intermediateResult);
        }

        [return: SqlFacet(MaxSize = -1)]
        public SqlString Terminate()
        {
            //delete the trailing comma, if any
            if ((intermediateResult != null) && (intermediateResult.Length > 0))
            {
                return new SqlString(intermediateResult.ToString(0, intermediateResult.Length - 1));
            }

            return SqlString.Null;
        }

        public void Read(BinaryReader r)
        {
            intermediateResult = new StringBuilder(r.ReadString());
        }

        public void Write(BinaryWriter w)
        {
            w.Write(intermediateResult.ToString());
        }
    }
}