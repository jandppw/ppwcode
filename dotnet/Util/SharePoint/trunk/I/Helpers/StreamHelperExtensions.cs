#region Using

using System.IO;

#endregion

namespace PPWCode.Util.SharePoint.I.Helpers
{
    internal static class StreamHelperExtensions
    {
        internal static byte[] ConvertToByteArray(this Stream stream)
        {
            using (MemoryStream memoryStream = new MemoryStream())
            {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    memoryStream.Write(buffer, 0, bytesRead);
                }
                return memoryStream.ToArray();
            }
        }
    }
}