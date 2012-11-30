using System;
using System.Collections.Generic;

using UserManagement.Data;

namespace FrontEnd.Code
{
    public class UserObject
    {
        public Int32 UserID
        {
            get { return User.UserID; }
            set { User.UserID = value; }
        }

        public int AdminID { get; set; }

        public string UserName { get; set; }

        public string Name { get; set; }

        public string FirstName { get; set; }

        public string FullName { get; set; }

        public User User { get; set; }

        public string Permissions { get; set; }

        private Dictionary<string, List<Right>> m_PagePermissionList = new Dictionary<string, List<Right>>();

        public Dictionary<string, List<Right>> PagePermissionList
        {
            get { return m_PagePermissionList; }
            set
            {
                m_PagePermissionList.Clear();
                m_PagePermissionList = value;
            }
        }

        public UserObject(String userName, User user, Int32 adminID, String perm, Dictionary<string, List<Right>> pagePermissionList)
        {
            FullName = string.Empty;
            AdminID = adminID;
            UserName = userName;
            FirstName = user.FirstName;
            Name = user.Name;
            SetFullName(FirstName, Name);
            User = user;
            Permissions = perm;
            PagePermissionList = pagePermissionList;
        }

        private void SetFullName(string fname, string sname)
        {
            if (fname != String.Empty)
                FullName = fname + " " + sname;
            else
                FullName = sname;
        }
    }
}
