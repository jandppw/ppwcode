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
using System.Data.SqlTypes;
using System.Text;
using System.Text.RegularExpressions;

using DM.Build.Yukon.Attributes;

using Microsoft.SqlServer.Server;

#endregion

namespace PPWCode.Util.SQLServer.I
{
    public class AdministrativeUtils
    {
        private struct sIBANCountry
        {
            #region Properties

            public int IBANLength { get; set; }
            public string Pattern { get; set; }

            #endregion

            #region Constructors

            public sIBANCountry(int aIBANLength, string aPattern)
                : this()
            {
                IBANLength = aIBANLength;
                Pattern = aPattern;
            }

            #endregion
        }

        private struct sBBANCountry
        {
            #region Properties

            public int BBANLength { get; set; }
            public string Pattern { get; set; }

            #endregion

            #region Constructors

            public sBBANCountry(int aBBANLength, string aPattern)
                : this()
            {
                BBANLength = aBBANLength;
                Pattern = aPattern;
            }

            #endregion
        }

        #region IBAN regular expressions

        private static readonly IDictionary<string, sIBANCountry> IBANCountries = new Dictionary<string, sIBANCountry>
        {
            {
                "AD", new sIBANCountry(24, "[0-9]{10}[0-9A-Z]{12}")
                }, //  1. Andorra
            {
                "AT", new sIBANCountry(20, "[0-9]{18}")
                }, //  2. Austria
            {
                "BE", new sIBANCountry(16, "[0-9]{14}")
                }, //  3. Belgium 
            {
                "BA", new sIBANCountry(20, "[0-9]{18}")
                }, //  4. Bosnia and Herzegovina
            {
                "BG", new sIBANCountry(22, "[0-9]{2}[A-Z]{4}[0-9]{6}[0-9A-Z]{8}")
                }, //  5. Bulgaria
            {
                "HR", new sIBANCountry(21, "[0-9]{19}")
                }, //  6. Croatia
            {
                "CY", new sIBANCountry(28, "[0-9]{10}[0-9A-Z]{16}")
                }, //  7. Cyprus
            {
                "CZ", new sIBANCountry(24, "[0-9]{22}")
                }, //  8. Czech Republic
            {
                "DK", new sIBANCountry(18, "[0-9]{16}")
                }, //  9. Denmark
            {
                "EE", new sIBANCountry(20, "[0-9]{18}")
                }, // 10. Estonia
            {
                "FI", new sIBANCountry(18, "[0-9]{16}")
                }, // 11. Finland
            {
                "FR", new sIBANCountry(27, "[0-9]{12}[0-9A-Z]{11}[0-9]{2}")
                }, // 12. France
            {
                "DE", new sIBANCountry(22, "[0-9]{20}")
                }, // 13. Germany
            {
                "GI", new sIBANCountry(23, "[0-9]{2}[A-Z]{4}[0-9A-Z]{15}")
                }, // 14. Gibraltar
            {
                "GR", new sIBANCountry(27, "[0-9]{9}[0-9A-Z]{16}")
                }, // 15. Greece
            {
                "HU", new sIBANCountry(28, "[0-9]{26}")
                }, // 16. Hungary
            {
                "IS", new sIBANCountry(26, "[0-9]{24}")
                }, // 17. Iceland
            {
                "IE", new sIBANCountry(22, "[0-9]{2}[A-Z]{4}[0-9]{14}")
                }, // 18. Ireland
            {
                "IL", new sIBANCountry(23, "[0-9]{21}")
                }, // 19. Israel
            {
                "IT", new sIBANCountry(27, "[0-9]{2}[A-Z][0-9]{10}[0-9A-Z]{12}")
                }, // 20. Italy
            {
                "LV", new sIBANCountry(21, "[0-9]{2}[A-Z]{4}[0-9A-Z]{13}")
                }, // 21. Latvia
            {
                "LI", new sIBANCountry(21, "[0-9]{7}[0-9A-Z]{12}")
                }, // 22. Principality of Liechtenstein
            {
                "LT", new sIBANCountry(20, "[0-9]{18}")
                }, // 23. Lithuania
            {
                "LU", new sIBANCountry(20, "[0-9]{5}[0-9A-Z]{13}")
                }, // 24. Luxembourg
            {
                "MK", new sIBANCountry(19, "[0-9]{5}[0-9A-Z]{10}[0-9]{2}")
                }, // 25. Macedonia, former Yugoslav Republic of
            {
                "MT", new sIBANCountry(31, "[0-9]{2}[A-Z]{4}[0-9]{5}[0-9A-Z]{18}")
                }, // 26. Malta
            {
                "MU", new sIBANCountry(30, "[0-9]{2}[A-Z]{4}[0-9]{19}[A-Z]{3}")
                }, // 27. Mauritius
            {
                "MC", new sIBANCountry(27, "[0-9]{12}[0-9A-Z]{11}[0-9]{2}")
                }, // 28. Monaco
            {
                "ME", new sIBANCountry(22, "[0-9]{20}")
                }, // 29. Montenegro
            {
                "NL", new sIBANCountry(18, "[0-9]{2}[A-Z]{4}[0-9]{10}")
                }, // 30. The Netherlands
            {
                "NO", new sIBANCountry(15, "[0-9]{13}")
                }, // 31. Norway
            {
                "PL", new sIBANCountry(28, "[0-9]{26}")
                }, // 32. Poland
            {
                "PT", new sIBANCountry(25, "[0-9]{23}")
                }, // 33. Portugal
            {
                "RO", new sIBANCountry(24, "[0-9]{2}[A-Z]{4}[0-9A-Z]{16}")
                }, // 34. Romania
            {
                "SM", new sIBANCountry(27, "[0-9]{2}[A-Z][0-9]{10}[0-9A-Z]{12}")
                }, // 35. San Marino
            {
                "RS", new sIBANCountry(22, "[0-9]{20}")
                }, // 36. Serbia
            {
                "SK", new sIBANCountry(24, "[0-9]{22}")
                }, // 37. Slovak Republic
            {
                "SI", new sIBANCountry(19, "[0-9]{17}")
                }, // 38. Slovenia
            {
                "ES", new sIBANCountry(24, "[0-9]{22}")
                }, // 39. Spain
            {
                "SE", new sIBANCountry(24, "[0-9]{22}")
                }, // 40. Sweden
            {
                "CH", new sIBANCountry(21, "[0-9]{7}[0-9A-Z]{12}")
                }, // 41. Switzerland
            {
                "TN", new sIBANCountry(24, "59[0-9]{20}")
                }, // 42. Tunisia
            {
                "TR", new sIBANCountry(26, "[0-9]{7}[0-9A-Z]{17}")
                }, // 43. Turkey
            {
                "GB", new sIBANCountry(22, "[0-9]{2}[A-Z]{4}[0-9]{14}")
                } // 44. United Kingdom
        };

