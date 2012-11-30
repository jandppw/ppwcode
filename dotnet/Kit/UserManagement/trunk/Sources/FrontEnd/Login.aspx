<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="FrontEnd.Login" %>

<%@ Register src="Components/LoginControl.ascx" tagname="LoginControl" tagprefix="uc1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="cphMain" runat="server"> 
    <uc1:LoginControl ID="LoginControl1" runat="server" />
</asp:Content>
