using System;

namespace FrontEnd.Code
{
    public class SelectionItem
    {
        private int m_SelectedID;

        public int SelectedID
        {
            get { return m_SelectedID; }
            set { m_SelectedID = value; }
        }

        public string Description { get; set; }

        public string ExtraInfo { get; set; }

        public decimal Price { get; set; }

        public SelectionItem(int id, string descr)
        {
            SelectedID = id;
            Description = descr;
            ExtraInfo = String.Empty;
        }

        public SelectionItem(int id, string descr, string info)
        {
            SelectedID = id;
            Description = descr;
            ExtraInfo = info;
        }

        public SelectionItem(int id, string descr, decimal price)
        {
            SelectedID = id;
            Description = descr;
            Price = price;
        }

        public override int GetHashCode()
        {
            return m_SelectedID.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (!obj.GetType().Equals(typeof(SelectionItem))) return false;
            SelectionItem sp = (SelectionItem)obj;

            return SelectedID.Equals(sp.SelectedID);
        }
    }
}
