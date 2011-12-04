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
    public class BeginEndTimeIntervalTest : AbstractBeginEndTimeIntervalTest
    {
        #region Additional test attributes

        public IList<BeginEndTimeInterval> BeginEndSubjects
        {
            get
            {
                return Subjects.Cast<BeginEndTimeInterval>().ToList();
            }
        }

        private DateTime?[] m_Dates;

        [TestInitialize]
        public override void TestInitialize()
        {
            DateTime now = DateTime.Now;

            InitSubjects();
            AbstractBeginEndTimeInterval subject = new BeginEndTimeInterval(now, null);
            AddSubject(subject);
            subject = new BeginEndTimeInterval(null, now);
            AddSubject(subject);
            subject = new BeginEndTimeInterval(now, now);
            AddSubject(subject);
            DateTime b = new DateTime(2000, 1, 1);
            DateTime e = new DateTime(2122, 12, 31);
            subject = new BeginEndTimeInterval(b, e);
            AddSubject(subject);

            DateTime past = new DateTime(1995, 4, 24);
            DateTime future = new DateTime(2223, 5, 13);
            m_Dates = new DateTime?[]
            {
                null, past, now, future
            };
        }

        [TestCleanup]
        public override void TestCleanup()
        {
            base.TestCleanup();
            m_Dates = null;
        }

        #endregion

        [TestMethod]
        public void TestDeterminateBeginEndTimeInterval()
        {
            BeginEndTimeInterval result = null;
            foreach (BeginEndTimeInterval subject in Subjects)
            {
                foreach (DateTime? dt1 in m_Dates)
                {
                    foreach (DateTime? dt2 in m_Dates)
                    {
                        try
                        {
                            result = subject.DeterminateBeginEndTimeInterval(dt1, dt2);
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
        public void TestDeterminate()
        {
            ITimeInterval result = null;
            foreach (BeginEndTimeInterval subject in Subjects)
            {
                foreach (DateTime? dt1 in m_Dates)
                {
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
        public override void TestConstructor()
        {
            foreach (DateTime? d1 in m_Dates)
            {
                foreach (DateTime? d2 in m_Dates)
                {
                    try
                    {
                        AbstractBeginEndTimeInterval subject = new BeginEndTimeInterval(d1, d2);
                    }
                    catch (IllegalTimeIntervalException)
                    {
                        // NOP
                    }
                }
            }
        }
    }
}