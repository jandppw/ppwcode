#region Using

using System;
using System.Collections;
using System.IO;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.SharePoint.I;

using Microsoft.SharePoint.Client;

using File = Microsoft.SharePoint.Client.File;

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
            Uri sourceUri = new Uri(@"C:\Users\kristel.bogaerts\Documents\Allerlei\Probeer.pdf");
            Uri targetUri = new Uri(@"http://Hoefnix/PensioB/a/");

            byte[] contents = System.IO.File.ReadAllBytes(sourceUri.LocalPath);

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

            string nr = sharePointClient.UploadDocumentReceiveVersion(fileName, targetSpDoc);
            Console.WriteLine(nr);
            
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
                   sharePointClient.CreateFolder("PensioB/bbbb/bbbb/", true);
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
                    sharePointClient.DeleteFolder("/PensioB/bbbb", true);
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
                    int exists = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/aaaa"," 1");
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
                Uri startUri = new Uri(@"http://hoefnix/PensioB/");
                SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
                if (sharePointClient != null)
                {
                    try
                    {

                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg", true);
                        int getal1 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal1);
                        bool check1 = sharePointClient.CheckExistenceOfFolderWithExactPath("/PensioB/HEYVAERT,KOEN@79110600301@2/Construo/Payments/DEATH%20-%202012-05-21/Beneficiaries/VANDEGINSTE,AN@316685-86030620287/LetterRequestingInformationOfBeneficiary.pdf");
                        Console.WriteLine(check1);
                        sharePointClient.DeleteFolder("PensioB/a", false);
                        int getal2 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal2);
                        sharePointClient.DeleteFolder("PensioB/bbbb/cccc/dddd/eeee/ffff", true);
                        int getal3 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal3);
                        bool check2 = sharePointClient.CheckExistenceOfFolderWithExactPath("PensioB/a");
                        Console.WriteLine(check2);
                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg/", true);
                        int getal4 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal4);
                        int getal5 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb", "gggg");
                        Assert.AreEqual(getal5, 5);
                        int getal6 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg", "gggg");
                        Console.WriteLine(getal6);

                        sharePointClient.CreateFolder("/PensioB", "GGGG/DDDD");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine(string.Format("Exception: {0}", ex));
                    }
                }

            } 
 
        }
        [TestMethod]
        public void TestVersions()
        {
            Uri startUri = new Uri(@"http://hoefnix/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
               try
                {
                    System.Collections.Generic.IEnumerable<SharePointDocumentVersion> sharePointDocumentVersions = sharePointClient.RetrieveAllVersionsFromUrl(@"/PensioB/HEYVAERT,KOEN@79110600301@2/Construo/Payments/DEATH%20-%202012-05-21/Beneficiaries/VANDEGINSTE,AN@316685-86030620287/LetterRequestingInformationOfBeneficiary.pdf");
                    foreach (var version in sharePointDocumentVersions)
                    {
                        Console.WriteLine(version.Version);
                        Console.WriteLine(version.CreationDate);
                        Console.WriteLine(version.Url);
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }
        [TestMethod]
        public void TestDownloadWithSpecificVersion()
        {
            Uri startUri = new Uri(@"http://hoefnix/PensioB/");
            SharePointClient sharePointClient = (SharePointClient)GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    SharePointDocument sharePointDocument = sharePointClient.DownloadSpecificVersion("/PensioB/HEYVAERT,KOEN@79110600301@2/Construo/Payments/DEATH%20-%202012-05-21/Beneficiaries/VANDEGINSTE,AN@316685-86030620287/LetterRequestingInformationOfBeneficiary.pdf", "2.0");
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }
    }
}