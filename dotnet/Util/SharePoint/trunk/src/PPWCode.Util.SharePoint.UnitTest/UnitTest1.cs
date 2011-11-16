using System;
using System.IO;
using System.Text;
using System.Collections.Generic;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

using PPWCode.Util.SharePoint.I;

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
    }
}
