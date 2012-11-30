using System;
using System.Diagnostics;

namespace FrontEnd.Code
{
	public static class ErrorLogger
	{
	    private const string ErrorFormat = "Date: {0}; Modul Name: {1}; Error Level: {2}; Message: {3}";

		public enum ErrorLevel
		{
			INFORMATION,
			TRACE,
			WARRNING,
			EXCEPTION
		}

		public static void WriteToLog(string moduleName, ErrorLevel lvl, string error)
		{
			try
			{
			    EventLog myLog = new EventLog
			    {
			        Source = "IISADMIN"
			    };
			    if (lvl == ErrorLevel.EXCEPTION)
					myLog.WriteEntry(string.Format(ErrorFormat, DateTime.Now.ToString(), moduleName, GetErrorCodeDescription(lvl), error), EventLogEntryType.Error);
				if (lvl == ErrorLevel.INFORMATION)
					myLog.WriteEntry(string.Format(ErrorFormat, DateTime.Now.ToString(), moduleName, GetErrorCodeDescription(lvl), error), EventLogEntryType.Information);
			}
			catch
			{
			    // NOP
			}
		}

		public static void WriteToLog(string origin, Exception ex)
		{
			try
			{
				string message = ex.Message + " \n" + ex.StackTrace;
				if (ex.InnerException != null) message = ex.Message + " \n" + ex.StackTrace + " \nInnerException: " + ex.InnerException.Message + " \n InnerException Stack Trace " + ex.InnerException.StackTrace;
				WriteToLog(origin, ErrorLevel.EXCEPTION, message);
			}
			catch
			{
			    // NOP
			}
		}

		public static string GetErrorCodeDescription(ErrorLevel errorLvl)
		{
			switch (errorLvl)
			{
				case ErrorLevel.TRACE:
					return "Trace";
				case ErrorLevel.WARRNING:
					return "Warrning";
				case ErrorLevel.EXCEPTION:
					return "Exception";
				default:
					return "Information";
			}
		}
	}
}
