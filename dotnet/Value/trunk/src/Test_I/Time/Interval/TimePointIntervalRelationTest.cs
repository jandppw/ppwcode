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
    public class TimePointIntervalRelationTest
    {
        #region Additional test attributes

        [TestInitialize]
        public void TestInitialize()
        {
        }

        [TestCleanup]
        public void TestCleanup()
        {
        }

        #endregion

        [TestMethod]
        public void TestVALUES()
        {
            Assert.IsNotNull(TimePointIntervalRelation.Values);
            Assert.AreEqual(TimePointIntervalRelation.NrOfRelations, TimePointIntervalRelation.Values.Length);
            for (int i = 0; i < TimePointIntervalRelation.Values.Length; i++)
            {
                TimePointIntervalRelation v = TimePointIntervalRelation.Values[i];
                Assert.IsNotNull(v);
                Assert.AreEqual(i, v.GetHashCode());
                for (int j = i + 1; j < TimePointIntervalRelation.Values.Length; j++)
                {
                    Assert.AreNotEqual(v, TimePointIntervalRelation.Values[j]);
                }
            }
        }

        [TestMethod]
        public void ShowBitPatterns()
        {
            Console.WriteLine("ALL VALUES");
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                Console.WriteLine(tpir + ": " + FullBitPattern(tpir));
            }
            Console.WriteLine();
            Console.WriteLine("BASIC RELATIONS");
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.BasicRelations)
            {
                Console.WriteLine(tpir + ": " + FullBitPattern(tpir));
            }
            Console.WriteLine();
        }

        private static string FullBitPattern(TimePointIntervalRelation ar)
        {
            int bitpattern = ar.GetHashCode();
            string bitString = Convert.ToString(bitpattern, 2);
            while (bitString.Length < 5)
            {
                bitString = "0" + bitString;
            }
            return bitString;
        }

        [TestMethod]
        public void TestEquals()
        {
            bool result = false;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                result = tpir1.Equals(null);
                result = tpir1.Equals(new object());
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1.Equals(tpir2);
                }
            }
        }

        [TestMethod]
        public void TestOperatorEq()
        {
            bool result = false;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1 == tpir2;
                }
            }
        }

        [TestMethod]
        public void TestOperatorNe()
        {
            bool result = false;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1 != tpir2;
                }
            }
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            int result = 0;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                result = tpir1.GetHashCode();
            }
        }

        [TestMethod]
        public void TestOr()
        {
            TimePointIntervalRelation result;
            result = TimePointIntervalRelation.Or();
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                result = TimePointIntervalRelation.Or(tpir1);
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = TimePointIntervalRelation.Or(tpir1, tpir2);
                    foreach (TimePointIntervalRelation tpir3 in TimePointIntervalRelation.Values)
                    {
                        result = TimePointIntervalRelation.Or(tpir1, tpir2, tpir3);
                        foreach (TimePointIntervalRelation tpir4 in TimePointIntervalRelation.Values)
                        {
                            result = TimePointIntervalRelation.Or(tpir1, tpir2, tpir3, tpir4);
                        }
                    }
                }
            }
        }

        [TestMethod]
        public void TestOrOperator()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1 | tpir2;
                    foreach (TimePointIntervalRelation tpir3 in TimePointIntervalRelation.Values)
                    {
                        result = tpir1 | tpir2 | tpir3;
                        foreach (TimePointIntervalRelation tpir4 in TimePointIntervalRelation.Values)
                        {
                            result = tpir1 | tpir2 | tpir3 | tpir4;
                        }
                    }
                }
            }
        }

        [TestMethod]
        public void TestAnd()
        {
            TimePointIntervalRelation result;
            result = TimePointIntervalRelation.And();
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                result = TimePointIntervalRelation.And(tpir1);
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = TimePointIntervalRelation.And(tpir1, tpir2);
                    foreach (TimePointIntervalRelation tpir3 in TimePointIntervalRelation.Values)
                    {
                        result = TimePointIntervalRelation.And(tpir1, tpir2, tpir3);
                        foreach (TimePointIntervalRelation tpir4 in TimePointIntervalRelation.Values)
                        {
                            result = TimePointIntervalRelation.And(tpir1, tpir2, tpir3, tpir4);
                        }
                    }
                }
            }
        }

        [TestMethod]
        public void TestAndOperator()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1 & tpir2;
                    foreach (TimePointIntervalRelation tpir3 in TimePointIntervalRelation.Values)
                    {
                        result = tpir1 & tpir2 & tpir3;
                        foreach (TimePointIntervalRelation tpir4 in TimePointIntervalRelation.Values)
                        {
                            result = tpir1 & tpir2 & tpir3 & tpir4;
                        }
                    }
                }
            }
        }

        [TestMethod]
        public void TestMin()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = TimePointIntervalRelation.Min(tpir1, tpir2);
                }
            }
        }

        [TestMethod]
        public void TestMinOperator()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1 - tpir2;
                }
            }
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation1A()
        {
            DateTime? t = null;
            ITimeInterval i = new BeginEndTimeInterval(DateTime.Now, null);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Full, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation1B()
        {
            DateTime? t = null;
            ITimeInterval i = new BeginEndTimeInterval(null, DateTime.Now);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Full, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation1C()
        {
            DateTime? t = null;
            DateTime now = DateTime.Now;
            ITimeInterval i = new BeginEndTimeInterval(now, now);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Full, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation2A()
        {
            DateTime? t = DateTime.Now;
            ITimeInterval i = new BeginEndTimeInterval(t, null);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Begins, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation2B()
        {
            DateTime? t = DateTime.Now;
            ITimeInterval i = new BeginEndTimeInterval(null, t);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Ends, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation3A()
        {
            DateTime? t = new DateTime(1995, 6, 23);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1996, 7, 4), null);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Before, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation3B()
        {
            DateTime? t = new DateTime(1995, 6, 23);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1996, 7, 4), new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Before, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation3C()
        {
            DateTime? t = new DateTime(1995, 6, 23);
            ITimeInterval i = new BeginEndTimeInterval(null, new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.BeforeEnd, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation4()
        {
            DateTime? t = new DateTime(1995, 6, 23);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1991, 11, 17), new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.In, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation5()
        {
            DateTime? t = new DateTime(2010, 3, 12);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1991, 11, 17), new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.Ends, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation6A()
        {
            DateTime? t = new DateTime(2011, 3, 12);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1991, 11, 17), new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.After, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation6B()
        {
            DateTime? t = new DateTime(2011, 3, 12);
            ITimeInterval i = new BeginEndTimeInterval(null, new DateTime(2010, 3, 12));
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.After, result);
        }

        [TestMethod]
        public void TestLeastUncertainTimePointIntervalRelation6C()
        {
            DateTime? t = new DateTime(2011, 3, 12);
            ITimeInterval i = new BeginEndTimeInterval(new DateTime(1991, 11, 17), null);
            TimePointIntervalRelation result = TimePointIntervalRelation.LeastUncertainTimePointIntervalRelation(t, i);
            //Console.WriteLine("(" + t + ", " + i + ") =:= " + result);
            Assert.AreEqual(TimePointIntervalRelation.AfterBegin, result);
        }

        [TestMethod]
        public void TestBasicRelationalOrdinal()
        {
            int result = 0;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.BasicRelations)
            {
                result = tpir.BasicRelationalOrdinal;
                //Console.WriteLine(result);
            }
        }

        [TestMethod]
        public void TestIsBasic()
        {
            bool result = false;
            int counter = 0;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = tpir.IsBasic;
                if (result)
                {
                    counter++;
                }
            }
            Assert.AreEqual(5, counter);
        }

        [TestMethod]
        public void TestImpliedBy()
        {
            bool result = false;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1.ImpliedBy(tpir2);
                }
            }
        }

        [TestMethod]
        public void TestImplies()
        {
            bool result = false;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = tpir1.Implies(tpir2);
                }
            }
        }

        [TestMethod]
        public void TestToString()
        {
            string result = null;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = tpir.ToString();
                Assert.IsFalse(string.IsNullOrEmpty(result));
                Console.WriteLine(FullBitPattern(tpir) + ": " + result);
            }
        }

        [TestMethod]
        public void TestNrOfBasicRelations()
        {
            int result = 0;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = tpir.NrOfBasicRelations;
                if (tpir.IsBasic)
                {
                    Assert.AreEqual(1, result);
                }
                if (tpir == TimePointIntervalRelation.Full)
                {
                    Assert.AreEqual(5, result);
                }
                if (tpir == TimePointIntervalRelation.Empty)
                {
                    Assert.AreEqual(0, result);
                }
            }
        }

        [TestMethod]
        public void TestCalcNrOfBasicRelations()
        {
            int result = 0;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = TimePointIntervalRelation.CalcNrOfBasicRelations(tpir);
                if (tpir.IsBasic)
                {
                    Assert.AreEqual(1, result);
                }
                if (tpir == TimePointIntervalRelation.Full)
                {
                    Assert.AreEqual(5, result);
                }
                if (tpir == TimePointIntervalRelation.Empty)
                {
                    Assert.AreEqual(0, result);
                }
            }
        }

        [TestMethod]
        public void TestUncertainty()
        {
            float result = 0;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = tpir.Uncertainty;
                if (tpir.IsBasic)
                {
                    Assert.AreEqual(0, result);
                }
                if (tpir == TimePointIntervalRelation.Full)
                {
                    Assert.AreEqual(1, result);
                }
                // Console.WriteLine(FullBitPattern(tpir) + ": " + result + tpir.ToString());
            }
        }

        [TestMethod]
        public void TestComplement()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = tpir.Complement;
                //Console.WriteLine(FullBitPattern(tpir) + " ### " + FullBitPattern(result));
            }
        }

        [TestMethod]
        public void TestComplementOperator()
        {
            TimePointIntervalRelation result;
            foreach (TimePointIntervalRelation tpir in TimePointIntervalRelation.Values)
            {
                result = ~tpir;
                //Console.WriteLine(FullBitPattern(tpir) + " ### " + FullBitPattern(result));
            }
        }

        [TestMethod]
        public void TestAreComplementary()
        {
            bool result;
            foreach (TimePointIntervalRelation tpir1 in TimePointIntervalRelation.Values)
            {
                foreach (TimePointIntervalRelation tpir2 in TimePointIntervalRelation.Values)
                {
                    result = TimePointIntervalRelation.AreComplementary(tpir1, tpir2);
                }
            }
        }
    }
}