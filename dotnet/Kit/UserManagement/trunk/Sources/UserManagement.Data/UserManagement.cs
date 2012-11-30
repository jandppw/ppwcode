namespace UserManagement.Data
{

	using System.Data.Linq.Mapping;
	using System.Reflection;

    partial class UserManagementDataContext
    {
        [Function(Name = "dbo.fnXMLGetMessageValue", IsComposable = true)]
        public string fnXMLGetMessageValue([Parameter(DbType = "Xml")] System.Xml.Linq.XElement doc, [Parameter(DbType = "Int")] System.Nullable<int> lcid, [Parameter(DbType = "Int")] System.Nullable<int> defaultLcid)
        {
            return ((string)(ExecuteMethodCall(this, ((MethodInfo)(MethodInfo.GetCurrentMethod())), doc, lcid, defaultLcid).ReturnValue));
        }
    }
}