        #endregion

        #region IBAN Alphanumeric to numeric translation

        private static readonly IDictionary<char, int> IBANConversions = new Dictionary<char, int>
        {
            {
                'A', 10
                },
            {
                'B', 11
                },
            {
                'C', 12
                },
            {
                'D', 13
                },
            {
                'E', 14
                },
            {
                'F', 15
                },
            {
                'G', 16
                },
            {
                'H', 17
                },
            {
                'I', 18
                },
            {
                'J', 19
                },
            {
                'K', 20
                },
            {
                'L', 21
                },
            {
                'M', 22
                },
            {
                'N', 23
                },
            {
                'O', 24
                },
            {
                'P', 25
                },
            {
                'Q', 26
                },
            {
                'R', 27
                },
            {
                'S', 28
                },
            {
                'T', 29
                },
            {
                'U', 30
                },
            {
                'V', 31
                },
            {
                'W', 32
                },
            {
                'X', 33
                },
            {
                'Y', 34
                },
            {
                'Z', 35
                }
        };

        #endregion

        private static readonly int LENGTH_RRN = 11;
        private static readonly int LENGTH_KBO = 10;
        private static readonly int LENGTH_RSZ = 10;
        private static readonly int LENGTH_VAT = 9;

        private static long Mod97Checknumber(long BaseNum)
        {
            long Result = BaseNum % 97;
            return Result == 0 ? 97 : Result;
        }

        private static string GetDigitStream(string Stream)
        {
            if (Stream == null)
            {
                return null;
            }

            StringBuilder sb = new StringBuilder(Stream.Length);
            for (int i = 0; i < Stream.Length; i++)
            {
                char ch = Stream[i];
                if (char.IsDigit(ch))
                {
                    sb.Append(ch);
                }
            }
            return sb.ToString();
        }

