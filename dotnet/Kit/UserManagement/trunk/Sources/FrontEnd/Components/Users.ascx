<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="Users.ascx.cs" Inherits="FrontEnd.Components.Users" %>
<%@ Register Assembly="DevExpress.Web.ASPxGridView.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxGridView" TagPrefix="dxwgv" %>

<asp:PlaceHolder ID="phList" runat="server"></asp:PlaceHolder>
<dxwgv:ASPxGridView ID="grdUsers" runat="server" AutoGenerateColumns="False" DataSourceID="dsUsers"
    OnStartRowEditing="grdUsers_StartRowEditing" EnableCallBacks="False" KeyFieldName="UserID">
    <SettingsBehavior ColumnResizeMode="Control" />
    <SettingsPager PageSize="20">
    </SettingsPager>
    <Settings ShowFilterRow="True" />
    <SettingsEditing Mode="EditForm" />
    <Columns>
        <dxwgv:GridViewDataTextColumn FieldName="UserName" VisibleIndex="0" ReadOnly="True"
            Caption="UserName" Width="100px" meta:resourcekey="lblUserName">
        </dxwgv:GridViewDataTextColumn>
        <dxwgv:GridViewDataTextColumn FieldName="Email" ReadOnly="True" VisibleIndex="2"
            Caption="Email" Width="150px" meta:resourcekey="lblEmail">
        </dxwgv:GridViewDataTextColumn>
        <dxwgv:GridViewDataTextColumn FieldName="Name" ReadOnly="True" VisibleIndex="3"
            Caption="Name" Width="100px" meta:resourcekey="lblName">
        </dxwgv:GridViewDataTextColumn>
        <dxwgv:GridViewDataTextColumn FieldName="FirstName" ReadOnly="True" VisibleIndex="4"
            Caption="FirstName" Width="100px" meta:resourcekey="lblFirstName">
        </dxwgv:GridViewDataTextColumn>
        <dxwgv:GridViewDataCheckColumn FieldName="Lockout" ReadOnly="True" VisibleIndex="5"
            Caption="Locked?" Width="100px" meta:resourcekey="lblLockout">
        </dxwgv:GridViewDataCheckColumn>
        <dxwgv:GridViewDataTextColumn FieldName="UserID" ReadOnly="True" Visible="False"
            VisibleIndex="6">
        </dxwgv:GridViewDataTextColumn>
    </Columns>
    <ClientSideEvents RowDblClick ="function(s, e) { s.StartEditRow(e.visibleIndex);}" ></ClientSideEvents>
</dxwgv:ASPxGridView>
<asp:LinqDataSource ID="dsUsers" runat="server" ContextTypeName="UserManagement.Data.UserManagementDataContext"
    Select="new (UserName, Email, Name, FirstName, Lockout, UserID)" TableName="Users"
    OnSelecting="dsUsers_Selecting">
</asp:LinqDataSource>
