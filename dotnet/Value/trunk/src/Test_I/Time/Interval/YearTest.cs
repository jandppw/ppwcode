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
using System.Collections.Generic;
using System.Linq;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Value.I.Time.Interval;

#endregion

namespace PPWCode.Value.Test_I.Time.Interval
{
    [TestClass]
    public class YearTest : AbstractTimeIntervalTest
    {
        #region Additional test attributes

        private int[] m_IntSubjects;


        public IList<Year> YearSubjects
        {
            get
            {
                return Subjects.Cast<Year>().ToList();
            }
        }

        private DateTime?[] m_Dates;

        [TestInitialize]
        public override void TestInitialize()
        {
            InitSubjects();

            m_IntSubjects = new int[]
                {
                    Int32.MinValue, -234544, -2346, -123, -4,
                    0,
                    1, 6, 33, 346, 1256, 1856, 1914, 1999, 2000, 2001, 2011, 2012,
                    2013, 2145, 2234, 43566, 434534343, Int32.MaxValue
                };

            foreach (int i in m_IntSubjects)
            {
                AddSubject(new Year(i));
            }

            m_Dates = new DateTime?[]
            {
                new DateTime(Int32.MinValue, 4, 6),
                new DateTime(-345, 2, 12),
                new DateTime(0, 5, 6), 
                new DateTime(1999, 12, 31),
                DateTime.Now,
                DateTime.UtcNow,
                new DateTime(2011, 1, 1, 0, 0, 0, 0), 
                new DateTime(2011, 12, 31, 23, 59, 59, 999), 
                new DateTime(2011, 2, 19, 14, 35, 34, 345, DateTimeKind.Utc), 
                new DateTime(2011, 2, 28), 
                new DateTime(2012, 1, 1, 0, 0, 0, 0), 
                new DateTime(2012, 12, 31, 23, 59, 59, 999), 
                new DateTime(2012, 2, 19, 14, 35, 34, 345, DateTimeKind.Utc), 
                new DateTime(2012, 2, 28), 
                new DateTime(2012, 2, 29), 
                new DateTime(Int32.MaxValue, 12, 31, 23, 59, 59, 999)
            };
        }

        [TestCleanup]
        public override void TestCleanup()
        {
            base.TestCleanup();
            m_IntSubjects = null;
            m_Dates = null;
        }

        #endregion

        [TestMethod]
        public void TestDeterminate()
        {
            ITimeInterval result = null;
            foreach (BeginEndTimeInterval subject in Subjects)
            {
                try
                {
                    result = subject.Determinate(null, null);
                }
                catch (IllegalTimeIntervalException)
                {
                    // NOP
                }
                foreach (DateTime? dt1 in m_Dates)
                {
                    try
                    {
                        result = subject.Determinate(dt1, null);
                    }
                    catch (IllegalTimeIntervalException)
                    {
                        // NOP
                    }
                    try
                    {
                        result = subject.Determinate(null, dt1);
                    }
                    catch (IllegalTimeIntervalException)
                    {
                        // NOP
                    }
                    foreach (DateTime? dt2 in m_Dates)
                    {
                        try
                        {
                            result = subject.Determinate(dt1, dt2);
                        }
                        catch (IllegalTimeIntervalException)
                        {
                            // NOP
                        }
                    }                    
                }                
            }
        }

        [TestMethod]
        public void TestConstructor_Int()
        {
            foreach (int i in m_IntSubjects)
            {
                Year result = new Year(i);
            }
        }
    }
}