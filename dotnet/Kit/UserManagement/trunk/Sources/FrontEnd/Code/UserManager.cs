using System;
using System.Linq;
using System.Web.Security;
using UserManagement.Data;
using System.Text;
using System.Web.Configuration;
using System.Collections.Generic;
using System.Threading;
using System.Xml.Serialization;
using System.Xml;

namespace FrontEnd.Code
{
    public static class UserManager
    {
        private static User s_Us;

        private static bool ValidateUser(String user, String pass)
        {
            String p = FormsAuthentication.HashPasswordForStoringInConfigFile(user.ToUpper() + pass, FormsAuthPasswordFormat.MD5.ToString());

            return p == s_Us.Password;
        }

        public static UserObject GetUserData(String username, String pass, String loginAs)
        {
            UserManagementDataContext db = BasePage.GetDatabaseContext();

            int adminID = -1;

            var resU = from u in db.Users where u.UserName == username select u;
            s_Us = resU.SingleOrDefault();

            if ((s_Us != null) && (s_Us.Lockout == false))
            {
                if (ValidateUser(username, pass))
                {
                    if ((s_Us.Roles.Select(r => r.RoleID == 1).Count() == 1) && (loginAs != ""))
                    {
                        adminID = s_Us.UserID;
                        username = loginAs;
                        resU = from u in db.Users where u.UserName == loginAs select u;
                        s_Us = resU.SingleOrDefault();
                    }
                    if ((s_Us != null) && (s_Us.Lockout == false))
                    {
                        Dictionary<string, List<Right>> pagePermissionList = BuildPagePermissionList();
                        String permissionList = db.fnGetPermissionsList(s_Us.UserID);
                        UserObject userobj = new UserObject(username, s_Us, adminID, permissionList, pagePermissionList);

                        return userobj;
                    }
                }
            }
            return null;
        }

        public static bool HasRightForPage(UserObject user, string page, Right r)
        {
            if (user != null)
                if (user.PagePermissionList.Keys.Contains(page))
                    if (user.PagePermissionList[page].Contains(r))
                        return true;
            return false;
        }

        public static bool CanSeeDetail(UserObject user, string page, int id)
        {
            //if (id != -1)
            //{
            //    if ((page == "~/UserDetail.aspx") && (BaseUserControl.canSeeUser(user.UserID, id)))
            //        return true;
            //    else if ((page == "~/RoleDetail.aspx") && (BaseUserControl.canSeeRole(user.UserID, id)))
            //        return true;
            //    return false;
            //}
            return true;
        }

        public static string GetEncryptedPassword(String user, String pass)
        {
            return FormsAuthentication.HashPasswordForStoringInConfigFile(user.ToUpper() + pass, FormsAuthPasswordFormat.MD5.ToString());
        }

        private static Dictionary<string, List<Right>> BuildPagePermissionList()
        {
            string prevPage = "";
            string currentPage = "";
            Dictionary<string, List<Right>> pagePermissionList = new Dictionary<string, List<Right>>();
            List<Right> rights = new List<Right>();

            UserManagementDataContext db = BasePage.GetDatabaseContext();
            var resPp = from pp in db.fnGetPagePermissions(s_Us.UserID) orderby pp.Name select pp;

            foreach (var item in resPp)
            {
                currentPage = item.Name;
                if (prevPage != currentPage)
                {
                    if (prevPage != "")
                        pagePermissionList.Add(prevPage, rights);
                    rights = new List<Right>();
                }
                rights.Add((Right)item.Action);
                prevPage = item.Name;
            }
            if (resPp.Count() > 0)
                pagePermissionList.Add(currentPage, rights);

            return pagePermissionList;
        }

        public static StringBuilder GetContext(UserObject userobj)
        {
            List<contextparam> cpList = new List<contextparam>();
            contextparams cps = new contextparams();
            contextparam cp = new contextparam
            {
                key = "UserID",
                Value = userobj.UserID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "AdminID",
                Value = userobj.AdminID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "LCID",
                Value = Thread.CurrentThread.CurrentCulture.LCID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "default_LCID",
                Value = "1033"
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "PermissionIDs",
                Value = userobj.Permissions
            };
            cpList.Add(cp);

            if (userobj.UserID == 1)
            {
                cp = new contextparam
                {
                    key = "Override_Delete",
                    Value = "1"
                };
                cpList.Add(cp);
            }

            contextparam[] array = cpList.ToArray<contextparam>();
            cps.contextparam = array;

            XmlSerializer ser = new XmlSerializer(typeof(contextparams));
            StringBuilder sb = new StringBuilder();
            XmlWriterSettings set = new XmlWriterSettings
            {
                OmitXmlDeclaration = true
            };
            XmlWriter xml = XmlWriter.Create(sb, set);
            ser.Serialize(xml, cps);
            return sb;
        }
    }
}
