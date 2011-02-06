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
using System.Text.RegularExpressions;

using DM.Build.Yukon.Attributes;

using Microsoft.SqlServer.Server;

#endregion

namespace PPWCode.Util.SqlServer.I
{
    public class StringFuncs
    {
        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 256)]
        public static SqlString fnLPad([SqlParamFacet(MaxSize = 256)] SqlString aStr, SqlInt32 aTotalWidth, [SqlParamFacet(MaxSize = 1)] SqlString aPaddingChar)
        {
            if (aStr.IsNull)
            {
                return SqlString.Null;
            }
            int totalWidth = Math.Min(aTotalWidth.IsNull ? 256 : aTotalWidth.Value, 256);
            char paddingChar = !aPaddingChar.IsNull && aPaddingChar.Value.Length == 1 ? aPaddingChar.Value[0] : ' ';

            return new SqlString(aStr.Value.PadLeft(totalWidth, paddingChar));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 256)]
        public static SqlString fnRPad([SqlParamFacet(MaxSize = 256)] SqlString aStr, SqlInt32 aTotalWidth, [SqlParamFacet(MaxSize = 1)] SqlString aPaddingChar)
        {
            if (aStr.IsNull)
            {
                return SqlString.Null;
            }
            int totalWidth = Math.Min(aTotalWidth.IsNull ? 256 : aTotalWidth.Value, 256);
            char paddingChar = !aPaddingChar.IsNull && aPaddingChar.Value.Length == 1 ? aPaddingChar.Value[0] : ' ';

            return new SqlString(aStr.Value.PadRight(totalWidth, paddingChar));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 1024)]
        public static SqlString fnTrim([SqlParamFacet(MaxSize = 1024)] SqlString aStr)
        {
            if (aStr.IsNull)
            {
                return SqlString.Null;
            }
            return new SqlString(aStr.Value.Trim());
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 1024)]
        public static SqlString fnTrimFull([SqlParamFacet(MaxSize = 1024)] SqlString aStr)
        {
            if (aStr.IsNull)
            {
                return SqlString.Null;
            }
            string TrimmedStr = aStr.Value.Trim();
            const string DoubleSpace = "  ";
            const string SingleSpace = " ";

            while (TrimmedStr.Contains(DoubleSpace))
            {
                TrimmedStr = TrimmedStr.Replace(DoubleSpace, SingleSpace);
            }

            return new SqlString(TrimmedStr);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnRegexMatch([SqlParamFacet(MaxSize = 256)] SqlString aStr, [SqlParamFacet(MaxSize = 128)] SqlString Pattern)
        {
            if (aStr.IsNull || Pattern.IsNull)
            {
                return SqlBoolean.Null;
            }
            return new SqlBoolean(Regex.IsMatch(aStr.Value, Pattern.Value));
        }
    }
}