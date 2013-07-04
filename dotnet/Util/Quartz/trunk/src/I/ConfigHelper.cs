using System;
using System.Configuration;
using System.Globalization;

namespace PPWCode.Util.Quartz
{
    public static class ConfigHelper
    {
        public static T GetAppSetting<T>(string key, T defaultValue)
           where T : IConvertible
        {
            T result;
            try
            {
                result = ConfigurationManager.AppSettings.Get(key) != null
                             ? (T)Convert.ChangeType(ConfigurationManager.AppSettings[key], typeof(T), CultureInfo.InvariantCulture)
                             : defaultValue;
            }
            catch
            {
                result = defaultValue;
            }
            return result;
        }

        public static string GetConnectionString(string key)
        {
            ConnectionStringSettings connectionString = ConfigurationManager.ConnectionStrings[key];
            return connectionString == null ? null : connectionString.ConnectionString;
        }
    }
}
