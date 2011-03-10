using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.ServiceProcess;

using PPWCode.Kit.Tasks.Server.API_I;

namespace PPWCode.Kit.Tasks.Server.NTServiceHost
{
    public partial class Service : ServiceBase
    {
        private sealed class HostDef
        {
            public ServiceHost Svc { get; set; }
            public Type SvcType { get; private set; }

            public HostDef(Type svcType)
            {
                Svc = null;
                SvcType = svcType;
            }
        }

        private static readonly List<HostDef> s_Hosts = new List<HostDef>
        {
            new HostDef(typeof(TasksDao))
        };

        public Service()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            foreach (HostDef host in s_Hosts)
            {
                if (host.Svc != null)
                {
                    host.Svc.Close();
                }
                host.Svc = new ServiceHost(host.SvcType);
                host.Svc.Open();
            }
        }

        protected override void OnStop()
        {
            foreach (HostDef host in s_Hosts.Where(host => host.Svc != null))
            {
                host.Svc.Close();
                host.Svc = null;
            }
        }
    }
}
