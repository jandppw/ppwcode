using System.IO;
using System.Linq;
using System.Threading;
using System.Web.UI;
using System.Xml;
using System.Xml.Linq;

using UserManagement.Data;

namespace FrontEnd.Code
{
    public class BaseUserControl : UserControl
    {
        public UserObject CurrentUser
        {
            get { return (UserObject)Session["User"]; }
            set { Session["User"] = value; }
        }

        public int UserID
        {
            get { return (CurrentUser != null) ? CurrentUser.UserID : -1; }
        }

        public static UserManagementDataContext GetDatabaseContext()
        {
            return BasePage.GetDatabaseContext();
        }

        public static XElement GetUserContext()
        {
            return BasePage.GetUserContext();
        }

        public void SetMasterPageError(string message)
        {
            Control cnt = Parent;

            while (cnt != null)
            {
                if (cnt is MasterPage)
                {
                    ((UserManagementMaster)cnt).ShowError(message);
                    break;
                }
                cnt = cnt.Parent;
            }
        }

        protected string ParseXml2String(XElement doc)
        {
            using (StringWriter sw = new StringWriter())
            {
                XmlWriterSettings settings = new XmlWriterSettings();
                {
                    settings.OmitXmlDeclaration = true;

                    using (XmlWriter writer = XmlWriter.Create(sw, settings))
                    {
                        if (doc != null)
                        {
                            doc.WriteTo(writer);
                        }
                    }
                }

                return sw.ToString();
            }
        }

        public IQueryable<User> GetUsers()
        {
            IQueryable<User> result = from o in GetDatabaseContext().fnGetUsers(UserID, null)
                                      orderby o.UserName
                                      select o;
            return result;
        }

        public User GetUserByID(int id)
        {
            var res = from o in GetDatabaseContext().fnGetUsers(UserID, id)
                      select o;
            User org = res.SingleOrDefault();
            return org;
        }

        public static bool CanSeeUser(int userID, int id)
        {
            bool? fnCanSeeUser = GetDatabaseContext().fnCanSeeUser(userID, id);
            return fnCanSeeUser != null && (bool)fnCanSeeUser;
        }

        public IQueryable<Role> GetRoles()
        {
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            IQueryable<Role> result = from r in GetDatabaseContext().fnGetRoles(UserID, null)
                                        orderby GetDatabaseContext().fnXMLGetMessageValue(r.Description, lcid, 1033)
                                        select r;
            return result;
        }

        public Role GetRoleByID(int id)
        {
            var res = from p in GetDatabaseContext().fnGetRoles(UserID, id)
                      select p;
            Role role = res.SingleOrDefault();
            return role;
        }

        public static bool CanSeeRole(int userID, int id)
        {
            bool? fnCanSeeRole = GetDatabaseContext().fnCanSeeRole(userID, id);
            return fnCanSeeRole != null && (bool)fnCanSeeRole;
        }

        public IQueryable<Language> GetLanguages()
        {
            IQueryable<Language> result = from p in GetDatabaseContext().Languages
                                          orderby p.Description
                                          select p;
            return result;
        }

        public IQueryable<UserManagement.Data.Permission> GetPermissions()
        {
            IQueryable<UserManagement.Data.Permission> result = from p in GetDatabaseContext().fnGetPermissions(UserID)
                                      orderby p.Name
                                      select p;
            return result;
        }
    }
}