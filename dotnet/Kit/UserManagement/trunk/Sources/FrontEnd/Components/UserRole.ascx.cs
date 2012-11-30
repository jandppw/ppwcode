using System;
using System.Linq;
using System.Web;
using System.Web.UI.WebControls;
using UserManagement.Data;
using System.Threading;
using System.Collections.Generic;
using FrontEnd.Code;

namespace FrontEnd.Components
{
    public partial class UserRole : BaseUserControl
    {
        private readonly UserManagementDataContext m_DbContext;

        private List<SelectionItem> selectedList
        {
            get
            {
                List<SelectionItem> list = HttpContext.Current.Session["SelectedUserRoleList"] as List<SelectionItem>;

                if (list == null)
                {
                    list = new List<SelectionItem>();
                    selectedList = list;
                }
                return list;
            }
            set
            {
                HttpContext.Current.Session["SelectedUserRoleList"] = value;
            }
        }

        private List<SelectionItem> fullList
        {
            get
            {
                List<SelectionItem> list = HttpContext.Current.Session["FullUserRoleList"] as List<SelectionItem>;

                if (list == null)
                {
                    list = new List<SelectionItem>();
                    fullList = list;
                }
                return list;
            }
            set
            {
                HttpContext.Current.Session["FullUserRoleList"] = value;
            }
        }

        public int currentUser
        {
            get
            {
                if (ViewState["currentUser"] == null) return -1;
                return (int)ViewState["currentUser"];
            }
            set { ViewState["currentUser"] = value; }
        }

        private bool MultipleSelection
        {
            get
            {
                if (ViewState["MultipleSelection"] == null) return true;
                return (bool)ViewState["MultipleSelection"];
            }
            set { ViewState["MultipleSelection"] = value; }
        }

        public UserRole()
        {
            m_DbContext = GetDatabaseContext();
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            dsAvailableRoles.ContextTypeName = m_DbContext.ToString();
            grdAvailableRoles.DataBind();

            btnAdd.Click += btnAdd_Click;
            btnRemove.Click += btnRemove_Click;
            btnAddAll.Click += btnAddAll_Click;
            btnRemoveAll.Click += btnRemoveAll_Click;
        }

        protected void dsAvailableRoles_Selecting(object sender, LinqDataSourceSelectEventArgs e)
        {
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            var result = from r in GetRoles() select new { r.RoleID, Description = m_DbContext.fnXMLGetMessageValue(r.Description, lcid, 1033) };
            e.Result = result;

            fullList = result.Select(a => new SelectionItem(a.RoleID, a.Description)).ToList();
        }

        public void fillSelectedRoles()
        {
            selectedList.Clear();
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            var result = from r in m_DbContext.UserRoles where r.UserID == currentUser select new { r.RoleID, Description = m_DbContext.fnXMLGetMessageValue(r.Role.Description, lcid, 1033) };
            foreach (SelectionItem p in result.Select(item => new SelectionItem(item.RoleID, item.Description)).Where(p => !selectedList.Contains(p)))
            {
                selectedList.Add(p);
            }
            grdSelectedRoles.DataBind();
        }

        public List<SelectionItem> GetList()
        {
            return selectedList;
        }

        protected void btnAdd_Click(object sender, EventArgs e)
        {
            try
            {
                int keyValue = (int)grdAvailableRoles.GetRowValues(grdAvailableRoles.FocusedRowIndex, "RoleID");
                string descr = (string)grdAvailableRoles.GetRowValues(grdAvailableRoles.FocusedRowIndex, "Description");
                SelectionItem p = new SelectionItem(keyValue, descr);
                if (!selectedList.Contains(p))
                {
                    if ((MultipleSelection) || (selectedList.Count == 0))
                        selectedList.Add(p);
                }
                grdSelectedRoles.DataBind();
            }
            catch 
            { 
            
            }
        }

        protected void btnRemove_Click(object sender, EventArgs e)
        {
            try
            {
                int keyValue = (int)grdSelectedRoles.GetRowValues(grdSelectedRoles.FocusedRowIndex, "SelectedID");
                SelectionItem p = new SelectionItem(keyValue, "");
                selectedList.Remove(p);
                grdSelectedRoles.DataBind();
            }
            catch
            {

            }
        }

        protected void btnAddAll_Click(object sender, EventArgs e)
        {
            try
            {
                foreach (SelectionItem selectionItem in fullList)
                {
                    if (!selectedList.Contains(selectionItem))
                        selectedList.Add(selectionItem);
                }
                grdSelectedRoles.DataBind();
            }
            catch (Exception ex)
            {
                ErrorLogger.WriteToLog("UserRole", ex);
            }
        }

        protected void btnRemoveAll_Click(object sender, EventArgs e)
        {
            try
            {
                selectedList.Clear();
                grdSelectedRoles.DataBind();
            }
            catch (Exception ex)
            {
                ErrorLogger.WriteToLog("UserRole", ex);
            }
        }

        public void setUserRoles(User u)
        {
            UserManagementDataContext dbContext = GetDatabaseContext();

            List<UserManagement.Data.UserRole> listToRemove = new List<UserManagement.Data.UserRole>();
            List<int> listToCheck = new List<int>();

            foreach (UserManagement.Data.UserRole item in u.UserRoles)
            {
                SelectionItem sp = new SelectionItem(item.RoleID, "");
                listToCheck.Add(item.RoleID);

                if (!selectedList.Contains(sp))
                    listToRemove.Add(item);
            }

            foreach (UserManagement.Data.UserRole item in listToRemove)
            {
                dbContext.UserRoles.DeleteOnSubmit(item);
            }

            foreach (SelectionItem item in selectedList)
            {
                UserManagement.Data.UserRole ur = new UserManagement.Data.UserRole
                {
                    UserID = u.UserID,
                    RoleID = item.SelectedID
                };
                if (!listToCheck.Contains(item.SelectedID))
                {
                    u.UserRoles.Add(ur);
                }
            }
        }
    }
}