using System;
using System.Linq;
using System.Web.UI.WebControls;

using FrontEnd.Code;

using UserManagement.Data;

namespace FrontEnd.Components
{
    public partial class Users : ListComponent
    {
        private UserManagementDataContext m_DbContext;

        protected new void Page_Load(object sender, EventArgs e)
        {
            base.Page_Load(sender, e);

            m_DbContext = GetDatabaseContext();
            dsUsers.ContextTypeName = m_DbContext.ToString();
            grdUsers.DataBind();

            linkCreate.Text = GetLocalResourceObject("linkCreate").ToString();
            linkCreate.Click += linkCreate_Click;
            linkCreate.ID = "LinkCreate";
            linkCreate.CssClass = "LinkCreate";
            phList.Controls.Add(linkCreate);
        }

        protected void dsUsers_Selecting(object sender, LinqDataSourceSelectEventArgs e)
        {
            var result = from o in GetUsers() select new { o.UserID, o.UserName, o.Email, o.Name, o.FirstName, o.Lockout };
            e.Result = result;
        }

        protected void grdUsers_StartRowEditing(object sender, DevExpress.Web.Data.ASPxStartRowEditingEventArgs e)
        {
            e.Cancel = true;
            Server.Transfer("~/UserDetail.aspx?user=" + e.EditingKeyValue);
        }

        protected void linkCreate_Click(object sender, EventArgs e)
        {
            Server.Transfer("~/UserDetail.aspx?user=-1");
        }
    }
}
