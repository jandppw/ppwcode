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

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Value.I.Time.Interval;

#endregion

namespace PPWCode.Value.Test_I.Time.Interval
{
    [TestClass]
    public class AbstractTimeIntervalTest
    {
        private class StubAbstractTimeInterval : AbstractTimeInterval
        {
            private readonly DateTime? m_Begin;
            private readonly DateTime? m_End;
            private readonly TimeSpan? m_Duration;

            public StubAbstractTimeInterval(DateTime? begin, DateTime? end, TimeSpan? duration)
            {
                m_Begin = begin;
                m_End = end;
                m_Duration = duration;
            }

            public override DateTime? Begin
            {
                get
                {
                    return m_Begin;
                }
            }

            public override DateTime? End
            {
                get
                {
                    return m_End;
                }
            }

            public override TimeSpan? Duration
            {
                get
                {
                    return m_Duration;
                }
            }

            public override ITimeInterval Determinate(DateTime? stubBegin, DateTime? stubEnd)
            {
                return null;
            }
        }

        #region Additional test attributes

        //
        // You can use the following additional attributes as you write your tests:
        //
        // Use ClassInitialize to run code before running the first test in the class
        // [ClassInitialize()]
        // public static void MyClassInitialize(TestContext testContext) { }
        //
        // Use ClassCleanup to run code after all tests in a class have run
        // [ClassCleanup()]
        // public static void MyClassCleanup() { }
        //

        protected List<AbstractTimeInterval> MSubjects;

        public IList<AbstractTimeInterval> Subjects
        {
            get
            {
                return MSubjects;
            }
        }

        [TestInitialize()]
        public void MyTestInitialize()
        {
            List<AbstractTimeInterval> s = new List<AbstractTimeInterval>();
            DateTime now = DateTime.Now;
            AbstractTimeInterval subject = new StubAbstractTimeInterval(now, null, null);
            s.Add(subject);
            subject = new StubAbstractTimeInterval(null, now, null);
            s.Add(subject);
            subject = new StubAbstractTimeInterval(now, null, new TimeSpan());
            s.Add(subject);
            subject = new StubAbstractTimeInterval(now, now, null);
            s.Add(subject);
            subject = new StubAbstractTimeInterval(now, now, new TimeSpan());
            s.Add(subject);
            DateTime b = new DateTime(2000, 1, 1);
            DateTime e = new DateTime(2122, 12, 31);
            subject = new StubAbstractTimeInterval(b, e, null);
            s.Add(subject);
            subject = new StubAbstractTimeInterval(b, e, e - b);
            s.Add(subject);
            MSubjects = s;
        }

        [TestCleanup()]
        public void MyTestCleanup()
        {
            MSubjects = null;
        }

        #endregion

        public
            void TestEqualsObject(AbstractTimeInterval subject, object other)
        {
            bool result = subject.Equals(other);
            //    System.out.println(subject + ".Equals(" + other + ") == " + result);
            //if (other is ITimeInterval) {
            //assertInvariants((AbstractTimeInterval)other);
            //}
        }

        [TestMethod]
        public void TestEqualsObject()
        {
            foreach (AbstractTimeInterval subject in Subjects)
            {
                TestEqualsObject(subject, null);
                TestEqualsObject(subject, new object());
                foreach (AbstractTimeInterval other in Subjects)
                {
                    TestEqualsObject(subject, other);
                }
            }
        }

        [TestMethod]
        public
            void TestHashCode()
        {
            int result = 0;
            foreach (AbstractTimeInterval subject in Subjects)
            {
                result = subject.GetHashCode();
            }
        }

        [TestMethod]
        public void TestToString()
        {
            string result = null;
            foreach (AbstractTimeInterval subject in Subjects)
            {
                result = subject.ToString();
                Console.WriteLine(result);
            }
        }

        [TestMethod]
        public void TestDeterminateBeginDate()
        {
            DateTime? result = null;
            foreach (AbstractTimeInterval subject in Subjects)
            {
                result = subject.DeterminateBegin(null);
                result = subject.DeterminateBegin(new DateTime());
            }
        }

        [TestMethod]
        public void TestDeterminateEndDate()
        {
            DateTime? result = null;
            foreach (AbstractTimeInterval subject in Subjects)
            {
                result = subject.DeterminateEnd(null);
                result = subject.DeterminateEnd(new DateTime());
            }
        }
    }
}