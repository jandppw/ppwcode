using System;
using System.Linq;
using System.Web.UI.WebControls;

using FrontEnd.Code;
using UserManagement.Data;

namespace FrontEnd.Components
{
    public partial class UserDetail : DetailComponent
    {
        private const string PasswordString = "abcdefg";

        private int Usr
        {
            get
            {
                if (ViewState["user"] == null) return -1;
                return (int)ViewState["user"];
            }
            set { ViewState["user"] = value; }
        }

        protected new void Page_Load(object sender, EventArgs e)
        {
            base.Page_Load(sender, e);

            linkBack.Text = GetLocalResourceObject("btnBack").ToString();
            linkBack.PostBackUrl = "~/Users.aspx";
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

            comboLanguage.DataBind();

            if (String.IsNullOrEmpty(Request.QueryString["user"])) return;

            int id = Int32.Parse(Request.QueryString["user"]);
            User usr = GetUserByID(id);

            if (!IsPostBack)
            {
                if (usr == null)
                {
                    usr = new User
                    {
                        UserID = -1
                    };
                }
                FillUserData(usr);
                Usr = usr.UserID;
            }
        }

        private void FillUserData(User usr)
        {
            if (usr.UserID != -1)
            {
                txtPassword.Text = PasswordString;
                txtRetypePassword.Text = PasswordString;
            }

            txtUserName.Text = usr.UserName;
            txtEmail.Text = usr.Email;
            txtName.Text = usr.Name; 
            txtFirstName.Text = usr.FirstName;
            chkLock.Checked = usr.Lockout;

            if (usr.UserID != -1)
            {
                comboLanguage.SelectedItem = comboLanguage.Items.FindByValue(usr.LanguageLCID);
            }

            UserRoleComponent1.currentUser = usr.UserID;
            UserRoleComponent1.fillSelectedRoles();

            MetadataComponent1.Entity = usr;
        }

        private void SetUserData(User usr)
        {
            usr.UserName = txtUserName.Text;
            if (txtPassword.Text != PasswordString)
            {
                usr.Password = UserManager.GetEncryptedPassword(usr.UserName, txtPassword.Text);
            }
            usr.Email = txtEmail.Text;
            usr.Name = txtName.Text;
            usr.FirstName = txtFirstName.Text;
            usr.Lockout = chkLock.Checked;
            usr.LanguageLCID = (int)comboLanguage.Value;

            UserRoleComponent1.setUserRoles(usr);
        }

        private new void btnSubmit_Click(object sender, EventArgs e)
        {
            bool isValid = DevExpress.Web.ASPxEditors.ASPxEdit.ValidateEditorsInContainer(this);
            if (isValid)
            {
                User usr = GetUserByID(Usr);

                if (txtPassword.Text != txtRetypePassword.Text)
                {
                    ((UserManagementMaster)(Parent.Page).Master).ShowError(GetLocalResourceObject("lblDifferentPassword").ToString());
                }
                else if (usr == null)
                {
                    usr = new User();
                    SetUserData(usr);

                    GetDatabaseContext().Users.InsertOnSubmit(usr);
                }
                else
                {
                    SetUserData(usr);
                }
                try
                {
                    GetDatabaseContext().SubmitChanges();
                    Server.Transfer("~/Users.aspx");
                }
                catch (Exception ex)
                {
                    ((UserManagementMaster)(Parent.Page).Master).ShowError(ex.Message);
                }
            }
        }

        private new void btnDelete_Click(object sender, EventArgs e)
        {
            User usr = GetUserByID(Usr);
            GetDatabaseContext().Users.DeleteOnSubmit(usr);
            try
            {
                GetDatabaseContext().SubmitChanges();
                Server.Transfer("~/Users.aspx");
            }
            catch (Exception ex)
            {
                ((UserManagementMaster)(Parent.Page).Master).ShowError(ex.Message);
            }

        }

        protected void dsLanguages_Selecting(object sender, LinqDataSourceSelectEventArgs e)
        {
            var result = from l in GetLanguages() select new { l.LanguageLCID, l.Description };
            e.Result = result;
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