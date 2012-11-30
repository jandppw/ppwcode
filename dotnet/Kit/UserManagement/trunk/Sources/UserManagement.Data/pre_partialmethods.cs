namespace UserManagement.Data {

    using System;
    using System.Xml.Linq;

    partial class UserManagementDataContext {
        private const string UnknownError = "Unknown error: {0}";
        private readonly XDocument m_UserContext;

        public UserManagementDataContext(XDocument context) : base(global::UserManagement.Data.Properties.Settings.Default.userManagementConnectionString, mappingSource) {
            m_UserContext = context;
        }

