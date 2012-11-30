using System;
using System.ComponentModel;
using System.Linq;
using System.Reflection;
using System.Threading;
using System.Web.UI;
using System.Web.UI.WebControls;

using DevExpress.Web.ASPxEditors;

using FrontEnd.Code;

using UserManagement.Data;

namespace FrontEnd.Components.SelectionComponents
{
    public partial class SelectionComponent : BaseUserControl
    {
        private UserManagementDataContext m_DbContext;
        private HiddenField idField = new HiddenField();
        private ASPxTextBox descrField = new ASPxTextBox();
        private HiddenField fieldToClear;
        private ASPxTextBox descrToClear;
        private SelectionTypes selectFunction = SelectionTypes.None;
        public event EventHandler BeforeSelecting;

        [DefaultValue(false)]
        public bool DoPostback { get; set; }

        public SelectionComponent()
        {
            idField.Value = "-1";
        }

        private void onBeforeSelecting()
        {
            if (BeforeSelecting != null)
            {
                BeforeSelecting(this, EventArgs.Empty);
            }
        }

        [Browsable(true)]
        [TypeConverter(typeof(MyTypeConverter))]
        public SelectionTypes SelectionHandler
        {
            get { return selectFunction; }
            set { selectFunction = value; }
        }

        public HiddenField IdField
        {
            get { return idField; }
            set { idField = value; }
        }

        public ASPxTextBox DescrField
        {
            get { return descrField; }
            set
            {
                descrField = value;
                descrField.ReadOnly = true;
            }
        }

        public HiddenField FieldToClear
        {
            get { return fieldToClear; }
            set { fieldToClear = value; }
        }

        public ASPxTextBox DescrToClear
        {
            get { return descrToClear; }
            set { descrToClear = value; }
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            DetailComponent dc = null;
            Control c = this;

            try
            {
                while (dc == null)
                {
                    c = c.Parent;
                    if (c is DetailComponent)
                    {
                        dc = (DetailComponent)c;
                    }
                }
            }
            catch (Exception)
            {
                //throw new Exception("None of the parent components are DetailComponent.");
            }

            if (FieldToClear != null)
            {
                mpeList.OnOkScript = "document.getElementById('" + IdField.ClientID + "').value = document.getElementById('" + txtID.ClientID + "').value; " +
                                     " document.getElementById('" + DescrField.ClientID + "_I').value = document.getElementById('" + txtName.ClientID + "').value; " +
                                     " document.getElementById('" + FieldToClear.ClientID + "').value = '-1';" +
                                     " document.getElementById('" + DescrToClear.ClientID + "_I').value = '';";
            }
            else
            {
                mpeList.OnOkScript = "document.getElementById('" + IdField.ClientID + "').value = document.getElementById('" + txtID.ClientID + "').value; " +
                                     " document.getElementById('" + DescrField.ClientID + "_I').value = document.getElementById('" + txtName.ClientID + "').value; ";
            }

            if (dc != null)
            {
                mpeList.OnOkScript = mpeList.OnOkScript +
                                     " document.getElementById('" + dc.linkSubmit.ClientID + "').className = 'btnSubmitChanged'; ";
            }

            if (DoPostback)
            {
                Control d = Parent;
                try
                {
                    while (!(d is UpdatePanel))
                    {
                        d = d.Parent;
                    }
                }
                catch (Exception)
                {
                    d = null;
                }

                if (d != null)
                {
                    mpeList.OnOkScript += "__doPostBack('" + d.ClientID + "','');";
                }
                else
                {
                    mpeList.OnOkScript += " document.aspnetForm.submit(); ";
                }
            }

            grdList.ClientSideEvents.FocusedRowChanged = " function (s,e) { var hasInnerText = (document.getElementsByTagName('body')[0].innerText != undefined) ? true : false; " +
                                                         "  var index = s.GetFocusedRowIndex(); " +
                                                         "   if (index >= 0) { " +
                                                         "     var row = s.GetRow(index); " +
                                                         "     if(!hasInnerText){ " +
                                                         "         document.getElementById('" + txtID.ClientID + "').value = row.cells[0].textContent;" +
                                                         "         document.getElementById('" + txtName.ClientID + "').value = row.cells[1].textContent;" +
                                                         "     } else{" +
                                                         "         document.getElementById('" + txtID.ClientID + "').value = row.cells[0].innerText;" +
                                                         "         document.getElementById('" + txtName.ClientID + "').value = row.cells[1].innerText;" +
                                                         "     } " +
                                                         "  }  } ";

            if (SelectionHandler != SelectionTypes.None)
            {
                EventInfo eventInfo = dsList.GetType().GetEvent("Selecting");
                Type evType = eventInfo.EventHandlerType;
                Delegate d = Delegate.CreateDelegate(evType, this, SelectionHandler.ToString());
                MethodInfo addMethod = eventInfo.GetAddMethod();
                Object[] addHandlerArgs = { d };
                addMethod.Invoke(dsList, addHandlerArgs);

                m_DbContext = GetDatabaseContext();
                dsList.ContextTypeName = m_DbContext.ToString();
                grdList.DataBind();
            }
            else
            {
                DescrField.Visible = false;
                Visible = false;
            }
        }

        public void Reload()
        {
            Page_Load(null, null);
        }

        protected void btnClear_Click(object sender, EventArgs e)
        {
            idField.Value = "-1";
            descrField.Value = "";
            txtID.Value = "-1";
            txtName.Value = "";

            if (FieldToClear != null)
            {
                fieldToClear.Value = "-1";
                descrToClear.Value = "";
            }
        }

        public int SelectedID
        {
            get
            {
                if (!String.IsNullOrEmpty(idField.Value))
                {
                    return int.Parse(idField.Value);
                }
                return -1;
            }
            set
            {
                idField.Value = value.ToString();
                btnClear.Visible = false;
            }
        }

        public string Description
        {
            get { return descrField.Text; }
            set { descrField.Text = value; }
        }

        protected void grdList_DataBound(object sender, EventArgs e)
        {
            grdList.FocusedRowIndex = -1;
        }

        public void disableSelecting()
        {
            linkSelect.Visible = false;
        }

        protected virtual void dsList_Selecting_User(object sender, LinqDataSourceSelectEventArgs e)
        {
            onBeforeSelecting();
            var result = from u in GetUsers()
                         select new
                         {
                             ID = u.UserID,
                             Description = u.UserName
                         };
            e.Result = result;
        }

        protected virtual void dsList_Selecting_Role(object sender, LinqDataSourceSelectEventArgs e)
        {
            onBeforeSelecting();
            int lcid = Thread.CurrentThread.CurrentCulture.LCID;
            var result = from r in GetRoles()
                         select new
                         {
                             ID = r.RoleID,
                             Description = m_DbContext.fnXMLGetMessageValue(r.Description, lcid, 1033)
                         };
            e.Result = result;
        }
    }
}