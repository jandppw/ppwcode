namespace FrontEnd.Code
{
    public class MenuItem
    {
        public int MenuID { get; set; }

        public int ParentMenuID { get; set; }

        public int Level { get; set; }

        public int Sequence { get; set; }

        public string Description { get; set; }

        public string Url { get; set; }

        public MenuItem(int aMenuID, int aParentMenuID, int aLevel, int aSequence, string aDescription, string aUrl)
        {
            MenuID = aMenuID;
            ParentMenuID = aParentMenuID;
            Level = aLevel;
            Sequence = aSequence;
            Description = aDescription;
            Url = aUrl;
        }
    }
}
