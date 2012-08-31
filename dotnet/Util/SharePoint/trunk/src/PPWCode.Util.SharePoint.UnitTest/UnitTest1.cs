#region Using

using System;
using System.Collections.Generic;
using System.IO;
using PPWCode.Util.SharePoint.I;

using NUnit.Framework;

#endregion

namespace PPWCode.Util.SharePoint.UnitTest
{
    [TestFixture]
    public class UnitTest1
    {
        private static ISharePointClient GetSharePointService(Uri uri)
        {
            ISharePointClient sharePointClient = new SharePointClient();
            sharePointClient.SharePointSiteUrl = uri.GetLeftPart(UriPartial.Authority);
            return sharePointClient;
        }

        [Test]
        public void TestUpload()
        {
            var sourceUri = new Uri(@"C:\Temp\temp.pdf");
            var targetUri = new Uri(@"http://Hoefnix/Test/a/");

            byte[] contents = File.ReadAllBytes(sourceUri.LocalPath);

            ISharePointClient sharePointClient = GetSharePointService(targetUri);

            //Create Sharepoint document
            var targetSpDoc = new SharePointDocument(contents);

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

        [Test]
        public void TestChangeName()
        {
            var startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    // sharePointClient.EnsureFolder("/PensioB/A-Test/test1/test2/test1/test2/test3/test1");
                    // sharePointClient.EnsureFolder("/PensioB/AA-Test/test1");
                    // sharePointClient.RenameAllOccurrencesOfFolder("/PensioB", "test1111", "test1");
                    // sharePointClient.RenameAllOccurrencesOfFolder("/PensioB", "A,L@xxx@9999999999", "A,L@xxx@775");
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

        [Test]
        public void TestFailingChangeFolderName()
        {
            var startUri = new Uri(@"http://hoefnix/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    //sharePointClient.CreateFolder("/PensioB", "A-Test/test1/test2/test1/test2/test3/test1");
                    //sharePointClient.CreateFolder("/PensioB/A-Test/test1/test2/test1/test2", "test1");
                    //sharePointClient.CreateFolder("/PensioB", "AA-Test/test1/test1");
                    //sharePointClient.CreateFolder("/PensioB/AA-Test/test1", "test1234");
                    //sharePointClient.CreateFolder("/PensioB", "ABB/TTT");
                    sharePointClient.RenameFolder("/PensioB/", "B,W@xx@13", "B,W@xx@1326");
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }

        [Test]
        public void TestNew()
        {
            var startUri = new Uri(@"http://hoefnix/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
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
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }

        [Test]
        public void TestDeleteFolder()
        {
            var startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    sharePointClient.DeleteFolder("/PensioB/bbbb", true);
                }
                catch (Exception e)
                {
                    Console.WriteLine(string.Format("Exception: {0}", e));
                }
            }
        }

        [Test]
        public void CheckExistenceAllOccurencesFolder()
        {
            var startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    int exists = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB/aaaa", " 1");
                    Console.WriteLine(exists);
                }
                catch (Exception e)
                {
                    Console.WriteLine(string.Format("Exception: {0}", e));
                }
            }
        }

        [Test]
        //[ExpectedException(typeof(Exception))]
        public void CheckExistenceFolderWithExactPath()
        {
            var startUri = new Uri(@"http://pensiob-sp2010/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    //sharePointClient.RenameFolder("/PensioB/B,W@xx@1326/Construo/Affiliation", "LetterPensionFiche 2010", "LetterPensionFiche 2009");
                    sharePointClient.RenameFolder("PensioB/", "B,W@xx@1325", "B,W@xx@1326");
                }
                catch (Exception e)
                {
                    Console.WriteLine(string.Format("Exception: {0}", e));
                }
            }
        }

        [Test]
        public void TestAll()
        {
            {
                var startUri = new Uri(@"http://hoefnix/Temp/");
                var sharePointClient = (SharePointClient) GetSharePointService(startUri);
                if (sharePointClient != null)
                {
                    try
                    {
                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg", true);
                        int getal1 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal1);
                        bool check1 = sharePointClient.CheckExistenceOfFolderWithExactPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg");
                        Console.WriteLine(check1);
                        sharePointClient.DeleteFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg", true);
                        int getal2 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal2);
                        sharePointClient.DeleteFolder("PensioB/bbbb/cccc/dddd/eeee/ffff", true);
                        int getal3 = sharePointClient.CountAllOccurencesOfFolderInPath("PensioB", "gggg");
                        Console.WriteLine(getal3);
                        bool check2 = sharePointClient.CheckExistenceOfFolderWithExactPath("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg");
                        Console.WriteLine(check2);
                        sharePointClient.CreateFolder("PensioB/bbbb/cccc/dddd/eeee/ffff/gggg/bbbb/gggg/", true);
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

        [Test]
        public void TestVersions()
        {
            var startUri = new Uri(@"http://hoefnix/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    IEnumerable<SharePointDocumentVersion> sharePointDocumentVersions =
                        sharePointClient.RetrieveAllVersionsFromUrl(
                            @"/PensioB/H,K@xx@2/Construo/Payments/DEATH%20-%202012-05-21/Beneficiaries/V,A@xx-xxx/LetterRequestingInformationOfBeneficiary.pdf");
                    foreach (SharePointDocumentVersion version in sharePointDocumentVersions)
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

        [Test]
        public void TestDownloadWithSpecificVersion()
        {
            var startUri = new Uri(@"http://hoefnix/PensioB/");
            var sharePointClient = (SharePointClient) GetSharePointService(startUri);
            if (sharePointClient != null)
            {
                try
                {
                    SharePointDocument sharePointDocument =
                        sharePointClient.DownloadSpecificVersion(
                            "/PensioB/H,K@xx@2/Construo/Payments/DEATH%20-%202012-05-21/Beneficiaries/V,A@xx-xx/LetterRequestingInformationOfBeneficiary.pdf",
                            "2.0");
                }
                catch (Exception ex)
                {
                    Console.WriteLine(string.Format("Exception: {0}", ex));
                }
            }
        }
    }
}