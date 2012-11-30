<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Roles.aspx.cs" Inherits="FrontEnd.Roles"  %>

<%@ Register src="Components/RoleComponents/Roles.ascx" tagname="Roles" tagprefix="uc1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="cphMain" runat="server"> 
    <uc1:Roles ID="Roles1" runat="server" />
</asp:Content>