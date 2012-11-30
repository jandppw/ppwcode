using System;
using System.Linq;
using System.Web.Security;
using UserManagement.Data;
using System.Text;
using System.Web.Configuration;
using System.Collections.Generic;
using System.Threading;
using System.Xml.Serialization;
using System.Xml;

namespace FrontEnd.Code
{
    public static class UserManager
    {
        private static User s_Us;

        private static bool ValidateUser(String user, String pass)
        {
            String p = FormsAuthentication.HashPasswordForStoringInConfigFile(user.ToUpper() + pass, FormsAuthPasswordFormat.MD5.ToString());

            return p == s_Us.Password;
        }

        public static UserObject GetUserData(String username, String pass, String loginAs)
        {
            UserManagementDataContext db = BasePage.GetDatabaseContext();

            int adminID = -1;

            var resU = from u in db.Users where u.UserName == username select u;
            s_Us = resU.SingleOrDefault();

            if ((s_Us != null) && (s_Us.Lockout == false))
            {
                if (ValidateUser(username, pass))
                {
                    if ((s_Us.Roles.Select(r => r.RoleID == 1).Count() == 1) && (loginAs != ""))
                    {
                        adminID = s_Us.UserID;
                        username = loginAs;
                        resU = from u in db.Users where u.UserName == loginAs select u;
                        s_Us = resU.SingleOrDefault();
                    }
                    if ((s_Us != null) && (s_Us.Lockout == false))
                    {
                        Dictionary<string, List<Right>> pagePermissionList = BuildPagePermissionList();
                        String permissionList = db.fnGetPermissionsList(s_Us.UserID);
                        UserObject userobj = new UserObject(username, s_Us, adminID, permissionList, pagePermissionList);

                        return userobj;
                    }
                }
            }
            return null;
        }

        public static bool HasRightForPage(UserObject user, string page, Right r)
        {
            if (user != null)
                if (user.PagePermissionList.Keys.Contains(page))
                    if (user.PagePermissionList[page].Contains(r))
                        return true;
            return false;
        }

        public static bool CanSeeDetail(UserObject user, string page, int id)
        {
            //if ((ID != -1) && (ID != -99))
            //{
            //    if ((page == "~/OrganizerDetail.aspx") && (BaseUserControl.canSeeOrganizer(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerDetail.aspx") && (BaseUserControl.canSeeReseller(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/AgentDetail.aspx") && (BaseUserControl.canSeeAgent(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/EventDetail.aspx") && (BaseUserControl.canSeeEvent(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ShowDetail.aspx") && (BaseUserControl.canSeeShow(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ProductDetail.aspx") && (BaseUserControl.canSeeProduct(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ArticleDetail.aspx") && (BaseUserControl.canSeeArticle(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/VenueDetail.aspx") && (BaseUserControl.canSeeVenue(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/VenueLayoutDetail.aspx") && (BaseUserControl.canSeeVenueLayout(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/VenueAreaDetail.aspx") && (BaseUserControl.canSeeVenueArea(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/VenueTypeDetail.aspx") && (BaseUserControl.canSeeVenueType(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/EventTypeDetail.aspx") && (BaseUserControl.canSeeEventType(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ActionDetail.aspx") && (BaseUserControl.canSeeAction(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/UserDetail.aspx") && (BaseUserControl.canSeeUser(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/UserShowEventDetail.aspx") && (BaseUserControl.canSeeUserShowEvent(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/PolicyDetail.aspx") && (BaseUserControl.canSeePolicy(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/AgentGroupDetail.aspx") && (BaseUserControl.canSeeAgentGroup(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerAgreementDetail.aspx") && (BaseUserControl.canSeeResellerAgreement(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/AgentAgreementDetail.aspx") && (BaseUserControl.canSeeAgentAgreement(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerPaymentDetail.aspx") && (BaseUserControl.canSeeResellerPaymentway(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerServiceDetail.aspx") && (BaseUserControl.canSeeResellerService(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerShipmentDetail.aspx") && (BaseUserControl.canSeeResellerShipment(user.UserID, ID)))
            //        return true;
            //    else if (page == "~/EventReport.aspx")
            //        return true;
            //    else if ((page == "~/ArticlePromoActionDetail.aspx") && (BaseUserControl.canSeeArticlePromoAction(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/FAQDetail.aspx") && (BaseUserControl.canSeeFAQ(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/CalendarDetail.aspx") && (BaseUserControl.canSeeCalendar(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/BarcodePoolDetail.aspx") && (BaseUserControl.canSeeBarcodePool(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/BasketDetail.aspx") && (BaseUserControl.canSeeBasket(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/PaymentProviderDetail.aspx") && (BaseUserControl.canSeePaymentProvider(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/BaseTemplateDetail.aspx") && (BaseUserControl.canSeeBaseTemplate(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/TicketTechBlockNomenDetail.aspx") && (BaseUserControl.canSeeTicketTechBlockNomen(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/TicketTemplateDetail.aspx"))// && (BaseUserControl.canSeeTicketTemplate(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ArticleVoucherDetail.aspx") && (BaseUserControl.canSeeArticleVoucher(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/MailTemplateDetail.aspx") && (BaseUserControl.canSeeMailTemplate(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/PageTemplateDetail.aspx") && (BaseUserControl.canSeePageTemplate(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ShowCalendarExceptionDetail.aspx") && (BaseUserControl.canSeeShow(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/ResellerCounterDetail.aspx") && (BaseUserControl.canSeeResellerCounter(user.UserID, ID)))
            //        return true;
            //    else if ((page == "~/WebshopPageConfigDetail.aspx") && (BaseUserControl.canSeeWebshopPageConfig(user.UserID, ID)))
            //        return true;
			//    else if ((page == "~/WebShopSupportTextDetail.aspx") && (BaseUserControl.canSeeWebshopSupportTextDetail(user.UserID, ID)))
			//        return true;
            //    return true;
            //}
            return true;
        }

