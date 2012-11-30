using System.Data.Linq.Mapping;
using System.Linq;
using System.Reflection;

namespace UserManagement.Data
{
    partial class UserManagementDataContext
    {
        [Function(Name = "dbo.fnGetUsers", IsComposable = true)]
        public IQueryable<User> fnGetUsers([Parameter(Name = "UserID", DbType = "Int")] int? userID, [Parameter(Name = "ViewUserID", DbType = "Int")] int? viewUserID)
        {
            return CreateMethodCallQuery<User>(this, ((MethodInfo)(MethodBase.GetCurrentMethod())), userID, viewUserID);
        }

        [Function(Name = "dbo.fnGetRoles", IsComposable = true)]
        public IQueryable<Role> fnGetRoles([Parameter(Name = "UserID", DbType = "Int")] int? userID, [Parameter(Name = "RoleID", DbType = "Int")] int? roleID)
        {
            return CreateMethodCallQuery<Role>(this, ((MethodInfo)(MethodBase.GetCurrentMethod())), userID, roleID);
        }

        [Function(Name = "dbo.fnGetPermissions", IsComposable = true)]
        public IQueryable<Permission> fnGetPermissions([Parameter(Name = "UserID", DbType = "Int")] int? userID)
        {
            return CreateMethodCallQuery<Permission>(this, ((MethodInfo)(MethodBase.GetCurrentMethod())), userID);
        }
    }
}