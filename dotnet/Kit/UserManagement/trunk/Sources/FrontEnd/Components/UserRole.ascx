<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="UserRole.ascx.cs" Inherits="FrontEnd.Components.UserRole" %>
<%@ Register Assembly="DevExpress.Web.ASPxGridView.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxGridView" TagPrefix="dxwgv" %>
<asp:UpdatePanel ID="UpdatePanel1" runat="server">
    <contenttemplate>
        <div>
            <table width="650px">
                <tr>
                    <td style="vertical-align: top">
                        <dxwgv:ASPxGridView ID="grdAvailableRoles" runat="server" AutoGenerateColumns="False"
                            DataSourceID="dsAvailableRoles" KeyFieldName="RoleID">
                            <SettingsPager PageSize="20">
                            </SettingsPager>
                            <Settings ShowFilterRow="True" />
                            <SettingsBehavior AllowFocusedRow="True" ColumnResizeMode="Control" />
                            <Columns>
                                <dxwgv:GridViewDataTextColumn FieldName="RoleID" ReadOnly="True" Visible="False"
                                    VisibleIndex="1">
                                </dxwgv:GridViewDataTextColumn>
                                <dxwgv:GridViewDataTextColumn Caption="Description" FieldName="Description" meta:resourcekey="lblAvailable"
                                    VisibleIndex="1" Width="300px">
                                </dxwgv:GridViewDataTextColumn>
                            </Columns>
                        </dxwgv:ASPxGridView>
                        <asp:LinqDataSource ID="dsAvailableRoles" runat="server" ContextTypeName="UserManagement.Data.UserManagementDataContext"
                            OnSelecting="dsAvailableRoles_Selecting">
                        </asp:LinqDataSource>
                    </td>
                    <td>
                        <asp:LinkButton ID="btnAddAll" class="swapButton" runat="server">►►</asp:LinkButton>
                        <br />
                        <br />
                        <asp:LinkButton ID="btnAdd" class="swapButton" runat="server">►</asp:LinkButton>
                        <br />
                        <br />
                        <asp:LinkButton ID="btnRemove" class="swapButton" runat="server">◄</asp:LinkButton>
                        <br />
                        <br />
                        <asp:LinkButton ID="btnRemoveAll" class="swapButton" runat="server">◄◄</asp:LinkButton> 
                    </td>
                    <td style="vertical-align: top">
                        <dxwgv:ASPxGridView ID="grdSelectedRoles" runat="server" AutoGenerateColumns="False"
                            DataSourceID="dsSelectedRoles" KeyFieldName="SelectedID">
                            <SettingsPager PageSize="20">
                            </SettingsPager>
                            <Settings ShowFilterRow="True" />
                            <SettingsBehavior AllowFocusedRow="True" ColumnResizeMode="Control" />
                            <Columns>
                                <dxwgv:GridViewDataTextColumn FieldName="SelectedID" ReadOnly="True" Visible="False"
                                    VisibleIndex="1">
                                </dxwgv:GridViewDataTextColumn>
                                <dxwgv:GridViewDataTextColumn Caption="Description" FieldName="Description" meta:resourcekey="lblSelected"
                                    VisibleIndex="1" Width="250px" ReadOnly="True">
                                </dxwgv:GridViewDataTextColumn>
                            </Columns>
                        </dxwgv:ASPxGridView>
                        <asp:ObjectDataSource ID="dsSelectedRoles" runat="server" SelectMethod="GetList" TypeName="FrontEnd.Components.UserRole">
                        </asp:ObjectDataSource>
                    </td>
                </tr>
            </table>
        </div>
    </contenttemplate>
</asp:UpdatePanel>
