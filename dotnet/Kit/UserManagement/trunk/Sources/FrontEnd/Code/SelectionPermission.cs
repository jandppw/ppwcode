namespace FrontEnd.Code
{
    public class SelectionPermission
    {
        public int PermissionID { get; set; }

        public string Name { get; set; }

        public char Action { get; set; }

        public SelectionPermission(int id, string aName, char aAction)
        {
            PermissionID = id;
            Name = aName;
            Action = aAction;
        }

        public override int GetHashCode()
        {
            return PermissionID.GetHashCode() ^ Action.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (!obj.GetType().Equals(typeof(SelectionPermission))) return false;
            SelectionPermission sp = (SelectionPermission)obj;

            return ((PermissionID.Equals(sp.PermissionID)) && (Action.Equals(sp.Action)));
        }
    }
}
