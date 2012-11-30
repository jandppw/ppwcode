using System;
using System.Web;
using System.Xml.Linq;
using System.Globalization;
using System.Threading;
using FrontEnd.Code;
using UserManagement.Data;

namespace FrontEnd
{
    public class BasePage : System.Web.UI.Page
    {
        const string BaseCulture = "en-US";

        public UserObject CurrentUser
        {
            get { return (UserObject)Session["User"]; }
            set { Session["User"] = value; }
        }

        public XElement UserContext
        {
            get { return (XElement)Session["UserContext"]; }
            set { Session["UserContext"] = value; }
        }

        public BasePage()
        {
            Load += BasePageLoad;
        }

        void BasePageLoad(object sender, EventArgs e)
        {
            if (CurrentUser == null && !IsCallback)
            {
                HttpContext.Current.Items["ReturnUrl"] = Request.Path;
                Server.Transfer("~/Login.aspx");
            }
            else if (CurrentUser != null && !IsCallback && Request.AppRelativeCurrentExecutionFilePath != "~/Welcome.aspx")
            {
                if (!UserManager.HasRightForPage(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Right.R))
                    Server.Transfer("~/NotAuthorized.aspx");
                else if ((Request.QueryString.Count > 0)
                    && (Request.AppRelativeCurrentExecutionFilePath.Contains("Detail"))
                    && (!UserManager.CanSeeDetail(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Int32.Parse(Request.QueryString[0]))))
                    Server.Transfer("~/NotAuthorized.aspx");
            }
        }

        protected override void InitializeCulture()
        {
            base.InitializeCulture();

            string c = Request["MyCulture"];

            if (string.IsNullOrEmpty(c))
                c = Convert.ToString(Session["MyCulture"]);
            else
                Session["MyCulture"] = c;

            try
            {
                Thread.CurrentThread.CurrentCulture = CultureInfo.CreateSpecificCulture(c);
                Thread.CurrentThread.CurrentUICulture = new CultureInfo(c);
            }
            catch
            {
                Session["MyCulture"] = BaseCulture;
                Thread.CurrentThread.CurrentCulture = CultureInfo.CreateSpecificCulture(BaseCulture);
                Thread.CurrentThread.CurrentUICulture = new CultureInfo(BaseCulture);
            }
        }

        public static UserManagementDataContext GetDatabaseContext()
        {
            UserManagementDataContext result = null;
            BasePage bp = new BasePage();
            XElement context = bp.UserContext;

            if (HttpContext.Current != null)
            {
                result = (UserManagementDataContext)HttpContext.Current.Items["UserManagementDataContext"];
            }

            if (result == null)
            {
                result = new UserManagementDataContext(context);

                if (HttpContext.Current != null)
                {
                    HttpContext.Current.Items["UserManagementDataContext"] = result;
                }
            }

            return result;
        }

        public static XElement GetUserContext()
        {
            BasePage bp = new BasePage();
            return bp.UserContext;
        }
    }
}
