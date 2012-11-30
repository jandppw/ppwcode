<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="SelectionComponent.ascx.cs"
    Inherits="FrontEnd.Components.SelectionComponents.SelectionComponent" %>
<%@ Register Assembly="AjaxControlToolkit" Namespace="AjaxControlToolkit" TagPrefix="cc1" %>
<%@ Register Assembly="DevExpress.Web.ASPxGridView.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxGridView" TagPrefix="dxwgv" %>
<table>
    <tr>
        <td>
            <asp:LinkButton ID="linkSelect" class="linkSelect" runat="server" meta:resourcekey="lblSelect">Select</asp:LinkButton>
        </td>
        <td>
            <asp:Button ID="btnClear" class="linkSelect" meta:resourcekey="btnClear" runat="server"
                Text="Button" OnClick="btnClear_Click" />
        </td>
    </tr>
</table>
<div>
    <asp:Panel ID="pnlSelection" runat="server" CssClass="modalPopup" Style="display: none"
        Width="500px">
        <asp:HiddenField ID="txtID" runat="server" />
        <asp:HiddenField ID="txtName" runat="server" />
        <dxwgv:ASPxGridView ID="grdList" runat="server" DataSourceID="dsList" AutoGenerateColumns="False"
            KeyFieldName="ID" OnDataBound="grdList_DataBound">
            <Settings ShowFilterRow="True" />
            <SettingsBehavior AllowFocusedRow="True" ColumnResizeMode="Control" />
            <SettingsPager PageSize="13" />
            <Columns>
                <dxwgv:GridViewDataTextColumn FieldName="ID" VisibleIndex="0" Caption="ID" Width="75px"
                    meta:resourcekey="lblID">
                </dxwgv:GridViewDataTextColumn>
                <dxwgv:GridViewDataTextColumn FieldName="Description" VisibleIndex="1" Caption="Description"
                    Width="400px" meta:resourcekey="lblDescription">
                </dxwgv:GridViewDataTextColumn>
            </Columns>
        </dxwgv:ASPxGridView>
        <asp:LinqDataSource ID="dsList" runat="server" TableName="Agents">
        </asp:LinqDataSource>
        <br />
        <div align="center">
            <asp:Button ID="OkButton" runat="server" Text="OK" meta:resourcekey="btnOK" Width="75px " />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <asp:Button ID="CancelButton" runat="server" Text="Cancel" meta:resourcekey="btnCancel"
                Width="75px" />
        </div>
    </asp:Panel>
    <cc1:ModalPopupExtender ID="mpeList" runat="server" TargetControlID="linkSelect"
        PopupControlID="pnlSelection" BackgroundCssClass="modalBackground" DropShadow="True"
        OkControlID="OkButton" CancelControlID="CancelButton" />
</div>
