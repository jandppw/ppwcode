/*<license>
Copyright 2011 - $Date: 2008-11-06 15:27:53 +0100 (Thu, 06 Nov 2008) $ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

#region Using

using System;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Value.I.Time.Interval;

#endregion

namespace PPWCode.Value.Test_I.Time.Interval
{
    [TestClass]
    public class TimeIntervalRelationTest
    {
        #region Additional test attributes

        public static readonly DateTime[] Dts = new DateTime[]
        {
            new DateTime(2002, 4, 14),
            new DateTime(2004, 9, 30),
            new DateTime(2011, 12, 22),
            new DateTime(2043, 3, 3),
        };

        public static readonly ITimeInterval[] Tis = new ITimeInterval[]
        {
            null,
            new BeginEndTimeInterval(null, Dts[0]),
            new BeginEndTimeInterval(null, Dts[1]),
            new BeginEndTimeInterval(null, Dts[2]),
            new BeginEndTimeInterval(null, Dts[3]),
            new BeginEndTimeInterval(Dts[0], null),
            new BeginEndTimeInterval(Dts[0], Dts[0]),
            new BeginEndTimeInterval(Dts[0], Dts[1]),
            new BeginEndTimeInterval(Dts[0], Dts[2]),
            new BeginEndTimeInterval(Dts[0], Dts[3]),
            new BeginEndTimeInterval(Dts[1], null),
            new BeginEndTimeInterval(Dts[1], Dts[1]),
            new BeginEndTimeInterval(Dts[1], Dts[2]),
            new BeginEndTimeInterval(Dts[1], Dts[3]),
            new BeginEndTimeInterval(Dts[2], null),
            new BeginEndTimeInterval(Dts[2], Dts[2]),
            new BeginEndTimeInterval(Dts[2], Dts[3]),
            new BeginEndTimeInterval(Dts[3], null),
            new BeginEndTimeInterval(Dts[3], Dts[3]),
        };

        public static readonly bool FullTests = false;
        
        public static readonly int NrOfRandomValues = 300;

        public TimeIntervalRelation[] Subjects { get; set; }

        [TestInitialize]
        public void TestInitialize()
        {
            if (FullTests)
            {
                Subjects = TimeIntervalRelation.Values;
            }
            else
            {
                Subjects = new TimeIntervalRelation[NrOfRandomValues];
                Subjects[0] = TimeIntervalRelation.Empty;
                for (int i = 1; i < (TimeIntervalRelation.BasicRelations.Length + 1); i++)
                {
                    Subjects[i] = TimeIntervalRelation.BasicRelations[i - 1];
                }
                Subjects[14] = TimeIntervalRelation.Full;
                    Random r = new Random();
                for (int i = 15; i < NrOfRandomValues; i++)
                {
                    int index = r.Next(TimeIntervalRelation.NrOfRelations);
                    Subjects[i] = TimeIntervalRelation.Values[index];
                }
            }
        }

        [TestCleanup]
        public void TestCleanup()
        {
            Subjects = null;
        }

        #endregion

        [TestMethod]
        public void TestValues()
        {
            Assert.IsNotNull(TimeIntervalRelation.Values);
            Assert.AreEqual(TimeIntervalRelation.NrOfRelations, TimeIntervalRelation.Values.Length);
            for (int i = 0; i < TimeIntervalRelation.Values.Length; i++)
            {
                TimeIntervalRelation v = TimeIntervalRelation.Values[i];
                Assert.IsNotNull(v);
                Assert.AreEqual(i, v.GetHashCode());
                for (int j = i + 1; j < TimeIntervalRelation.Values.Length; j++)
                {
                    Assert.AreNotEqual(v, TimeIntervalRelation.Values[j]);
                }
            }
        }

        [TestMethod]
        public void ShowBitPatterns()
        {
            Console.WriteLine("ALL VALUES");
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                Console.WriteLine(tir + ": " + FullBitPattern(tir));
            }
            Console.WriteLine();
            Console.WriteLine("BASIC RELATIONS");
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.BasicRelations)
            {
                Console.WriteLine(tir + ": " + FullBitPattern(tir));
            }
            Console.WriteLine();
        }

        private static string FullBitPattern(TimeIntervalRelation ar)
        {
            return ((uint)ar.GetHashCode()).FullBitPattern(13);
        }

        [TestMethod]
        public void TestEquals()
        {
            bool result = false;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                result = tir1.Equals(null);
                result = tir1.Equals(new object());
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = tir1.Equals(tir2);
                }
            }
        }

        [TestMethod]
        public void TestOperatorEq()
        {
            bool result = false;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = tir1 == tir2;
                }
            }
        }

        [TestMethod]
        public void TestOperatorNe()
        {
            bool result = false;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = tir1 != tir2;
                }
            }
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            int result = 0;
            foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
            {
                result = tir1.GetHashCode();
            }
        }

        [TestMethod]
        public void TestOr0()
        {
            foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.BasicRelations)
            {
                foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.BasicRelations)
                {
                    TimeIntervalRelation result = TimeIntervalRelation.Or(tir1, tir2);
                }
            }
        }

        [TestMethod]
        public void TestOr1()
        {
            long total = (TimeIntervalRelation.NrOfRelations * (TimeIntervalRelation.NrOfRelations + 2)) + 1;
            Console.WriteLine("Starting test over " + total + " cases");
            long count = 0;
            TimeIntervalRelation result;
            result = TimeIntervalRelation.Or();
            count++;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                result = TimeIntervalRelation.Or(tir1);
                result = TimeIntervalRelation.Or(tir1, tir1);
                count += 2;
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = TimeIntervalRelation.Or(tir1, tir2);
                    count++;
                    float percentage = ((float)count / total) * 100;
                    if (count % 100000 == 0)
                    {
                        Console.WriteLine("  progress: " + count + " / " + total + " done (" + percentage + "%)");
                    }
                }
            }
        }

        [TestMethod]
        public void TestOr2()
        {
            TimeIntervalRelation result = TimeIntervalRelation.Or(TimeIntervalRelation.Values);
            Assert.AreEqual(TimeIntervalRelation.Full, result);
        }

        public static readonly int NrOfCombinationTestSubjects = 50;

        [TestMethod]
        public void TestOr()
        {
            TimeIntervalRelation[] localSubjects = new TimeIntervalRelation[NrOfCombinationTestSubjects];
            for (int i = 0; i < (NrOfCombinationTestSubjects - 1); i++)
            {
                localSubjects[i] = Subjects[i];
            }
            double total = Math.Pow(NrOfCombinationTestSubjects, 4);
            Console.WriteLine("Starting test over more than " + total + " cases");
            long count = 0;
            double totalForPercentage = total / 100;
            double percentage = 0.00;
            TimeIntervalRelation result;
            result = TimeIntervalRelation.Or();
            count++;
            foreach (TimeIntervalRelation tir1 in localSubjects)
            {
                result = TimeIntervalRelation.Or(tir1);
                result = TimeIntervalRelation.Or(tir1, tir1);
                result = TimeIntervalRelation.Or(tir1, tir1, tir1);
                result = TimeIntervalRelation.Or(tir1, tir1, tir1, tir1);
                count += 4;
                foreach (TimeIntervalRelation tir2 in localSubjects)
                {
                    result = TimeIntervalRelation.Or(tir1, tir2);
                    count++;
                    foreach (TimeIntervalRelation tir3 in localSubjects)
                    {
                        result = TimeIntervalRelation.Or(tir1, tir2, tir3);
                        count++;
                        foreach (TimeIntervalRelation tir4 in localSubjects)
                        {
                            result = TimeIntervalRelation.Or(tir1, tir2, tir3, tir4);
                            count++;
                            percentage = count / totalForPercentage;
                            if (count % 100000 == 0)
                            {
                                Console.WriteLine("  progress: " + count + " / " + total + " done (" + percentage + "%)");
                            }
                        }
                    }
                }
            }
        }

        [TestMethod]
        public void TestOrOperator()
        {
            TimeIntervalRelation[] localSubjects = new TimeIntervalRelation[NrOfCombinationTestSubjects];
            for (int i = 0; i < (NrOfCombinationTestSubjects - 1); i++)
            {
                localSubjects[i] = Subjects[i];
            }
            TimeIntervalRelation result;
            foreach (TimeIntervalRelation tir1 in localSubjects)
            {
                foreach (TimeIntervalRelation tir2 in localSubjects)
                {
                    result = tir1 | tir2;
                    foreach (TimeIntervalRelation tir3 in localSubjects)
                    {
                        result = tir1 | tir2 | tir3;
                        foreach (TimeIntervalRelation tir4 in localSubjects)
                        {
                            result = tir1 | tir2 | tir3 | tir4;
                        }
                    }
                }
            }
        }

        //[TestMethod]
        //public void TestAnd()
        //{
        //    TimeIntervalRelation result;
        //    result = TimeIntervalRelation.And();
        //    foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
        //    {
        //        result = TimeIntervalRelation.And(tir1);
        //        foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.Values)
        //        {
        //            result = TimeIntervalRelation.And(tir1, tir2);
        //            foreach (TimeIntervalRelation tir3 in TimeIntervalRelation.Values)
        //            {
        //                result = TimeIntervalRelation.And(tir1, tir2, tir3);
        //                foreach (TimeIntervalRelation tir4 in TimeIntervalRelation.Values)
        //                {
        //                    result = TimeIntervalRelation.And(tir1, tir2, tir3, tir4);
        //                }
        //            }
        //        }
        //    }
        //}

        //[TestMethod]
        //public void TestAndOperator()
        //{
        //    TimeIntervalRelation result;
        //    foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
        //    {
        //        foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.Values)
        //        {
        //            result = tir1 & tir2;
        //            foreach (TimeIntervalRelation tir3 in TimeIntervalRelation.Values)
        //            {
        //                result = tir1 & tir2 & tir3;
        //                foreach (TimeIntervalRelation tir4 in TimeIntervalRelation.Values)
        //                {
        //                    result = tir1 & tir2 & tir3 & tir4;
        //                }
        //            }
        //        }
        //    }
        //}

        [TestMethod]
        public void TestMin()
        {
            TimeIntervalRelation result;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = TimeIntervalRelation.Min(tir1, tir2);
                }
            }
        }

        [TestMethod]
        public void TestMinOperator()
        {
            TimeIntervalRelation result;
            foreach (TimeIntervalRelation tir1 in Subjects)
            {
                foreach (TimeIntervalRelation tir2 in Subjects)
                {
                    result = tir1 - tir2;
                }
            }
        }

        [TestMethod]
        public void TestLeastUncertainTimeIntervalRelation1()
        {
            TimeIntervalRelation result;
            foreach (ITimeInterval ti1 in Tis)
            {
                foreach (ITimeInterval ti2 in Tis)
                {
                    result = TimeIntervalRelation.LeastUncertainTimeIntervalRelation(ti1, ti2);
                    float uncertainty = result.Uncertainty;
                    Console.WriteLine("(" + ti1 + ", " + ti2 + ") =:= " + result + "(~" + uncertainty + ")");
                }
            }
        }

        [TestMethod]
        public void TestBasicRelationalOrdinal()
        {
            int result = 0;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.BasicRelations)
            {
                result = tir.BasicRelationalOrdinal;
                Console.WriteLine(result);
            }
        }

        [TestMethod]
        public void TestIsBasic()
        {
            bool result = false;
            int counter = 0;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                result = tir.IsBasic;
                if (result)
                {
                    counter++;
                }
            }
            Assert.AreEqual(13, counter);
        }

        [TestMethod]
        public void TestImpliedBy()
        {
            bool result = false;
            foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
            {
                foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.Values)
                {
                    result = tir1.ImpliedBy(tir2);
                }
            }
        }

        [TestMethod]
        public void TestImplies()
        {
            bool result = false;
            foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
            {
                foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.Values)
                {
                    result = tir1.Implies(tir2);
                }
            }
        }

        [TestMethod]
        public void TestToString()
        {
            string result = null;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                result = tir.ToString();
                Assert.IsFalse(string.IsNullOrEmpty(result));
                Console.WriteLine(FullBitPattern(tir) + ": " + result);
            }
        }

        [TestMethod]
        public void TestNrOfBasicRelations()
        {
            int result = 0;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                result = tir.NrOfBasicRelations;
                if (tir.IsBasic)
                {
                    Assert.AreEqual(1, result);
                }
                if (tir == TimeIntervalRelation.Full)
                {
                    Assert.AreEqual(13, result);
                }
                if (tir == TimeIntervalRelation.Empty)
                {
                    Assert.AreEqual(0, result);
                }
            }
        }

        [TestMethod]
        public void TestCalcNrOfBasicRelations()
        {
            int result = 0;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                result = TimeIntervalRelation.CalcNrOfBasicRelations(tir);
                if (tir.IsBasic)
                {
                    Assert.AreEqual(1, result);
                }
                if (tir == TimeIntervalRelation.Full)
                {
                    Assert.AreEqual(13, result);
                }
                if (tir == TimeIntervalRelation.Empty)
                {
                    Assert.AreEqual(0, result);
                }
            }
        }

        [TestMethod]
        public void TestUncertainty()
        {
            float result = 0;
            foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
            {
                result = tir.Uncertainty;
                if (tir.IsBasic)
                {
                    Assert.AreEqual(0, result);
                }
                if (tir == TimeIntervalRelation.Full)
                {
                    Assert.AreEqual(1, result);
                }
                Console.WriteLine(FullBitPattern(tir) + ": " + result + " " + tir.ToString());
            }
        }

        //[TestMethod]
        //public void TestComplement()
        //{
        //    TimeIntervalRelation result;
        //    foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
        //    {
        //        result = tir.Complement;
        //        //Console.WriteLine(FullBitPattern(tir) + " ### " + FullBitPattern(result));
        //    }
        //}

        //[TestMethod]
        //public void TestComplementOperator()
        //{
        //    TimeIntervalRelation result;
        //    foreach (TimeIntervalRelation tir in TimeIntervalRelation.Values)
        //    {
        //        result = ~tir;
        //        //Console.WriteLine(FullBitPattern(tir) + " ### " + FullBitPattern(result));
        //    }
        //}

        //[TestMethod]
        //public void TestAreComplementary()
        //{
        //    bool result;
        //    foreach (TimeIntervalRelation tir1 in TimeIntervalRelation.Values)
        //    {
        //        foreach (TimeIntervalRelation tir2 in TimeIntervalRelation.Values)
        //        {
        //            result = TimeIntervalRelation.AreComplementary(tir1, tir2);
        //        }
        //    }
        //}
    }
}