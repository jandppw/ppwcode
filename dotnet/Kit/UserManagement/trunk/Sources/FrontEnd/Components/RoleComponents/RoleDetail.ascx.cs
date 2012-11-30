using System;
using System.Web.UI.WebControls;

using FrontEnd.Code;

using UserManagement.Data;

namespace FrontEnd.Components.RoleComponents
{
    public partial class RoleDetail : DetailComponent
    {
        private int Role
        {
            get
            {
                if (ViewState["role"] == null) return -1;
                return (int)ViewState["role"];
            }
            set { ViewState["role"] = value; }
        }

        protected new void Page_Load(object sender, EventArgs e)
        {
            base.Page_Load(sender, e);

            linkBack.Text = GetLocalResourceObject("btnBack").ToString();
            linkBack.PostBackUrl = "~/Roles.aspx";
            linkBack.CausesValidation = false;
            linkBack.ID = "btnBack";
            linkBack.CssClass = "btnBack";
            phBack.Controls.Add(linkBack);

            linkSubmit.Text = GetLocalResourceObject("btnSubmit").ToString();
            linkSubmit.Click += btnSubmit_Click;
            linkSubmit.ID = "btnSubmit";
            linkSubmit.CssClass = "btnSubmit";
            phSubmit.Controls.Add(linkSubmit);

            linkDelete.Text = GetLocalResourceObject("btnDelete").ToString();
            linkDelete.CausesValidation = false;
            linkDelete.Click += btnDelete_Click;
            linkDelete.Attributes.Add("onclick", "return confirm('" + GetLocalResourceObject("DeleteConfirm") + "');");
            linkDelete.ID = "btnDelete";
            linkDelete.CssClass = "btnDelete";
            phDelete.Controls.Add(linkDelete);

            Description.Change += DetailChanged;

            if (!IsPostBack)
            {
                if (String.IsNullOrEmpty(Request.QueryString["role"])) return;

                int id = Int32.Parse(Request.QueryString["role"]);
                Role role = GetRoleByID(id);

                if (role == null)
                {
                    role = new Role
                    {
                        RoleID = -1
                    };
                }
                else
                {
                    CheckBoxList1.LinkData(role);
                }
                FillRoleData(role);
                Role = role.RoleID;
            }
        }

        private void FillRoleData(Role role)
        {
            Description.XmlValue = role.Description;

            MetadataComponent1.Entity = role;
        }


        private void SetRoleData(Role role)
        {
            role.Description = Description.XmlValue;
        }

        protected new void btnSubmit_Click(object sender, EventArgs e)
        {
            bool isValid = DevExpress.Web.ASPxEditors.ASPxEdit.ValidateEditorsInContainer(this);
            if (isValid)
            {
                try
                {
                    Role role = GetRoleByID(Role);
                    if (role == null)
                    {
                        role = new Role();
                        SetRoleData(role);
                        CheckBoxList1.SetData(role);
                        GetDatabaseContext().Roles.InsertOnSubmit(role);
                    }
                    else
                    {
                        SetRoleData(role);
                        CheckBoxList1.SetData(role);
                    }

                    GetDatabaseContext().SubmitChanges();
                    Server.Transfer("~/Roles.aspx");
                }
                catch (Exception ex)
                {
                    ((UserManagementMaster)(Parent.Page).Master).ShowError(ex.Message);
                }
            }
        }

        protected new void btnDelete_Click(object sender, EventArgs e)
        {
            Role role = GetRoleByID(Role);
            GetDatabaseContext().Roles.DeleteOnSubmit(role);
            try
            {
                GetDatabaseContext().SubmitChanges();
                Server.Transfer("~/Roles.aspx");
            }
            catch (Exception ex)
            {
                ((UserManagementMaster)(Parent.Page).Master).ShowError(ex.Message);
            }
        }

		public override PlaceHolder PhBack
		{
			get { throw new NotImplementedException(); }
		}

		public override PlaceHolder PhSubmit
		{
			get { throw new NotImplementedException(); }
		}

		public override PlaceHolder PhDelete
		{
			get { throw new NotImplementedException(); }
		}

		public override string ReturnUrl
		{
			get { throw new NotImplementedException(); }
		}
	}
}
