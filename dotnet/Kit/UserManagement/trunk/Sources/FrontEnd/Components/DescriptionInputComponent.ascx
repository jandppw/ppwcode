<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="DescriptionInputComponent.ascx.cs"
    Inherits="FrontEnd.Components.DescriptionInputComponent" %>
<%@ Register Assembly="DevExpress.Web.ASPxGridView.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxGridView" TagPrefix="dxwgv" %>
<%@ Register Assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxEditors" TagPrefix="dxe" %>
<asp:UpdatePanel ID="UpdatePanel1" runat="server">
    <contenttemplate>
        <table>
            <tr>
                <td>
                    <dxwgv:ASPxGridView ID="ASPxGridView1" runat="server" AutoGenerateColumns="False"
                        Width="500px" KeyFieldName="lcid" DataSourceID="ObjectDataSource1" EnableCallBacks="false">
                        <Styles>
                            <DetailCell Wrap="True">
                            </DetailCell>
                        </Styles>
                        <Settings ShowColumnHeaders="False" />
                        <SettingsBehavior ColumnResizeMode="Control" />
                        <ClientSideEvents DetailRowCollapsing="function(s, e) {
	onchange();
}" DetailRowExpanding="function(s, e) {
	onchange();
}" />
                        <Columns>
                            <dxwgv:GridViewCommandColumn VisibleIndex="0" Name="a" Caption=" " Width="80px">
                                <EditButton Visible="True">
                                </EditButton>
                                <DeleteButton Visible="True">
                                </DeleteButton>
                            </dxwgv:GridViewCommandColumn>
                            <dxwgv:GridViewDataTextColumn FieldName="LcidText" ReadOnly="True" ShowInCustomizationForm="False"
                                VisibleIndex="1" Width="100px">
                                <EditFormSettings Visible="False" />
                            </dxwgv:GridViewDataTextColumn>
                            <dxwgv:GridViewDataTextColumn FieldName="lcid" Caption=" " VisibleIndex="2" Name="a"
                                Visible="False" Width="1px">
                                <EditFormSettings Visible="True" />
                                <EditItemTemplate>
                                    <dxe:ASPxComboBox ID="ASPxComboBox1" runat="server" DataSourceID="LinqDataSource1"
                                        Text='<%# Bind("LcidText") %>' TextField="Description" Width="100px" 
                                        ondatabound="ASPxComboBox1_DataBound">
                                    </dxe:ASPxComboBox>
                                    <asp:LinqDataSource ID="LinqDataSource1" runat="server" ContextTypeName="UserManagement.Data.UserManagementDataContext"
                                        TableName="Languages"  OnSelecting="dsLanguages_Selecting">
                                    </asp:LinqDataSource>
                                </EditItemTemplate>
                            </dxwgv:GridViewDataTextColumn>
                            <dxwgv:GridViewDataTextColumn Caption=" " FieldName="Value" VisibleIndex="3" Width="300px">
                                <DataItemTemplate>
                                    <dxe:ASPxLabel ID="ASPxLabel1" runat="server" Text='<%# MyBind("Value") %>'>
                                    </dxe:ASPxLabel>
                                </DataItemTemplate>
                                <EditItemTemplate>
                                    <dxe:ASPxMemo ID="ASPxMemo1" runat="server" Height="100px" Text='<%# Bind("Value") %>'
                                        Width="300px">
                                    </dxe:ASPxMemo>
                                </EditItemTemplate>
                            </dxwgv:GridViewDataTextColumn>
                        </Columns>
                    </dxwgv:ASPxGridView>
                </td>
                <td class="descriptionNewColumn">
                    <asp:LinkButton ID="lnkNew" CssClass="lnkDescriptionNew" runat="server" OnClick="lnkNew_Click">New</asp:LinkButton>
                </td>
            </tr>
        </table>
        <asp:ObjectDataSource ID="ObjectDataSource1" runat="server" InsertMethod="InsertList"
            UpdateMethod="UpdateList" SelectMethod="GetList" DeleteMethod="DeleteList" TypeName="FrontEnd.Components.DescriptionInputComponent">
            <UpdateParameters>
                <asp:ControlParameter ControlID="ASPxGridView1" Name="id" PropertyName="ClientID"
                    Type="String" />
                <asp:Parameter Name="lcid" Type="Int32" />
                <asp:Parameter Name="lcidText" Type="String" />
                <asp:Parameter Name="Value" Type="String" />
            </UpdateParameters>
            <SelectParameters>
                <asp:ControlParameter ControlID="ASPxGridView1" Name="id" PropertyName="ClientID"
                    Type="String" />
            </SelectParameters>
            <DeleteParameters>
                <asp:ControlParameter ControlID="ASPxGridView1" Name="id" PropertyName="ClientID"
                    Type="String" />
            </DeleteParameters>
            <InsertParameters>
                <asp:ControlParameter ControlID="ASPxGridView1" Name="id" PropertyName="ClientID"
                    Type="String" />
                <asp:Parameter Name="lcidText" Type="String" />
                <asp:Parameter Name="Value" Type="String" />
            </InsertParameters>
        </asp:ObjectDataSource>
        
    </contenttemplate>
</asp:UpdatePanel>
