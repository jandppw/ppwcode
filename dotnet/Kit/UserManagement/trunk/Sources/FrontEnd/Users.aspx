<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Users.aspx.cs" Inherits="FrontEnd.Users"  %>

<%@ Register src="Components/Users.ascx" tagname="Users" tagprefix="uc1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="cphMain" runat="server"> 
    <uc1:Users ID="Users1" runat="server" />
</asp:Content>