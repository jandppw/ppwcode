using System;
using System.Web;

namespace FrontEnd
{
    public class Global : HttpApplication
    {

        protected void Application_Start(object sender, EventArgs e)
        {

        }

        protected void Session_Start(object sender, EventArgs e)
        {

        }

        protected void Application_BeginRequest(object sender, EventArgs e)
        {
            if (HttpContext.Current.Request.Cookies.Count != 0)
            {
                return;
            }
            String sessionID = HttpContext.Current.Request["ctl00$SessionID"];
            if (String.IsNullOrEmpty(sessionID))
            {
                return;
            }
            HttpCookie cookie = new HttpCookie("ASP.NET_SessionId")
            {
                Value = sessionID
            };

            HttpContext.Current.Request.Cookies.Add(cookie);
        }

        protected void Application_AuthenticateRequest(object sender, EventArgs e)
        {

        }

        protected void Application_Error(object sender, EventArgs e)
        {

        }

        protected void Session_End(object sender, EventArgs e)
        {

        }

        protected void Application_End(object sender, EventArgs e)
        {

        }
    }
}