using System;

namespace FrontEnd
{
	public partial class Welcome : BasePage
	{
		protected void Page_Load(object sender, EventArgs e)
		{
			if (CurrentUser != null)
			{
			    lblUserName.Text = CurrentUser.FullName;
			}
		}
	}
}
