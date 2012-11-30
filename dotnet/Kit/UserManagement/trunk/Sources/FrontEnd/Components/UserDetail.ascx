<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="UserDetail.ascx.cs"
    Inherits="FrontEnd.Components.UserDetail" %>
<%@ Register Assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxEditors" TagPrefix="dxe" %>
<%@ Register Src="Metadata/MetadataComponent.ascx" TagName="MetadataComponent" TagPrefix="uc7" %>
<%@ Register Src="UserRole.ascx" TagName="UserRoleComponent" TagPrefix="ur" %>
<div>
    <div id="divMetaData" runat="server">
        <uc7:MetadataComponent ID="MetadataComponent1" runat="server" />
    </div>
    <table class="data-details" runat="server">
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblUserName" runat="server" meta:resourcekey="lblUserName">
                </dxe:ASPxLabel>
            </td>
            <td colspan="3">
                <dxe:ASPxTextBox ID="txtUserName" runat="server" Width="170px" MaxLength="320">
                    <ValidationSettings CausesValidation="True">
                        <RequiredField IsRequired="True" />
                    </ValidationSettings>
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblPassword" runat="server" meta:resourcekey="lblPassword">
                </dxe:ASPxLabel>
            </td>
            <td>
                <dxe:ASPxTextBox ID="txtPassword" AutoCompleteType="None" runat="server" Width="170px"
                    Password="True" MaxLength="32">
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
            <td>
                <dxe:ASPxLabel ID="lblRetypePassword" runat="server" meta:resourcekey="lblRetypePassword">
                </dxe:ASPxLabel>
            </td>
            <td>
                <dxe:ASPxTextBox ID="txtRetypePassword" AutoCompleteType="None" runat="server" Width="170px"
                    Password="True" MaxLength="32">
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblEmail" runat="server" meta:resourcekey="lblEmail">
                </dxe:ASPxLabel>
            </td>
            <td colspan="3">
                <dxe:ASPxTextBox ID="txtEmail" runat="server" Width="250px" MaxLength="320">
                    <ValidationSettings CausesValidation="True">
                        <RequiredField IsRequired="True" />
                    </ValidationSettings>
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblLanguage" runat="server" meta:resourcekey="lblLanguage">
                </dxe:ASPxLabel>
            </td>
            <td colspan="3">
                <dxe:ASPxComboBox ID="comboLanguage" runat="server" DataSourceID="LinqDataSource2"
                    TextField="Description" ValueField="LanguageLCID" ValueType="System.Int32">
                    <ValidationSettings>
                        <RequiredField IsRequired="True" />
                    </ValidationSettings>
                    <ClientSideEvents SelectedIndexChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxComboBox>
                <asp:LinqDataSource ID="LinqDataSource2" runat="server" ContextTypeName="UserManagement.Data.UserManagementDataContext"
                    Select="new (LanguageLCID, Description)" TableName="Languages"  OnSelecting="dsLanguages_Selecting">
                </asp:LinqDataSource>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblName" runat="server" meta:resourcekey="lblName">
                </dxe:ASPxLabel>
            </td>
            <td>
                <dxe:ASPxTextBox ID="txtName" runat="server" Width="170px" MaxLength="64">
                    <ValidationSettings CausesValidation="True">
                        <RequiredField IsRequired="True" />
                    </ValidationSettings>
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
            <td>
                <dxe:ASPxLabel ID="lblFirstName" runat="server" meta:resourcekey="lblFirstName">
                </dxe:ASPxLabel>
            </td>
            <td>
                <dxe:ASPxTextBox ID="txtFirstName" runat="server" Width="250px" MaxLength="128">
                    <ClientSideEvents TextChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxTextBox>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblLockout" runat="server" meta:resourcekey="lblLockout">
                </dxe:ASPxLabel>
            </td>
            <td colspan="3">
                <dxe:ASPxCheckBox ID="chkLock" runat="server">
                    <ClientSideEvents CheckedChanged="function(s, e) {
	document.getElementById(&quot;ctl00_cphMain_UserDetailComponent_btnSubmit&quot;).className = &quot;btnSubmitChanged&quot;
}" />
                </dxe:ASPxCheckBox>
            </td>
        </tr>
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblRoles" runat="server" meta:resourcekey="lblRoles">
                </dxe:ASPxLabel>
            </td>
            <td colspan="3">
                <ur:UserRoleComponent ID="UserRoleComponent1" runat="server" />
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="ButtonColumn">
                <asp:PlaceHolder ID="phBack" runat="server"></asp:PlaceHolder>
            </td>
            <td class="ButtonColumn">
                <asp:PlaceHolder ID="phSubmit" runat="server"></asp:PlaceHolder>
            </td>
            <td class="ButtonColumn">
                <asp:PlaceHolder ID="phDelete" runat="server"></asp:PlaceHolder>
            </td>
        </tr>
    </table>
</div>
