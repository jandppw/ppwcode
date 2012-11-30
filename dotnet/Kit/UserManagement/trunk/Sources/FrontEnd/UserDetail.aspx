<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="UserDetail.aspx.cs" Inherits="FrontEnd.Components.UserDetail1" %>

<%@ Register src="Components/UserDetail.ascx" tagname="UserDetail" tagprefix="uc1" %>


<asp:Content ID="Content1" ContentPlaceHolderID="cphMain" runat="server"> 
    <div>
    
        <uc1:UserDetail ID="UserDetailComponent" runat="server" />
    
    </div>
</asp:Content>
