using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;
using System.Xml;
using System.IO;

public partial class UserDefinedFunctions
{

	[Microsoft.SqlServer.Server.SqlFunction]
	public static SqlXml fnStr2Msg(int lcid, string message)
	{
		//Create document in memory
		using(MemoryStream ms = new MemoryStream())
		{
			using(XmlWriter xw = XmlWriter.Create(ms))
			{
				xw.WriteStartDocument();
				xw.WriteStartElement("messages");
				xw.WriteStartElement("message");
				xw.WriteStartAttribute("lcid");
				xw.WriteValue(lcid);
				xw.WriteEndAttribute();
				xw.WriteValue(message);
				xw.WriteEndElement();
				xw.WriteEndElement();
				xw.WriteEndDocument();
			}

			ms.Position = 0;

			return new SqlXml(XmlReader.Create(ms));
		}
	}

};

