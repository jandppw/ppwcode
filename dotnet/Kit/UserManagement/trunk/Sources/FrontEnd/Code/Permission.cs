using System.Collections.Generic;

namespace FrontEnd.Code
{
	public class Permission
	{
	    public string PagePath { get; set; }

	    private List<Right> m_Rights = new List<Right>();

		public List<Right> Rights
		{
			get { return m_Rights; }
			set { m_Rights = value; }
		}
	}
}
