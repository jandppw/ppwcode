<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="Roles.ascx.cs" Inherits="FrontEnd.Components.RoleComponents.Roles" %>
<%@ Register Assembly="DevExpress.Web.ASPxGridView.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxGridView" TagPrefix="dxwgv" %>
<%@ Register Assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxEditors" TagPrefix="dxe" %>

<asp:PlaceHolder ID="phList" runat="server"></asp:PlaceHolder>
<dxwgv:ASPxGridView ID="grdRoles" runat="server" AutoGenerateColumns="False" DataSourceID="dsRoles"
    OnStartRowEditing="grdRoles_StartRowEditing" KeyFieldName="RoleID" EnableCallBacks="False">
    <SettingsBehavior ColumnResizeMode="Control" />
    <SettingsPager PageSize="20">
    </SettingsPager>
    <Settings ShowFilterRow="True" />
    <SettingsEditing Mode="EditForm" />
    <Columns>
        <dxwgv:GridViewDataTextColumn FieldName="RoleID" meta:resourcekey="lblID" VisibleIndex="0"
            Width="90px">
        </dxwgv:GridViewDataTextColumn>
        <dxwgv:GridViewDataTextColumn FieldName="Description" VisibleIndex="1" Caption="Description"
            Width="250px" meta:resourcekey="lblDescription">
        </dxwgv:GridViewDataTextColumn>
    </Columns>
    <ClientSideEvents RowDblClick ="function(s, e) { s.StartEditRow(e.visibleIndex);}" ></ClientSideEvents>
</dxwgv:ASPxGridView>
<asp:LinqDataSource ID="dsRoles" runat="server" ContextTypeName="UserManagement.Data.UserManagementDataContext"
    Select="new (RoleID, Description)" TableName="Roles" OnSelecting="dsRoles_Selecting">
</asp:LinqDataSource>
