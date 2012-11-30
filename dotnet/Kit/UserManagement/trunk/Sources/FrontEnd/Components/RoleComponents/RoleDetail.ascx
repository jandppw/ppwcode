<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="RoleDetail.ascx.cs"
    Inherits="FrontEnd.Components.RoleComponents.RoleDetail" %>
<%@ Register Assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxEditors" TagPrefix="dxe" %>
<%@ Register Src="~/Components/DescriptionInputComponent.ascx" TagName="DescriptionInputComponent"
    TagPrefix="uc1" %>
<%@ Register Src="CheckBoxList.ascx" TagName="CheckBoxList" TagPrefix="uc2" %>
<%@ Register src="../Metadata/MetadataComponent.ascx" tagname="MetadataComponent" tagprefix="uc3" %>
<div>
	<div id="divMetaData" runat="server">
        <uc3:MetadataComponent ID="MetadataComponent1" runat="server" />
    </div>
    <table style="width: 700px;">
        <tr>
            <td>
                <dxe:ASPxLabel ID="lblDescription" runat="server" meta:resourcekey="lblDescription">
                </dxe:ASPxLabel>
            </td>
            <td>
                <uc1:DescriptionInputComponent ID="Description" runat="server" />
            </td>
        </tr>
        <tr>
            <td>
                &nbsp;
            </td>
            <td>
                <uc2:CheckBoxList ID="CheckBoxList1" runat="server" />
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
