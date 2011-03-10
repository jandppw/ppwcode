using System.ServiceProcess;

using log4net.Config;

namespace PPWCode.Kit.Tasks.Server.NTServiceHost
{
    public static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        public static void Main()
        {
            XmlConfigurator.Configure();
            ServiceBase[] servicesToRun = new ServiceBase[] 
            { 
                new Service() 
            };
            ServiceBase.Run(servicesToRun);
        }
    }
}
