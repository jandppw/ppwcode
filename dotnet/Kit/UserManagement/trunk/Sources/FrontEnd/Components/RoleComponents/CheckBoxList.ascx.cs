using System;
using System.Linq;
using System.Web;
using System.Web.UI.HtmlControls;
using DevExpress.Web.ASPxEditors;
using UserManagement.Data;
using System.Collections.Generic;
using FrontEnd.Code;

namespace FrontEnd.Components.RoleComponents
{
    public partial class CheckBoxList : BaseUserControl
    {
        private int m_RoleID;
        private UserManagementDataContext m_DbContext;
        private HtmlTable m_Table;

        private List<SelectionPermission> selectedList
        {
            get
            {
                List<SelectionPermission> list = HttpContext.Current.Session["RolePermissionList"] as List<SelectionPermission>;

                if (list == null)
                {
                    list = new List<SelectionPermission>();
                    selectedList = list;
                }
                return list;
            }
            set
            {
                HttpContext.Current.Session["RolePermissionList"] = value;
            }
        }

        private List<UserManagement.Data.Permission> fullList
        {
            get
            {
                List<UserManagement.Data.Permission> list = HttpContext.Current.Session["RolePermissionFullList"] as List<UserManagement.Data.Permission>;

                if (list == null)
                {
                    list = new List<UserManagement.Data.Permission>();
                    fullList = list;
                }
                return list;
            }
            set
            {
                HttpContext.Current.Session["RolePermissionFullList"] = value;
            }
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            m_Table = new HtmlTable();

            fullList = GetData();

            m_Table.Border = 1;
            HtmlTableRow row = GetHeader();
            m_Table.Rows.Add(row);

            String currentAction = null;
            foreach (UserManagement.Data.Permission p in fullList)
            {
                HtmlTableCell cell;

                if (currentAction == null || currentAction != p.Name)
                {
                    currentAction = p.Name;
                    row = new HtmlTableRow();
                    for (int i = 0; i < 5; i++) row.Cells.Add(new HtmlTableCell());
                    cell = new HtmlTableCell();
                    ASPxLabel label = new ASPxLabel
                    {
                        Text = p.Name
                    };
                    cell.Controls.Add(label);
                    row.Cells.RemoveAt(0);
                    row.Cells.Insert(0, cell);
                }
                cell = new HtmlTableCell();
                ASPxCheckBox chk = new ASPxCheckBox();
                chk.ClientSideEvents.CheckedChanged = "function(s, e) {document.getElementById('" + ((DetailComponent)Parent).linkSubmit.ClientID + "').className = 'btnSubmitChanged'}";
                chk.ID = "chk_" + p.Action + "_" + p.PermissionID.ToString();
                cell.Controls.Add(chk);
                SelectionPermission sp = new SelectionPermission(p.PermissionID, p.Name, p.Action);
                if (selectedList.Contains(sp))
                    chk.Checked = true;

                if (p.Action.Equals('R'))
                {
                    row.Cells.RemoveAt(1);
                    row.Cells.Insert(1, cell);
                }
                else if (p.Action.Equals('C'))
                {
                    row.Cells.RemoveAt(2);
                    row.Cells.Insert(2, cell);
                }
                else if (p.Action.Equals('U'))
                {
                    row.Cells.RemoveAt(3);
                    row.Cells.Insert(3, cell);
                }
                else if (p.Action.Equals('D'))
                {
                    row.Cells.RemoveAt(4);
                    row.Cells.Insert(4, cell);
                }
                m_Table.Rows.Add(row);
            }
            Controls.Add(m_Table);
        }

        private HtmlTableRow GetHeader()
        {
            HtmlTableRow row = new HtmlTableRow();
            HtmlTableCell c = new HtmlTableCell();
            ASPxLabel label = new ASPxLabel
            {
                Text = GetLocalResourceObject("Name").ToString()
            };
            c.Controls.Add(label);
            row.Cells.Add(c);

            c = new HtmlTableCell();
            label = new ASPxLabel
            {
                Text = GetLocalResourceObject("Read").ToString()
            };
            c.Controls.Add(label);
            row.Cells.Add(c);

            c = new HtmlTableCell();
            label = new ASPxLabel
            {
                Text = GetLocalResourceObject("Create").ToString()
            };
            c.Controls.Add(label);
            row.Cells.Add(c);

            c = new HtmlTableCell();
            label = new ASPxLabel
            {
                Text = GetLocalResourceObject("Update").ToString()
            };
            c.Controls.Add(label);
            row.Cells.Add(c);

            c = new HtmlTableCell();
            label = new ASPxLabel
            {
                Text = GetLocalResourceObject("Delete").ToString()
            };
            c.Controls.Add(label);
            row.Cells.Add(c);
            
            return row;
        }

        private List<UserManagement.Data.Permission> GetData()
        {
            fullList.Clear();
            var res = from p in GetDatabaseContext().fnGetPermissions(CurrentUser.UserID) orderby p.Name select p;
            return res.ToList();
        }

        public void LinkData(Role role)
        {
            selectedList.Clear();
            m_RoleID = role.RoleID;
            m_DbContext = GetDatabaseContext();
            var res = from rp in m_DbContext.RolePermissions where rp.RoleID == m_RoleID select rp;
            foreach (RolePermission item in res)
            {
                SelectionPermission sp = new SelectionPermission(item.PermissionID, item.Permission.Name, item.Permission.Action);
                if (!selectedList.Contains(sp))
                    selectedList.Add(sp);
            }
        }

        public void SetData(Role role)
        {
            foreach (HtmlTableRow row in m_Table.Rows)
            {
                for (int i = 0; i < row.Cells.Count; i++)
                {
                    if (row.Cells[i].Controls.Count > 0)
                    {
                        if (row.Cells[i].Controls[0] is ASPxCheckBox)
                        {
                            ASPxCheckBox chk = (ASPxCheckBox)row.Cells[i].Controls[0];
                            char action = Char.Parse(chk.ID.Substring(4, 1));
                            int id = int.Parse(chk.ID.Remove(0, 6));
                            SelectionPermission sp = new SelectionPermission(id, "", action);
                            if (chk.Checked)
                            {
                                if (!selectedList.Contains(sp))
                                {
                                    RolePermission rp = new RolePermission
                                    {
                                        RoleID = role.RoleID,
                                        PermissionID = sp.PermissionID
                                    };
                                    role.RolePermissions.Add(rp);
                                }
                            }
                            else
                            {
                                if (selectedList.Contains(sp))
                                {
                                    var result = from rps in GetDatabaseContext().RolePermissions where rps.RoleID == role.RoleID && rps.PermissionID == sp.PermissionID select rps;
                                    RolePermission rp = result.SingleOrDefault();
                                    if (rp != null)
                                    {
                                        GetDatabaseContext().RolePermissions.DeleteOnSubmit(rp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}