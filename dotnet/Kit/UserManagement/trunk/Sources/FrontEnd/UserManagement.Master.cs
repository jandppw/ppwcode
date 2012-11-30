using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml.Linq;

using UserManagement.Data;
using System.Threading;
using System.Collections.Specialized;
using System.Globalization;
using System.Text;
using FrontEnd.Code;

using MenuItem = FrontEnd.Code.MenuItem;

namespace FrontEnd
{
    public partial class UserManagementMaster : MasterPage
    {
        private List<MenuItem> MenuList
        {
            get { return (List<MenuItem>)Session["MenuList"]; }
            set { Session["MenuList"] = value; }
        }

        private UserManagementDataContext m_DbContext;
        private UserObject m_User;
        private Dictionary<string, DevExpress.Web.ASPxMenu.MenuItem> m_MenuItems;
		private Dictionary<DevExpress.Web.ASPxMenu.MenuItem, String> m_MenuUrls;

        protected void Page_Load(object sender, EventArgs e)
        {
            Request.Cookies.Clear();
            mnuMain.Items.Clear();
            m_User = (UserObject)Session["User"];

            if (m_User != null)
            {
                lblLoggedInAs.Visible = true;
                lblLoggedInAs.Text = string.Concat(GetLocalResourceObject("LoggedInAs"), " ", m_User.UserName, "    |    ");
                linkLogout.Visible = true;
                lblVersion.Visible = true;
                lblVersion.Text = ConfigurationManager.AppSettings["version"];
                if (MenuList == null)
                    FetchMenu();
                BuildMenu(mnuMain, dsMenuItems);
                Session["User"] = m_User;
				
            }

            HiddenField field = new HiddenField
            {
                ID = "SessionID",
                Value = Session.SessionID
            };
            Page.Form.Controls.Add(field);
            lblError.Text = "";
        }

		void mnuMain_ItemClick(object source, DevExpress.Web.ASPxMenu.MenuItemEventArgs e)
		{
			DevExpress.Web.ASPxMenu.MenuItem i = e.Item;

			if (m_MenuUrls[i]!=null) Server.Transfer(m_MenuUrls[i]);

		}

        private void FetchMenu()
        {
            m_DbContext = BasePage.GetDatabaseContext();
            dsMenuItems.ContextTypeName = m_DbContext.ToString();
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            var result = from o in m_DbContext.fnGetMenuItems(m_User.User.UserID, lcid, 1033) orderby o.ParentMenuID, o.Sequence select o;

            List<MenuItem> itemList = new List<MenuItem>();
            foreach (var i in result)
            {
                if (i.ParentMenuID == null)
                    i.ParentMenuID = 0;
                MenuItem mItem = new MenuItem(i.MenuID, (int)i.ParentMenuID, (int)i.level, (int)i.Sequence, i.Description, i.URL);
                itemList.Add(mItem);
            }

            MenuList = itemList;
        }

        private void BuildMenu(DevExpress.Web.ASPxMenu.ASPxMenu menu, LinqDataSource dataSource)
        {
            m_MenuItems = new Dictionary<string, DevExpress.Web.ASPxMenu.MenuItem>();
			m_MenuUrls = new Dictionary<DevExpress.Web.ASPxMenu.MenuItem, string>();
            List<MenuItem> itemList = MenuList;

            foreach (MenuItem mItem in itemList)
            {
                DevExpress.Web.ASPxMenu.MenuItem item = CreateMenuItem(mItem.Description, mItem.Url);
                
                if (m_MenuItems.ContainsKey(mItem.ParentMenuID.ToString()))
                    m_MenuItems[mItem.ParentMenuID.ToString()].Items.Add(item);
                else
                {
                    if (mItem.ParentMenuID == 0)
                        menu.Items.Add(item);
                }

                m_MenuUrls.Add(item, mItem.Url);

                m_MenuItems.Add(mItem.MenuID.ToString(), item);
            }
        }

        private DevExpress.Web.ASPxMenu.MenuItem CreateMenuItem(string description, string url)
        {
            DevExpress.Web.ASPxMenu.MenuItem item = new DevExpress.Web.ASPxMenu.MenuItem
            {
                Text = description,
                NavigateUrl = url
            };

            return item;
        }

        private string BuildQueryString(NameValueCollection queryList) {
            StringBuilder sb = new StringBuilder(1024);
            string[] keys = queryList.AllKeys;
            for (int i = 0; i < keys.Length; i++) {
                string key = keys[i];
                if (i > 0) {
                    sb.Append('&');
                }
                sb.Append(key);
                sb.Append('=');
                sb.Append(queryList[key]);
            }
            return sb.ToString();
        }

        private string QueryChangeCulture(CultureInfo ci) {
            Session["UserContext"] = XElement.Parse(UserManager.GetContext(m_User).ToString());
            NameValueCollection queryList = new NameValueCollection(Request.QueryString);
            queryList.Set("MyCulture", ci.Name);
            return string.Concat(Request.AppRelativeCurrentExecutionFilePath, "?", BuildQueryString(queryList));
        }
 
        public void ShowError(string error)
        {
            lblError.Text = error;
        }

        protected void LinkEnClick(object sender, EventArgs e)
        {
            Server.Transfer(QueryChangeCulture(new CultureInfo("en-US")));
        }

        protected void LinkNlClick(object sender, EventArgs e)
        {
            Server.Transfer(QueryChangeCulture(new CultureInfo("nl-NL")));
        }

        protected void LinkFrClick(object sender, EventArgs e)
        {
            Server.Transfer(QueryChangeCulture(new CultureInfo("fr-FR")));
        }

        protected void LinkDeClick(object sender, EventArgs e)
        {
            Server.Transfer(QueryChangeCulture(new CultureInfo("de-DE")));
        }

        protected void LinkLogoutClick(object sender, EventArgs e)
        {
            Session.Clear();
            Request.Cookies.Clear();
            Server.Transfer("~/Login.aspx");
        }

        public void RegisterPostbackControl(Control ctrl)
        {
            ScriptManager1.RegisterPostBackControl(ctrl);
        }
    }
}
