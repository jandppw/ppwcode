<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="MetadataComponent.ascx.cs" Inherits="FrontEnd.Components.Metadata.MetadataComponent" %>
<%@ Register assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1" namespace="DevExpress.Web.ASPxEditors" tagprefix="dxe" %>

    <div id="divMetaData" runat="server">
        <dxe:ASPxLabel ID="lblCreatedBy" runat="server" Text="Created By">
        </dxe:ASPxLabel>
        <br />
        <dxe:ASPxLabel ID="lblModifiedBy" runat="server" Text="Modified By">
        </dxe:ASPxLabel>
        <br />
        <br />
    </div>
    