using System;
using System.Web.UI;
using System.Web.UI.WebControls;

using DevExpress.Web.ASPxEditors;

using FrontEnd.Components;
using FrontEnd.Components.SelectionComponents;

namespace FrontEnd.Code
{
    public abstract class DetailComponent : BaseUserControl
    {
		/// <summary>
		/// Override this property and return a PlaceHolder object for a back Button.
		/// Return <code>null</code> if no Back Button needed 
		/// </summary>
		public abstract PlaceHolder PhBack { get; }

		/// <summary>
		/// Override this property and return a PlaceHolder object for a back Button.
		/// Return <code>null</code> if no Back Button needed 
		/// </summary>
		public abstract PlaceHolder PhSubmit { get; }

		/// <summary>
		/// Override this property and return a PlaceHolder object for a back Button.
		/// Return <code>null</code> if no Back Button needed 
		/// </summary>
		public abstract PlaceHolder PhDelete { get; }

		/// <summary>
		/// Override this property and return a url string that back button will redirect to.
		/// </summary>
		public abstract String ReturnUrl { get; }

        private LinkButton lnkBack = new LinkButton();

        public LinkButton linkBack
        {
            get { return lnkBack; }
        }

        private LinkButton lnkSubmit = new LinkButton();

        public LinkButton linkSubmit
        {
            get { return lnkSubmit; }
            set { lnkSubmit = value; }
        }

        private LinkButton lnkDelete = new LinkButton();

        public LinkButton linkDelete
        {
            get { return lnkDelete; }
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            bool readOnly = false;
            bool noDelete = false;

            if ((Request.AppRelativeCurrentExecutionFilePath.Contains("Home.aspx")) || (!Request.QueryString.HasKeys())) return;

            string[] strings = Request.QueryString.GetValues(0);
            if (strings != null && strings[0] == "-1")
            {
				noDelete = true;
                lnkDelete.Visible = false;
                if (!UserManager.HasRightForPage(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Right.C))
                    readOnly = true;
            }
            else
            {
                if (!UserManager.HasRightForPage(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Right.D))
                    noDelete = true;
                if (!UserManager.HasRightForPage(CurrentUser, Request.AppRelativeCurrentExecutionFilePath, Right.U))
                    readOnly = true;
            }

            InitializeItems(this, readOnly, noDelete);
        }

        protected void InitializeItems(Control page, bool readOnly, bool noDelete)
        {
            lnkSubmit.Visible = !readOnly;
            lnkDelete.Visible = !noDelete;

            foreach (Control ctrl in page.Controls)
            {
                if (ctrl is ASPxComboBox)
                {
                    ((ASPxComboBox)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxLabel)
                {
                    ((ASPxLabel)(ctrl)).EncodeHtml = false;
                }
                else if (ctrl is ASPxRadioButton)
                {
                    ((ASPxRadioButton)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxTextBox)
                {
                    ((ASPxTextBox)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxCheckBox)
                {
                    ((ASPxCheckBox)ctrl).Enabled = !readOnly;
                }
                else if (ctrl is ASPxDateEdit)
                {
                    ((ASPxDateEdit)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxDropDownEdit)
                {
                    ((ASPxDropDownEdit)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxMemo)
                {
                    ((ASPxMemo)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxListBox)
                {
                    ((ASPxListBox)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxRadioButtonList)
                {
                    ((ASPxRadioButtonList)ctrl).ReadOnly = readOnly;
                }
                else if (ctrl is ASPxSpinEdit)
                {
                    ((ASPxSpinEdit)ctrl).ReadOnly = readOnly;
                }
                else if ((ctrl is SelectionComponent) && readOnly)
                {
                    ((SelectionComponent)ctrl).disableSelecting();
                }
                else if ((ctrl is DescriptionInputComponent) && readOnly)
                {
                    ((DescriptionInputComponent)ctrl).disableEditing();
                }
                else
                {
                    if (ctrl.Controls.Count > 0)
                    {
                        InitializeItems(ctrl, readOnly, noDelete);
                    }
                }
            }
        }

        protected void DetailChanged(object sender, EventArgs e)
        {
            linkSubmit.CssClass = "btnSubmitChanged";
        }

        protected DateTime? GetComponentValue(DateTime? dbField, DateTime cField)
        {
            if (cField.Date == DateTime.MinValue)
            {
                return null;
            }
            return cField;
        }


		// Code extracted from children classes
		protected void loadControlButtons()
		{
			if (PhBack != null)
			{
				linkBack.Text = GetGlobalResourceObject("Components","btnBack").ToString();
				linkBack.PostBackUrl = ReturnUrl;
				linkBack.CausesValidation = false;
				linkBack.ID = "btnBack";
				linkBack.CssClass = "btnBack";
				PhBack.Controls.Add(linkBack);
			}
			if (PhSubmit != null)
			{
				linkSubmit.Text = GetGlobalResourceObject("Components", "btnSubmit").ToString();
				linkSubmit.Click += btnSubmit_Click;
				linkSubmit.ID = "btnSubmit";
				linkSubmit.CssClass = "btnSubmit";
				PhSubmit.Controls.Add(linkSubmit);
			}

			if (PhDelete != null)
			{
				linkDelete.Text = GetGlobalResourceObject("Components", "btnDelete").ToString();
				linkDelete.CausesValidation = false;
				linkDelete.Click += btnDelete_Click;
// ReSharper disable ResourceItemNotResolved
                //this is a base class for a all the detail components, the detail components should have the resourcestring in their resx files
				linkDelete.Attributes.Add("onclick", "return confirm('" + GetLocalResourceObject("DeleteConfirm") + "');");
// ReSharper restore ResourceItemNotResolved
				linkDelete.ID = "btnDelete";
				linkDelete.CssClass = "btnDelete";
				PhDelete.Controls.Add(linkDelete);
			}
		}


		/// <summary>
		/// Override to handle submit buttons click event
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		protected virtual void btnSubmit_Click(object sender, EventArgs e)
		{
			
		}

		/// <summary>
		/// Override to handle delete buttons click event
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		protected virtual void btnDelete_Click(object sender, EventArgs e)
		{
			
		}
    }
}