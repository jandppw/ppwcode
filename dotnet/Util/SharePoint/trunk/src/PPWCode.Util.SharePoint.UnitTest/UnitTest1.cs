#region Using

using System;
using System.IO;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.SharePoint.I;

#endregion

namespace PPWCode.Util.SharePoint.UnitTest
{
    /// <summary>
    /// Summary description for UnitTest1
    /// </summary>
    [TestClass]
    public class UnitTest1
    {
        private static ISharePointClient GetSharePointService(Uri uri)
        {
            ISharePointClient sharePointClient = new SharePointClient();
            sharePointClient.SharePointSiteUrl = uri.GetLeftPart(UriPartial.Authority);
            return sharePointClient;
        }

        //[TestInitialize]
        //private void SetUp()
        //{
            
        //}

        //[TestCleanup]
        //private void TearDown()
        //{
            
        //}

        [TestMethod]
        public void TestUpload()
        {
            Uri sourceUri = new Uri(@"D:\ivan\icon.pdf");
            Uri targetUri = new Uri(@"http://pensiob-sp2010/PensioB/Test/");

            byte[] contents = File.ReadAllBytes(sourceUri.LocalPath);

            ISharePointClient sharePointClient = GetSharePointService(targetUri);

            //Create Sharepoint document
            SharePointDocument targetSpDoc = new SharePointDocument(contents);

            string fileName = Path.GetFileName(sourceUri.LocalPath);
            fileName = string.Format(
                "{0}{1}{2}",
                targetUri.AbsolutePath,
                targetUri.OriginalString.EndsWith("/")
                    ? string.Empty
                    : "/",
                fileName);

            sharePointClient.UploadDocument(fileName, targetSpDoc);
        }

        [TestMethod]
        public void TestChangeName()
        {
            Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    // sharePointClient.EnsureFolder("/PensioB/A-Test/test1/test2/test1/test2/test3/test1");
                    // sharePointClient.EnsureFolder("/PensioB/AA-Test/test1");
                    // sharePointClient.RenameAllOccurrencesOfFolder("/PensioB", "test1111", "test1");
                    // sharePointClient.RenameAllOccurrencesOfFolder("/PensioB", "ALAGOZLU,LUCIEN@81021034701@9999999999", "ALAGOZLU,LUCIEN@81021034701@775");
                    // sharePointClient.RenameAllOccurrencesOfFolder("/PensioB/AAA-Test", "atest1", "atest3");
                    // sharePointClient.EnsureFolder("/PensioB/AAAA/test1/test2/test3/test2");
                    //sharePointClient.RenameAllOccurrencesOfFolder("/PensioB/AAAA", "CCCC", "AAAA");
                    sharePointClient.RenameAllOccurrencesOfFolder("/PensioB/AAAA", "test9", "test1");
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));     
                }
              
            }
        }

        [TestMethod]
        public void TestFailingChangeFolderName()
        {
            Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    //sharePointClient.CreateFolder("/PensioB", "A-Test/test1/test2/test1/test2/test3/test1");
                    //sharePointClient.CreateFolder("/PensioB/A-Test/test1/test2/test1/test2", "test1");
                    //sharePointClient.CreateFolder("/PensioB", "AA-Test/test1/test1");
                    //sharePointClient.CreateFolder("/PensioB/AA-Test/test1", "test1234");
                    //sharePointClient.CreateFolder("/PensioB", "ABB/TTT");
                    sharePointClient.RenameFolder("/PensioB/", "BAEYENS,WIM@70051915702@13", "BAEYENS,WIM@70051915702@1326");
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }

        [TestMethod]
        public void TestNew()
        {
           Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
           SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
           if (sharePointClient != null)
           {
               try
               {
                   //sharePointClient.CreateFolder("PensioB/aaaa/bbbb/ /cccc/ ", true);
                   //sharePointClient.CreateFolder("PensioB/bbbb/", false);
                   //sharePointClient.CreateFolder("PensioB/bbbb/aaaa/", false);
                   //sharePointClient.CreateFolder("PensioB/bbbb/aaaa/cccc/dddd", true);
                   sharePointClient.CreateFolder("PensioB/aaaa/aaaa/cccc/dddd/ ", true);
               }
               catch(Exception ex)
               {
                   Console.WriteLine(string.Format("Exception: {0}", ex));   
               }
           }

        }
        [TestMethod]
        public void TestDeleteFolder()
        {
            Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    sharePointClient.DeleteFolder("/PensioB/bbbb/", true);
                }
                catch(Exception e)
                {
                    Console.WriteLine(string.Format("Exception: {0}", e));   
                }
            }
        }
        [TestMethod]
        public void CheckExistenceAllOccurencesFolder()
        {
             Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    int exists = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/bbbb/","aaaa");
                    Console.WriteLine(exists);
                }
                catch(Exception e)
                {
                    Console.WriteLine(string.Format("Exception: {0}", e)); 
                }
            }
        }
      [TestMethod]
      //[ExpectedException(typeof(Exception))]
      public void CheckExistenceFolderWithExactPath()
      {
          Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
          SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
          if (sharePointClient != null)
          {
              try
              {
                  //sharePointClient.RenameFolder("/PensioB/BAEYENS,WIM@70051915702@1326/Construo/Affiliation", "LetterPensionFiche 2010", "LetterPensionFiche 2009");
                  sharePointClient.RenameFolder("PensioB/", "BAEYENS,WIM@70051915702@1325", "BAEYENS,WIM@70051915702@1326");
              }
              catch (Exception e)
              {
                  Console.WriteLine(string.Format("Exception: {0}", e));
              }
          } 
      }
        [TestMethod]
        public void TestAll()
        {
            {
                Uri startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
                SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
                if (sharePointClient != null)
                {
                    try
                    {

                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg",true);
                        int getal1 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal1);
                        bool check1 = sharePointClient.CheckExistenceOfFolderWithExactPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg");
                        Console.WriteLine(check1);
                        sharePointClient.DeleteFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg",true);
                        int getal2 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal2);
                        sharePointClient.DeleteFolder("PensioB/bbbb/cccc/dddd/eeee/ffff",true);
                        int getal3 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal3);
                        bool check2 = sharePointClient.CheckExistenceOfFolderWithExactPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg");
                        Console.WriteLine(check2);
                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg/",true);
                        int getal4 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal4);
                        int getal5 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb", "gggg");
                        Assert.AreEqual(getal5, 5);
                        int getal6 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg", "gggg");
                        Console.WriteLine(getal6);

                        //sharePointClient.CreateFolder("/PensioB","GGGG/DDDD");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine(string.Format("Exception: {0}", ex));
                    }
                }

            } 
 
        }
    }
}