using System;
using System.Reflection;

using FrontEnd.Code;

using UserManagement.Data;

using System.Threading;

namespace FrontEnd.Components.Metadata
{
	public partial class MetadataComponent : BaseUserControl
	{
		private object m_Entity;

		public object Entity
		{
			get { return m_Entity; }
			set { 
				m_Entity = value;
				Initialize(m_Entity);
			}
		}

		private void Initialize(object obj)
		{
			if (obj == null)
			{
                lblCreatedBy.Visible = false;
                lblModifiedBy.Visible = false;
                Visible = false;
				return;
			}

			try
			{
				PropertyInfo prop = obj.GetType().GetProperty("User");
				
				User user = (User)prop.GetValue(obj, null);

				PropertyInfo prop1 = obj.GetType().GetProperty("User1");
				User user1=(User)prop1.GetValue(obj, null);

				PropertyInfo propDc = obj.GetType().GetProperty("DateCreated");
                DateTime dateC = (DateTime)propDc.GetValue(obj, null);

			    PropertyInfo propDm = obj.GetType().GetProperty("DateModified");
				DateTime dateM = (DateTime)propDm.GetValue(obj, null);

                lblCreatedBy.Text = String.Format(GetLocalResourceObject("CreatedBy").ToString(), user.FirstName + " " + user.Name, Convert.ToDateTime(dateC, Thread.CurrentThread.CurrentCulture));
                lblModifiedBy.Text = String.Format(GetLocalResourceObject("ModifiedBy").ToString(), user1.FirstName + " " + user1.Name, Convert.ToDateTime(dateM, Thread.CurrentThread.CurrentCulture));
			}
			catch (Exception)
			{
                lblCreatedBy.Visible = false;
                lblModifiedBy.Visible = false;
                Visible = false;
			}
		}

		protected void Page_Load(object sender, EventArgs e)
		{

		}
	}
}