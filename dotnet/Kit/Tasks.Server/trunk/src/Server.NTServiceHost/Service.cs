/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#region Using

using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.ServiceProcess;

using PPWCode.Kit.Tasks.Server.API_I;

#endregion

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
            new HostDef(typeof(TasksDao)),
            new HostDef(typeof(TasksMergeDao))
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