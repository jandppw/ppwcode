using System;
using System.Linq;
using System.Web.UI.WebControls;
using System.Threading;

using FrontEnd.Code;

using UserManagement.Data;

namespace FrontEnd.Components.RoleComponents
{
    public partial class Roles : ListComponent
    {
        private UserManagementDataContext m_DbContext;

        protected new void Page_Load(object sender, EventArgs e)
        {
            base.Page_Load(sender, e);

            m_DbContext = GetDatabaseContext();
            dsRoles.ContextTypeName = m_DbContext.ToString();
            grdRoles.DataBind();

            linkCreate.Text = GetLocalResourceObject("linkCreate").ToString();
            linkCreate.Click += linkCreate_Click;
            linkCreate.ID = "LinkCreate";
            linkCreate.CssClass = "LinkCreate";
            phList.Controls.Add(linkCreate);

        }

        protected void dsRoles_Selecting(object sender, LinqDataSourceSelectEventArgs e)
        {
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            var result = from r in GetRoles() select new { r.RoleID, Description = m_DbContext.fnXMLGetMessageValue(r.Description, lcid, 1033) };
            e.Result = result;
        }

        protected void grdRoles_StartRowEditing(object sender, DevExpress.Web.Data.ASPxStartRowEditingEventArgs e)
        {
            e.Cancel = true;
            Server.Transfer("~/RoleDetail.aspx?role=" + e.EditingKeyValue);
        }

        protected void linkCreate_Click(object sender, EventArgs e)
        {
            Server.Transfer("~/RoleDetail.aspx?role=-1");
        }
    }
}
