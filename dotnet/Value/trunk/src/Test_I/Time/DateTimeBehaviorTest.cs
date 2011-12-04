using System;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace PPWCode.Value.Test_I.Time
{
    /// <summary>
    /// Summary description for DateTimeBehaviorTest
    /// </summary>
    [TestClass]
    public class DateTimeBehaviorTest
    {
        [TestMethod]
        public void TestRelations1()
        {
            DateTime dt1 = new DateTime(2004, 7, 26);
            DateTime dt2 = new DateTime(2007, 3, 6);
            DateTime dt1Too = dt1;
            Assert.IsTrue(dt1 < dt2);
            Assert.IsTrue(dt1 <= dt2);
            Assert.IsTrue(dt1 != dt2);
            Assert.IsFalse(dt1 > dt2);
            Assert.IsFalse(dt1 >= dt2);
            Assert.IsFalse(dt1 == dt2);
            Assert.IsFalse(dt1 < dt1Too);
            Assert.IsTrue(dt1 <= dt1Too);
            Assert.IsFalse(dt1 != dt1Too);
            Assert.IsFalse(dt1 > dt1Too);
            Assert.IsTrue(dt1 >= dt1Too);
            Assert.IsTrue(dt1 == dt1Too);
        }

        [TestMethod]
        public void TestRelations2()
        {
            DateTime? dt1 = new DateTime(2004, 7, 26);
            DateTime? dt2 = new DateTime(2007, 3, 6);
            TestRelations(dt1, dt2);
        }

        [TestMethod]
        public void TestRelations3()
        {
            DateTime? dt2 = new DateTime(2007, 3, 6);
            TestRelations(null, dt2);
        }

        [TestMethod]
        public void TestRelations4()
        {
            DateTime? dt1 = new DateTime(2004, 7, 26);
            TestRelations(dt1, null);
        }

        /// <summary>
        /// DateTime? and order > is not total
        /// </summary>
        [TestMethod]
        public void TestRelations5()
        {
            DateTime? dt1 = new DateTime(2004, 7, 26);
            DateTime? dt2 = null;
            Assert.IsFalse(dt1 < dt2);
            Assert.IsFalse(dt1 <= dt2);
            Assert.IsFalse(dt1 == dt2);
            Assert.IsFalse(dt1 > dt2);
            Assert.IsFalse(dt1 >= dt2);
        }

        public void TestRelations(DateTime? dt1, DateTime? dt2)
        {
            DateTime? dt1Too = dt1;
            Assert.AreEqual(dt1 < dt2, dt1 != null && dt2 != null && dt1.Value < dt2.Value);
            Assert.AreEqual(dt1 <= dt2, dt1 != null && dt2 != null && dt1.Value <= dt2.Value);
            Assert.IsTrue(dt1 != dt2);
            Assert.AreEqual(dt1 > dt2, dt1 != null && dt2 != null && dt1.Value > dt2.Value);
            Assert.AreEqual(dt1 >= dt2, dt1 != null && dt2 != null && dt1.Value >= dt2.Value);
            Assert.IsFalse(dt1 == dt2);
            Assert.IsFalse(dt1 < dt1Too);
            Assert.AreEqual(dt1 <= dt1Too, dt1 != null);
            Assert.IsFalse(dt1 != dt1Too);
            Assert.IsFalse(dt1 > dt1Too);
            Assert.AreEqual(dt1 >= dt1Too, dt1 != null);
            Assert.IsTrue(dt1 == dt1Too);
        }
    }
}
