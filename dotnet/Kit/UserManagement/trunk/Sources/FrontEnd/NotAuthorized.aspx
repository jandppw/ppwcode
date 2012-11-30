<%@ Page Language="C#" MasterPageFile="~/UserManagement.Master" AutoEventWireup="true" CodeBehind="NotAuthorized.aspx.cs" Inherits="FrontEnd.NotAuthorized" %>
<%@ Register assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1" namespace="DevExpress.Web.ASPxEditors" tagprefix="dxe" %>
<asp:Content ID="Content2" ContentPlaceHolderID="cphMain" runat="server">
	<dxe:ASPxLabel ID="lblNotAuthorized" meta:resourcekey="lblNotAuthorized" Text="You are not authorized to view this page..." runat="server" Font-Bold="true" ForeColor="red">
    </dxe:ASPxLabel>
</asp:Content>
