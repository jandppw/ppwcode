using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Globalization;
using System.Threading;

namespace FrontEnd.Components
{
    public partial  class UserDetail1 : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected override void InitializeCulture()
        {
            base.InitializeCulture();
            if (String.IsNullOrEmpty(this.Request["Culture"])) return;
            CultureInfo cultureInfo = new CultureInfo(this.Request["Culture"]);

            Thread.CurrentThread.CurrentCulture = cultureInfo;
            Thread.CurrentThread.CurrentUICulture = cultureInfo;



        }
    }
}
