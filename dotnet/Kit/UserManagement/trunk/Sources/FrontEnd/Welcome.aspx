<%@ Page Language="C#" MasterPageFile="~/UserManagement.Master" AutoEventWireup="true" CodeBehind="Welcome.aspx.cs" Inherits="FrontEnd.Welcome" %>

<asp:Content ID="Content2" ContentPlaceHolderID="cphMain" runat="server">
	<p>
		<asp:Label ID="lblWelcome" runat="server" Text="lblWelcome" meta:resourcekey="lblWelcome"></asp:Label>
		<asp:Label ID="lblUserName" runat="server" Text="Label"></asp:Label>
	</p>
</asp:Content>
