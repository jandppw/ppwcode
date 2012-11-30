namespace UserManagement.Data {

    using System;
    using System.Xml.Linq;

    partial class UserManagementDataContext {
        private const string UnknownError = "Unknown error: {0}";
        private readonly XElement m_UserContext;

        public UserManagementDataContext(XElement context) : base(Properties.Settings.Default.userManagementConnectionString, mappingSource) {
            m_UserContext = context;
        }

