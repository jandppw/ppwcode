using System;
using System.Web.UI;
using System.Web.UI.WebControls;

using DevExpress.Data;
using DevExpress.Web.ASPxGridView;

namespace FrontEnd.Code
{
    public class ListComponent : BaseUserControl
    {
        protected string ClassName = string.Empty;

        private LinkButton lnkCreate = new LinkButton();

        protected LinkButton linkCreate
        {
            get { return lnkCreate; }    
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!UserManager.HasRightForPage(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Right.C))
                linkCreate.Visible = false;

            InitializeGrid(this, UserID, 0);
        }

        protected void InitializeGrid(Control Page, int userId, int depth)
        {
            foreach (Control ctrl in Page.Controls)
            {
                if (ctrl is ASPxGridView)
                {
                    ASPxGridView grid = ((ASPxGridView)ctrl);
                    string keyField = grid.KeyFieldName;
                    for (int i = 0; i < grid.Columns.Count; i++)
                    {
                        if (grid.Columns[i] is GridViewDataTextColumn)
                        {
                            if (((GridViewDataTextColumn)grid.Columns[i]).FieldName == keyField)
                            {
                                if ((depth == 0) && (!IsPostBack))
                                {
                                    grid.SortBy(grid.Columns[i], ColumnSortOrder.Descending);
                                }

                                if (UserCanSeeIdColumn(userId))
                                {
                                    grid.Columns[i].Visible = true;
                                    grid.Columns[i].VisibleIndex = 0;
                                    grid.Columns[i].Width = new Unit("60px");
                                } 
                            }
                        }
                    }
                }
                else
                {
                    if (ctrl.Controls.Count > 0)
                    {
                        InitializeGrid(ctrl, userId, depth + 1);
                    }
                }
            }
        }

        private bool UserCanSeeIdColumn(int userId)
        {
            return (userId == 1);
        }
    }
}