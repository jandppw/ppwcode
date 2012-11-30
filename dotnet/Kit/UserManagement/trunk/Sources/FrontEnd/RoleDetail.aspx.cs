using System;
using System.Globalization;
using System.Threading;

namespace FrontEnd
{
    public partial class RoleDetail : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected override void InitializeCulture()
        {
            base.InitializeCulture();
            if (String.IsNullOrEmpty(Request["Culture"])) return;
            CultureInfo cultureInfo = new CultureInfo(Request["Culture"]);

            Thread.CurrentThread.CurrentCulture = cultureInfo;
            Thread.CurrentThread.CurrentUICulture = cultureInfo;
        }
    }
}