        private static void ParseRR(string RRN, out DateTime? Birthdate, out int? Sexe)
        {
            Birthdate = null;
            Sexe = null;
            string DigitStream = GetDigitStream(RRN);
            if (fnValidRRN(RRN) && (DigitStream.Length > 0))
            {
                bool CalcSexe = false;
                bool CalcBirthDate = false;
                int YY = int.Parse(DigitStream.Substring(0, 2));
                int MM = int.Parse(DigitStream.Substring(2, 2));
                int DD = int.Parse(DigitStream.Substring(4, 2));
                int VVV = int.Parse(DigitStream.Substring(6, 3));

                int YYOffset;
                {
                    long NumberBefore2000 = long.Parse(DigitStream.Substring(0, 9));
                    int Rest = 97 - int.Parse(DigitStream.Substring(9, 2));
                    YYOffset = (NumberBefore2000 % 97) == Rest ? 1900 : 2000;
                }
                YY = YY + YYOffset;

                if (MM < 20)
                {
                    CalcSexe = true;
                    CalcBirthDate = true;
                }
                else if ((20 <= MM) && (MM < 40))
                {
                    MM -= 20;
                    CalcBirthDate = true;
                }
                else if ((40 <= MM) && (MM < 60))
                {
                    MM -= 40;
                    CalcSexe = true;
                    CalcBirthDate = true;
                }

                if (CalcSexe == true)
                {
                    Sexe = (VVV == 0) ? 0 : ((VVV % 2) == 1) ? 1 : 2;
                }
                if (CalcBirthDate == true)
                {
                    try
                    {
                        Birthdate = new DateTime(YY, MM, DD);
                        if (Birthdate > DateTime.Today)
                        {
                            Birthdate = null;
                        }
                    }
                    catch
                    {
                        Birthdate = null;
                    }
                }
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnGetDigitStream([SqlParamFacet(MaxSize = 64)] SqlString aStream)
        {
            if (aStream.IsNull)
            {
                return SqlString.Null;
            }
            return new SqlString(GetDigitStream(aStream.Value));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidIBAN([SqlParamFacet(MaxSize = 64)] SqlString aIBAN)
        {
            if (aIBAN.IsNull)
            {
                return SqlBoolean.Null;
            }

            string IBAN = aIBAN.Value;
            // Step 1. Size should be at least 2
            if (IBAN.Length < 2)
            {
                return SqlBoolean.False;
            }

            // Step 2. Check country code
            string CountryCode = IBAN.Substring(0, 2).ToUpper();
            sIBANCountry IBANCountry;
            if (!IBANCountries.TryGetValue(CountryCode, out IBANCountry))
            {
                return SqlBoolean.False;
            }

            // Fase 3. delete all non-alphanumeric characters
            StringBuilder sb = new StringBuilder(50);
            for (int i = 0; i < IBAN.Length; i++)
            {
                char ch = char.ToUpper(IBAN[i]);
                if (char.IsLetterOrDigit(ch) == true)
                {
                    sb.Append(ch);
                }
            }

            // Fase 4: Check the length of the IBAN nr according the country code
            if (IBANCountry.IBANLength != sb.Length)
            {
                return SqlBoolean.False;
            }

            // Fase 4b: Check regular expression if known
            if (IBANCountry.Pattern != string.Empty)
            {
                Regex regex = new Regex(string.Concat(CountryCode, IBANCountry.Pattern));
                if (!regex.IsMatch(sb.ToString()))
                {
                    return SqlBoolean.False;
                }
            }

            // Fase 5: Move the first four characters of the IBAN to the right of the number.
            sb.Append(sb.ToString().Substring(0, 4));
            sb.Remove(0, 4);

            // Fase 6: Convert the letters into numerics in accordance with the conversion table.
            StringBuilder sb2 = new StringBuilder(50);
            for (int i = 0; i < sb.ToString().Length; i++)
            {
                char ch = sb.ToString()[i];
                if (char.IsLetter(ch) == true)
                {
                    int num;
                    if (IBANConversions.TryGetValue(ch, out num))
                    {
                        sb2.Append(num.ToString());
                    }
                }
                else
                {
                    sb2.Append(ch);
                }
            }

            // Fase 7: Check MOD 97-10 (see ISO 7064).  
            // For the check digits to be correct, 
            // the remainder after calculating the modulus 97 must be 1.
            // We will use integers instead of floating point numnbers for precision.
            // BUT if the number is too long for the software implementation of
            // integers (a signed 32/64 bits represents 9/18 digits), then the 
            // calculation can be split up into consecutive remainder calculations
            // on integers with a maximum of 9 or 18 digits.
            // I wil choose 32 bit integers.
            int mod97 = 0, n = 9;
            string s9 = sb2.ToString().Substring(0, n);
            while (s9.Length > 0)
            {
                sb2.Remove(0, n);
                mod97 = int.Parse(s9) % 97;
                if (sb2.Length > 0)
                {
                    n = (mod97 < 10) ? 8 : 7;
                    n = sb2.Length < n ? sb2.Length : n;
                    s9 = string.Concat(mod97.ToString(), sb2.ToString().Substring(0, n));
                }
                else
                {
                    s9 = string.Empty;
                }
            }

            return (mod97 == 1) ? SqlBoolean.True : SqlBoolean.False;
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatIBANElectronicVersion([SqlParamFacet(MaxSize = 64)] SqlString aIBAN)
        {
            if ((aIBAN.IsNull) || !fnValidIBAN(aIBAN))
            {
                return SqlString.Null;
            }

            string IBAN = aIBAN.Value;
            StringBuilder sb = new StringBuilder(50);

            for (int i = 0; i < IBAN.Length; i++)
            {
                char ch = char.ToUpper(IBAN[i]);
                if (char.IsLetterOrDigit(ch) == true)
                {
                    sb.Append(ch);
                }
            }
            return new SqlString(sb.ToString());
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatIBANPaperVersion([SqlParamFacet(MaxSize = 64)] SqlString aIBAN)
        {
            if ((aIBAN.IsNull) || !fnValidIBAN(aIBAN))
            {
                return SqlString.Null;
            }

            string IBAN = aIBAN.Value;
            StringBuilder sb = new StringBuilder(50);

            for (int i = 0, j = 0; i < IBAN.Length; i++)
            {
                char ch = char.ToUpper(IBAN[i]);
                if (char.IsLetterOrDigit(ch) == true)
                {
                    if (j == 4)
                    {
                        sb.Append(' ');
                        j = 0;
                    }
                    j++;
                    sb.Append(ch);
                }
            }
            return new SqlString(sb.ToString());
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidBBAN([SqlParamFacet(MaxSize = 64)] SqlString aBBAN)
        {
            if (aBBAN.IsNull)
            {
                return SqlBoolean.Null;
            }

            string DigitStream = GetDigitStream(aBBAN.Value);
            bool Result = (DigitStream.Length == 12);
            if (Result)
            {
                long rest = Mod97Checknumber(long.Parse(DigitStream.Substring(0, 10)));
                Result = (rest == long.Parse(DigitStream.Substring(10, 2)));
            }
            return new SqlBoolean(Result);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatBBANElectronicVersion([SqlParamFacet(MaxSize = 64)] SqlString aBBAN)
        {
            if (aBBAN.IsNull || !fnValidBBAN(aBBAN))
            {
                return SqlString.Null;
            }

            return fnGetDigitStream(aBBAN);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatBBANPaperVersion([SqlParamFacet(MaxSize = 64)] SqlString aBBAN)
        {
            if (aBBAN.IsNull || !fnValidBBAN(aBBAN))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aBBAN.Value);
            return new SqlString(string.Format("{0}-{1}-{2}",
                                               DigitStream.Substring(0, 3),
                                               DigitStream.Substring(3, 7),
                                               DigitStream.Substring(10, 2)));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnBBAN2IBAN([SqlParamFacet(MaxSize = 64)] SqlString aBBAN)
        {
            aBBAN = fnFormatBBANElectronicVersion(aBBAN);
            if (aBBAN.IsNull)
            {
                return SqlString.Null;
            }

            // Verplaats BE + '00' achteraan het banknummer
            StringBuilder sb = new StringBuilder(aBBAN.Value);
            sb.Append(IBANConversions['B'].ToString());
            sb.Append(IBANConversions['E'].ToString());
            sb.Append("00");

            // Calculate Check Digits
            // Calculate MOD 97-10 (see ISO 7064)
            // For the check digits to be correct, 
            // the remainder after calculating the modulus 97 must be 1.
            // We will use integers instead of floating point numnbers for precision.
            // BUT if the number is too long for the software implementation of
            // integers (a signed 32/64 bits represents 9/18 digits), then the 
            // calculation can be split up into sonsecutive remainder calculations
            // on integers with a maximum of 9 or 18 digits.
            // I wil choose 32 bit integers.
            int mod97 = 0, n = 9;
            string s9 = sb.ToString().Substring(0, n);
            while (s9.Length > 0)
            {
                sb.Remove(0, n);
                mod97 = int.Parse(s9) % 97;
                if (sb.Length > 0)
                {
                    n = (mod97 < 10) ? 8 : 7;
                    n = sb.Length < n ? sb.Length : n;
                    s9 = string.Concat(mod97.ToString(), sb.ToString().Substring(0, n));
                }
                else
                {
                    s9 = string.Empty;
                }
            }
            mod97 = 98 - mod97;

            return new SqlString(string.Concat("BE", mod97.ToString("00"), aBBAN.Value));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnIBAN2BBAN([SqlParamFacet(MaxSize = 64)] SqlString aIBAN)
        {
            aIBAN = fnFormatIBANElectronicVersion(aIBAN);
            if (aIBAN.IsNull)
            {
                return SqlString.Null;
            }

            string IBAN = aIBAN.Value;
            if (IBAN.Substring(0, 2) == "BE")
            {
                return new SqlString(IBAN.Substring(4, 12));
            }
            else
            {
                return SqlString.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidKBO([SqlParamFacet(MaxSize = 64)] SqlString aKBO)
        {
            if (aKBO.IsNull)
            {
                return SqlBoolean.Null;
            }

            bool Result = false;
            string DigitStream = GetDigitStream(aKBO.Value);
            if (DigitStream.Length < LENGTH_KBO)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_KBO, '0');
            }
            if (DigitStream.Length == LENGTH_KBO)
            {
                long rest = 97 - (long.Parse(DigitStream.Substring(0, 8)) % 97);
                Result = (rest == long.Parse(DigitStream.Substring(8, 2)));
            }
            return new SqlBoolean(Result);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatKBOElectronicVersion([SqlParamFacet(MaxSize = 64)] SqlString aKBO)
        {
            if (aKBO.IsNull || !fnValidKBO(aKBO))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aKBO.Value);
            if (DigitStream.Length < LENGTH_KBO)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_KBO, '0');
            }
            return new SqlString(DigitStream);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatKBOPaperVersion([SqlParamFacet(MaxSize = 64)] SqlString aKBO)
        {
            if (aKBO.IsNull || !fnValidKBO(aKBO))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aKBO.Value);
            if (DigitStream.Length < LENGTH_KBO)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_KBO, '0');
            }
            return new SqlString(string.Format("{0}.{1}.{2}",
                                               DigitStream.Substring(0, 4),
                                               DigitStream.Substring(4, 3),
                                               DigitStream.Substring(7, 3)));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidRRN([SqlParamFacet(MaxSize = 64)] SqlString aRRN)
        {
            if (aRRN.IsNull)
            {
                return SqlBoolean.Null;
            }

            string DigitStream = GetDigitStream(aRRN.Value);
            if (DigitStream.Length < LENGTH_RRN)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RRN, '0');
            }

            if (DigitStream.Length == LENGTH_RRN)
            {
                string Number = DigitStream.Substring(0, 9);
                long NumberBefore2000 = long.Parse(Number);
                long NumberAfter2000 = long.Parse(string.Concat('2', Number));
                int Rest = 97 - int.Parse(DigitStream.Substring(9, 2));
                return new SqlBoolean(((NumberBefore2000 % 97) == Rest) || ((NumberAfter2000 % 97) == Rest));
            }
            return SqlBoolean.False;
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatRRNElectronicVersion([SqlParamFacet(MaxSize = 64)] SqlString aRRN)
        {
            if (aRRN.IsNull || !fnValidRRN(aRRN))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aRRN.Value);
            if (DigitStream.Length < LENGTH_RRN)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RRN, '0');
            }
            return new SqlString(DigitStream);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatRRNPaperVersion([SqlParamFacet(MaxSize = 64)] SqlString aRRN)
        {
            if (aRRN.IsNull || !fnValidRRN(aRRN))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aRRN.Value);
            if (DigitStream.Length < LENGTH_RRN)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RRN, '0');
            }
            return new SqlString(string.Format("{0}.{1}.{2} {3}-{4}",
                                               DigitStream.Substring(0, 2),
                                               DigitStream.Substring(2, 2),
                                               DigitStream.Substring(4, 2),
                                               DigitStream.Substring(6, 3),
                                               DigitStream.Substring(9, 2)));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlDateTime fnGetBirthDateFromRRN([SqlParamFacet(MaxSize = 64)] SqlString aRRN)
        {
            if (aRRN.IsNull)
            {
                return SqlDateTime.Null;
            }

            DateTime? BirthDate;
            int? Sexe;
            ParseRR(aRRN.Value, out BirthDate, out Sexe);
            if (BirthDate.HasValue)
            {
                return new SqlDateTime(BirthDate.Value);
            }
            else
            {
                return SqlDateTime.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlInt32 fnGetSexeFromRRN([SqlParamFacet(MaxSize = 64)] SqlString aRRN)
        {
            DateTime? BirthDate;
            int? Sexe;
            ParseRR(aRRN.Value, out BirthDate, out Sexe);
            if (Sexe.HasValue)
            {
                return new SqlInt32(Sexe.Value);
            }
            else
            {
                return SqlInt32.Null;
            }
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidRSZ([SqlParamFacet(MaxSize = 64)] SqlString aRSZ)
        {
            if (aRSZ.IsNull)
            {
                return SqlBoolean.Null;
            }

            bool Result = false;
            string DigitStream = GetDigitStream(aRSZ.Value);
            if (DigitStream.Length < LENGTH_RSZ)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RSZ, '0');
            }
            if (DigitStream.Length == LENGTH_RSZ)
            {
                long rest = 96 - ((long.Parse(DigitStream.Substring(0, 8)) * 100) % 97);

                if (rest == 0)
                {
                    rest = 97;
                }
                Result = rest == long.Parse(DigitStream.Substring(8, 2));
            }

            return new SqlBoolean(Result);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnTemporaryRSZ([SqlParamFacet(MaxSize = 64)] SqlString aRSZ)
        {
            if (aRSZ.IsNull || !fnValidRSZ(aRSZ))
            {
                return SqlBoolean.Null;
            }
            string RSZ = GetDigitStream(aRSZ.Value);
            return new SqlBoolean(RSZ.Length == LENGTH_RSZ && RSZ.StartsWith("5"));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatRSZElectronicVersion([SqlParamFacet(MaxSize = 64)] SqlString aRSZ)
        {
            if (aRSZ.IsNull || !fnValidRSZ(aRSZ))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aRSZ.Value);
            if (DigitStream.Length < LENGTH_RSZ)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RSZ, '0');
            }
            return new SqlString(DigitStream);
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        [return: SqlFacet(MaxSize = 64)]
        public static SqlString fnFormatRSZPaperVersion([SqlParamFacet(MaxSize = 64)] SqlString aRSZ)
        {
            if (aRSZ.IsNull || !fnValidRSZ(aRSZ))
            {
                return SqlString.Null;
            }

            string DigitStream = GetDigitStream(aRSZ.Value);
            if (DigitStream.Length < LENGTH_RSZ)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_RSZ, '0');
            }

            return new SqlString(string.Format("{0}.{1}",
                                               DigitStream.Substring(0, 8),
                                               DigitStream.Substring(8, 2)));
        }

        [SqlFunction(
            DataAccess = DataAccessKind.None,
            IsPrecise = true,
            IsDeterministic = true)]
        public static SqlBoolean fnValidVAT([SqlParamFacet(MaxSize = 64)] SqlString VATNbr)
        {
            if (VATNbr.IsNull)
            {
                return SqlBoolean.Null;
            }

            bool Result = false;
            string DigitStream = GetDigitStream(VATNbr.Value);
            if (DigitStream.Length < LENGTH_VAT)
            {
                DigitStream = DigitStream.PadLeft(LENGTH_VAT, '0');
            }
            if (DigitStream.Length == LENGTH_VAT)
            {
                long rest = 97 - (long.Parse(DigitStream.Substring(0, 7)) % 97);
                Result = (rest == long.Parse(DigitStream.Substring(7, 2)));
            }
            return new SqlBoolean(Result);
        }
    }
}