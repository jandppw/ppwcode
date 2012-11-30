using System;
using System.Web;
using System.Xml.Linq;

using FrontEnd.Code;

using System.Globalization;

namespace FrontEnd.Components
{
    public partial class LoginControl : BaseUserControl
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            rowVasco.Visible = false;

            if (!IsPostBack)
                HiddenFieldReturnUrl.Value = (String)HttpContext.Current.Items["ReturnUrl"];

            if (CurrentUser != null)
                Server.Transfer("~/Welcome.aspx");

            btnSubmit.Text = GetLocalResourceObject("btnSubmit").ToString();
        }

        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            lblWrongOtp.Visible = false;
            lblFillInOtp.Visible = false;
            lblWrongUserPass.Visible = false;
            lblVascoError.Visible = false;

            UserObject user = UserManager.GetUserData(txtUserName.Text, txtPass.Text, txtLoginAs.Text);

            if (user != null)
            {
                Session["UserContext"] = XDocument.Parse(UserManager.GetContext(user).ToString());
                lblWrongUserPass.Visible = false;
                CurrentUser = user;
                CultureInfo c = CultureInfo.GetCultureInfo(user.User.LanguageLCID);
                Session["MyCulture"] = c.Name;
                if (!String.IsNullOrEmpty(HiddenFieldReturnUrl.Value))
                    Server.Transfer(HiddenFieldReturnUrl.Value);
                else
                    Server.Transfer("~/Welcome.aspx");
            }
            else
            {
                lblWrongUserPass.Visible = true;
            }
        }
    }
}