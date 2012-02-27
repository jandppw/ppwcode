﻿using System;
using System.Collections;
using System.Data.Common;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.OddsAndEnds.I.SpreadSheet;

namespace PPWCode.Util.OddsAndEnds.Test_I
{
    /// <summary>
    /// Summary description for SpreadSheet
    /// </summary>
    [TestClass]
    public class SpreadSheetTest
    {
        public SpreadSheetTest()
        {
           
        }
        public class ExcelRow
        {
            public long PaymentDossierID { get; set; }
            public long AffiliateSynergyId { get; set; }
        }
        private ExcelRow SpreadsheetRowResolver(DbDataReader dr)
        {
            if ((dr.IsDBNull(0) == false) && (dr.IsDBNull(1) == false))
            {
                return new ExcelRow
                {
                    PaymentDossierID = (long)dr.GetDouble(0),
                    AffiliateSynergyId = (long)dr.GetDouble(1)
                };
            }
            return null;
        }
        private List<string> m_ColumnNames = new List<string>
        {
            "PaymentDossierId", 
            "AffiliateSynergyId"
        };
        string fileName = @"C:\Development\Sempera\PPWCode.Util.OddsAndEnds\src\Test_I\FixGenerateStandardProposals.xlsx";
        
        
        [TestMethod]
        public void TestMethod1()
        {
            IList<ExcelRow> list = GenerateUtil.ReadSheet<ExcelRow>(fileName, "GSP", m_ColumnNames, SpreadsheetRowResolver);
            Assert.IsNotNull(list);


        }
  
    }
}
