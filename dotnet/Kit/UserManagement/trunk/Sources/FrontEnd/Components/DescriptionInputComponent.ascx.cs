using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Linq;
using System.Xml.Serialization;

using DevExpress.Web.ASPxEditors;
using DevExpress.Web.Data;

using FrontEnd.Code;

namespace FrontEnd.Components
{
    public partial class DescriptionInputComponent : BaseUserControl
    {
        public event DetailsViewUpdatedEventHandler Change;

        private void OnChange()
        {
            if (Change != null)
            {
                Change(this, new DetailsViewUpdatedEventArgs(0, null));
            }
        }

        public XElement XmlValue
        {
            get
            {
                StringBuilder sb = GetValues();
                if (sb == null)
                {
                    return null;
                }
                return XElement.Parse(GetValues().ToString());
            }
            set
            {
                if (value == null)
                {
                    lnkNew_Click(null, null);
                    setList(new List<messagesMessage>(), ASPxGridView1.ClientID);
                    ASPxGridView1.DataBind();
                    return;
                }
                XmlSerializer ser = new XmlSerializer(typeof(messages));
                TextReader reader = new StringReader("<?xml version=\"1.0\" encoding=\"utf-16\"?>" + value);
                messages msg = (messages)ser.Deserialize(reader);
                if (msg != null && msg.Items != null)
                {
                    List<messagesMessage> list = msg.Items.ToList();

                    foreach (messagesMessage item in list)
                    {
                        var res = from l in GetDatabaseContext().Languages
                                  where l.LanguageLCID == item.lcid
                                  select l.Description;
                        item.LcidText = res.SingleOrDefault();
                    }
                    setList(list, ASPxGridView1.ClientID);
                }
                else
                {
                    setList(new List<messagesMessage>(), ASPxGridView1.ClientID);
                }
            }
        }

        protected String MyBind(String val)
        {
            String ret = Server.HtmlEncode("" + Eval(val));
            int index = ret.IndexOf("\n", StringComparison.Ordinal);
            if (index > 0)
            {
                ret = ret.Substring(0, index) + "...";
            }
            return ret;
        }

        public List<messagesMessage> GetList(String id)
        {
            List<messagesMessage> list = HttpContext.Current.Session["MyList" + id] as List<messagesMessage>;

            if (list == null)
            {
                list = new List<messagesMessage>();
                setList(list, id);
            }
            return list;
        }

        private void setList(List<messagesMessage> list, String id)
        {
            HttpContext.Current.Session["MyList" + id] = list;
        }

        private messagesMessage createMessage(String lcidText, String value)
        {
            messagesMessage l = new messagesMessage();
            var res = from lang in GetDatabaseContext().Languages
                      where lang.Description.Equals(lcidText)
                      select lang.LanguageLCID;
            l.lcid = res.SingleOrDefault();
            l.LcidText = lcidText;
            l.Value = value;
            return l;
        }

        public void InsertList(String id, String lcidText, String value)
        {
            if (value != String.Empty)
            {
                messagesMessage l = createMessage(lcidText, value);

                if (GetList(id).Contains(l))
                {
                    SetMasterPageError(GetLocalResourceObject("lblLanguageExists").ToString());
                }
                else
                {
                    GetList(id).Add(l);
                }
            }
        }

        public void UpdateList(String id, String lcidText, String value)
        {
            messagesMessage l = createMessage(lcidText, value);

            if (GetList(id).Contains(l))
            {
                int index = GetList(id).IndexOf(l);
                GetList(id).RemoveAt(index);
                GetList(id).Insert(index, l);
            }
        }

        public void DeleteList(String id, int lcid)
        {
            messagesMessage l = new messagesMessage
            {
                lcid = lcid
            };

            if (GetList(id).Contains(l))
            {
                GetList(id).Remove(l);
            }
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            ASPxGridView1.RowUpdated += ASPxGridView1_RowUpdated;
            ASPxGridView1.RowInserted += ASPxGridView1_RowInserted;
            ASPxGridView1.SettingsText.CommandEdit = GetLocalResourceObject("CommandEdit").ToString();
            ASPxGridView1.SettingsText.CommandDelete = GetLocalResourceObject("CommandDelete").ToString();
        }

        private void ASPxGridView1_RowInserted(object sender, ASPxDataInsertedEventArgs e)
        {
            OnChange();
        }

        private void ASPxGridView1_RowUpdated(object sender, ASPxDataUpdatedEventArgs e)
        {
            OnChange();
        }

        private StringBuilder GetValues()
        {
            StringBuilder sb = new StringBuilder();
            messagesMessage[] array = GetList(ASPxGridView1.ClientID).ToArray();

            bool defaultFound = array.Any(t => t.lcid == 1033);

            if (array.Length == 0)
            {
                return null;
            }
            if ((array.Length == 0) || (defaultFound == false))
            {
                throw new Exception(GetLocalResourceObject("lblFillDescription").ToString());
            }

            messages msg = new messages
            {
                Items = array
            };
            XmlSerializer ser = new XmlSerializer(typeof(messages));
            XmlWriterSettings set = new XmlWriterSettings
            {
                OmitXmlDeclaration = true
            };
            XmlWriter xml = XmlWriter.Create(sb, set);
            ser.Serialize(xml, msg);

            return sb;
        }

        protected void lnkNew_Click(object sender, EventArgs e)
        {
            ASPxGridView1.AddNewRow();
        }

        public void disableEditing()
        {
            lnkNew.Visible = false;
            ASPxGridView1.Columns[0].Visible = false;
        }

        protected void ASPxComboBox1_DataBound(object sender, EventArgs e)
        {
            if (((ASPxComboBox)sender).SelectedIndex == -1)
            {
                /* 1033 */
                ListEditItem item = ((ASPxComboBox)sender).Items.FindByValue("English");
                ((ASPxComboBox)sender).SelectedItem = item;
            }
        }

        protected void dsLanguages_Selecting(object sender, LinqDataSourceSelectEventArgs e)
        {
            var result = from l in GetLanguages()
                         select new
                         {
                             l.LanguageLCID,
                             l.Description
                         };
            e.Result = result;
        }
    }
}