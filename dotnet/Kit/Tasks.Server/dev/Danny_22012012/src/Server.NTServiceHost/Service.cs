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
using System.ServiceModel;
using System.ServiceProcess;

using log4net;

using PPWCode.Kit.Tasks.Server.API_I;
using PPWCode.Vernacular.Persistence.I.Dao.Wcf.Helpers.Hosting;

#endregion

namespace PPWCode.Kit.Tasks.Server.NTServiceHost
{
    public partial class Service : ServiceBase
    {
        #region fields

        private static readonly ILog s_Logger = LogManager.GetLogger(typeof(Service));

        private static readonly List<ServiceHost> s_ServiceHosts = new List<ServiceHost>
        {
            new ServiceHost<TasksDao>(),
        };

        #endregion

        #region Constructors

        public Service()
        {
            InitializeComponent();
        }

        #endregion

        #region Private static helpers

        private static void StartWcfServices()
        {
            foreach (ServiceHost serviceHost in s_ServiceHosts)
            {
                serviceHost.Open();
            }
        }

        private static void StopWcfServices()
        {
            foreach (ServiceHost serviceHost in s_ServiceHosts)
            {
                ICommunicationObject co = serviceHost;
                switch (co.State)
                {
                    case CommunicationState.Created:
                    case CommunicationState.Closing:
                    case CommunicationState.Closed:
                        break;
                    case CommunicationState.Opening:
                    case CommunicationState.Opened:
                        co.Close();
                        break;
                    case CommunicationState.Faulted:
                        co.Abort();
                        break;
                    default:
                        throw new ArgumentOutOfRangeException();
                }
            }
        }

        #endregion

        #region Overrided members

        protected override void OnStart(string[] args)
        {
            try
            {
                StartWcfServices();
            }
            catch (Exception e)
            {
                s_Logger.Fatal(e);
                throw;
            }
        }

        protected override void OnStop()
        {
            try
            {
                StopWcfServices();
            }
            catch (Exception e)
            {
                s_Logger.Fatal(e);
                throw;
            }
        }

        #endregion
    }
}