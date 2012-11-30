namespace UserManagement.Data {

    using System;
    using System.Xml.Linq;

    partial class UserManagementDataContext {
        private const string UnknownError = "Unknown error: {0}";
        private readonly XElement m_UserContext;

        public UserManagementDataContext(XElement context) : base(Properties.Settings.Default.userManagementConnectionString, mappingSource) {
            m_UserContext = context;
        }

        partial void DeleteConstraintMessage(ConstraintMessage instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_ConstraintMessage_D(
                    m_UserContext,
                    instance.ConstraintName,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertConstraintMessage(ConstraintMessage instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_ConstraintMessage_I(
                    m_UserContext,
                    instance.ConstraintName,
                    instance.ConstraintType,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateConstraintMessage(ConstraintMessage instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_ConstraintMessage_U(
                    m_UserContext,
                    instance.ConstraintName,
                    instance.ConstraintType,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteLanguage(Language instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Language_D(
                    m_UserContext,
                    (int?)instance.LanguageLCID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertLanguage(Language instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Language_I(
                    m_UserContext,
                    (int?)instance.LanguageLCID,
                    instance.CultureName,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateLanguage(Language instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Language_U(
                    m_UserContext,
                    (int?)instance.LanguageLCID,
                    instance.CultureName,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteMenu(Menu instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Menu_D(
                    m_UserContext,
                    (int?)instance.MenuID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertMenu(Menu instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Menu_I(
                    m_UserContext,
                    (int?)instance.MenuID,
                    instance.ParentMenuID,
                    instance.Sequence,
                    instance.URL,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateMenu(Menu instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Menu_U(
                    m_UserContext,
                    (int?)instance.MenuID,
                    instance.ParentMenuID,
                    instance.Sequence,
                    instance.URL,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteMenuPermission(MenuPermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_MenuPermission_D(
                    m_UserContext,
                    (int?)instance.MenuID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertMenuPermission(MenuPermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_MenuPermission_I(
                    m_UserContext,
                    (int?)instance.MenuID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeletePage(Page instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Page_D(
                    m_UserContext,
                    (int?)instance.PageID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertPage(Page instance) {
            int rc;
            string errorinfo = null;
            int? PageID = instance.PageID;

            rc = this.P_Page_I(
                    m_UserContext,
                    ref PageID,
                    instance.Name,
                    ref errorinfo);
            instance.PageID = PageID.GetValueOrDefault();
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdatePage(Page instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Page_U(
                    m_UserContext,
                    (int?)instance.PageID,
                    instance.Name,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeletePagePermission(PagePermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_PagePermission_D(
                    m_UserContext,
                    (int?)instance.PageID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertPagePermission(PagePermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_PagePermission_I(
                    m_UserContext,
                    (int?)instance.PageID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeletePermission(Permission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Permission_D(
                    m_UserContext,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertPermission(Permission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Permission_I(
                    m_UserContext,
                    (int?)instance.PermissionID,
                    instance.RequiredPermissionID,
                    instance.Action,
                    instance.Name,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdatePermission(Permission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Permission_U(
                    m_UserContext,
                    (int?)instance.PermissionID,
                    instance.RequiredPermissionID,
                    instance.Action,
                    instance.Name,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteRole(Role instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Role_D_Override(
                    m_UserContext,
                    (int?)instance.RoleID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertRole(Role instance) {
            int rc;
            string errorinfo = null;
            int? RoleID = instance.RoleID;

            rc = this.P_Role_I(
                    m_UserContext,
                    ref RoleID,
                    instance.Description,
                    ref errorinfo);
            instance.RoleID = RoleID.GetValueOrDefault();
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateRole(Role instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_Role_U(
                    m_UserContext,
                    (int?)instance.RoleID,
                    instance.Description,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteRolePermission(RolePermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_RolePermission_D(
                    m_UserContext,
                    (int?)instance.RolePermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertRolePermission(RolePermission instance) {
            int rc;
            string errorinfo = null;
            int? RolePermissionID = instance.RolePermissionID;

            rc = this.P_RolePermission_I(
                    m_UserContext,
                    ref RolePermissionID,
                    (int?)instance.RoleID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            instance.RolePermissionID = RolePermissionID.GetValueOrDefault();
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateRolePermission(RolePermission instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_RolePermission_U(
                    m_UserContext,
                    (int?)instance.RolePermissionID,
                    (int?)instance.RoleID,
                    (int?)instance.PermissionID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteUser(User instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_User_D_Override(
                    m_UserContext,
                    (int?)instance.UserID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertUser(User instance) {
            int rc;
            string errorinfo = null;
            int? UserID = instance.UserID;

            rc = this.P_User_I(
                    m_UserContext,
                    ref UserID,
                    instance.UserName,
                    instance.Password,
                    instance.Email,
                    (int?)instance.LanguageLCID,
                    instance.Name,
                    instance.FirstName,
                    (bool?)instance.Lockout,
                    ref errorinfo);
            instance.UserID = UserID.GetValueOrDefault();
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateUser(User instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_User_U(
                    m_UserContext,
                    (int?)instance.UserID,
                    instance.UserName,
                    instance.Password,
                    instance.Email,
                    (int?)instance.LanguageLCID,
                    instance.Name,
                    instance.FirstName,
                    (bool?)instance.Lockout,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void DeleteUserRole(UserRole instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_UserRole_D(
                    m_UserContext,
                    (int?)instance.UserRoleID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void InsertUserRole(UserRole instance) {
            int rc;
            string errorinfo = null;
            int? UserRoleID = instance.UserRoleID;

            rc = this.P_UserRole_I(
                    m_UserContext,
                    ref UserRoleID,
                    (int?)instance.UserID,
                    (int?)instance.RoleID,
                    ref errorinfo);
            instance.UserRoleID = UserRoleID.GetValueOrDefault();
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

        partial void UpdateUserRole(UserRole instance) {
            int rc;
            string errorinfo = null;

            rc = this.P_UserRole_U(
                    m_UserContext,
                    (int?)instance.UserRoleID,
                    (int?)instance.UserID,
                    (int?)instance.RoleID,
                    ref errorinfo);
            if (rc != 0) {
              throw new Exception(errorinfo != String.Empty ? errorinfo : string.Format(UnknownError, rc));
            }
        }

    }
}