        public static string GetEncryptedPassword(String user, String pass)
        {
            return FormsAuthentication.HashPasswordForStoringInConfigFile(user.ToUpper() + pass, FormsAuthPasswordFormat.MD5.ToString());
        }

        private static Dictionary<string, List<Right>> BuildPagePermissionList()
        {
            string prevPage = "";
            string currentPage = "";
            Dictionary<string, List<Right>> pagePermissionList = new Dictionary<string, List<Right>>();
            List<Right> rights = new List<Right>();

            UserManagementDataContext db = BasePage.GetDatabaseContext();
            var resPp = from pp in db.fnGetPagePermissions(s_Us.UserID) orderby pp.Name select pp;

            foreach (var item in resPp)
            {
                currentPage = item.Name;
                if (prevPage != currentPage)
                {
                    if (prevPage != "")
                        pagePermissionList.Add(prevPage, rights);
                    rights = new List<Right>();
                }
                rights.Add((Right)item.Action);
                prevPage = item.Name;
            }
            if (resPp.Count() > 0)
                pagePermissionList.Add(currentPage, rights);

            return pagePermissionList;
        }

        public static StringBuilder GetContext(UserObject userobj)
        {
            List<contextparam> cpList = new List<contextparam>();
            contextparams cps = new contextparams();
            contextparam cp = new contextparam
            {
                key = "UserID",
                Value = userobj.UserID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "AdminID",
                Value = userobj.AdminID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "LCID",
                Value = Thread.CurrentThread.CurrentCulture.LCID.ToString()
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "default_LCID",
                Value = "1033"
            };
            cpList.Add(cp);
            cp = new contextparam
            {
                key = "PermissionIDs",
                Value = userobj.Permissions
            };
            cpList.Add(cp);

            if (userobj.UserID == 1)
            {
                cp = new contextparam
                {
                    key = "Override_Delete",
                    Value = "1"
                };
                cpList.Add(cp);
            }

            contextparam[] array = cpList.ToArray<contextparam>();
            cps.contextparam = array;

            XmlSerializer ser = new XmlSerializer(typeof(contextparams));
            StringBuilder sb = new StringBuilder();
            XmlWriterSettings set = new XmlWriterSettings
            {
                OmitXmlDeclaration = true
            };
            XmlWriter xml = XmlWriter.Create(sb, set);
            ser.Serialize(xml, cps);
            return sb;
        }
    }
}
