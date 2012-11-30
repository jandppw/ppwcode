<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="LoginControl.ascx.cs"
    Inherits="FrontEnd.Components.LoginControl" %>
<%@ Register Assembly="DevExpress.Web.ASPxEditors.v8.2, Version=8.2.4.0, Culture=neutral, PublicKeyToken=9b171c9fd64da1d1"
    Namespace="DevExpress.Web.ASPxEditors" TagPrefix="dxe" %>

<script language="javascript">
    var sliderIntervalId = 0;
    var sliderHeight = 0;
    var sliding = false;
    var slideSpeed = 10;
    var fixedHeigth = 42;

    function Slide() {
        if (sliding)
            return;
        sliding = true;
        if (sliderHeight == fixedHeigth)
            sliderIntervalId = setInterval('SlideUpRun()', 30);
        else
            sliderIntervalId = setInterval('SlideDownRun()', 30);
    }

    function SlideUpRun() {
        var slider = document.getElementById('divLoginExtra');
        if (sliderHeight <= 0) {
            sliding = false;
            sliderHeight = 0;
            slider.style.height = '0px';
            clearInterval(sliderIntervalId);
        }
        else {
            sliderHeight -= slideSpeed;
            if (sliderHeight < 0)
                sliderHeight = 0;
            slider.style.height = sliderHeight + 'px';
        }
    }

    function SlideDownRun() {
        var slider = document.getElementById('divLoginExtra');
        if (sliderHeight >= fixedHeigth) {
            sliding = false;
            sliderHeight = fixedHeigth;
            slider.style.height = fixedHeigth + 'px';
            clearInterval(sliderIntervalId);
        }
        else {
            sliderHeight += slideSpeed;
            if (sliderHeight > fixedHeigth)
                sliderHeight = fixedHeigth;
            slider.style.height = sliderHeight + 'px';
        }
    } 
</script>

<dxe:ASPxLabel ID="lblWrongUserPass" runat="server" Visible="false" meta:resourcekey="lblWrongUserPass"
    Font-Bold="true" ForeColor="red">
</dxe:ASPxLabel>
<dxe:ASPxLabel ID="lblFillInOtp" runat="server" Visible="false" meta:resourcekey="lblFillInOtp"
    Font-Bold="true" ForeColor="red">
</dxe:ASPxLabel>
<dxe:ASPxLabel ID="lblWrongOtp" runat="server" Visible="false" meta:resourcekey="lblWrongOtp"
    Font-Bold="true" ForeColor="red">
</dxe:ASPxLabel>
<dxe:ASPxLabel ID="lblVascoError" runat="server" Visible="false" meta:resourcekey="lblVascoError"
    Font-Bold="true" ForeColor="red">
</dxe:ASPxLabel>
<br />
<br />
<div id="divLogin">
    <div id="divLoginBase">
        <table>
            <tr>
                <td class="loginColumn">
                    <dxe:ASPxLabel ID="lblUserName" runat="server" meta:resourcekey="lblUserName">
                    </dxe:ASPxLabel>
                </td>
                <td>
                    <dxe:ASPxTextBox ID="txtUserName" runat="server" Width="170px">
                    </dxe:ASPxTextBox>
                </td>
            </tr>
            <tr>
                <td class="loginColumn">
                    <dxe:ASPxLabel ID="lblPassWord" runat="server" meta:resourcekey="lblPassWord">
                    </dxe:ASPxLabel>
                </td>
                <td>
                    <dxe:ASPxTextBox ID="txtPass" runat="server" Password="True" Width="170px">
                    </dxe:ASPxTextBox>
                </td>
            </tr>
            <tr id="rowVasco" runat="server">
                <td class="loginColumn">
                    <dxe:ASPxLabel ID="lblOtp" runat="server" meta:resourcekey="lblOtp">
                    </dxe:ASPxLabel>
                </td>
                <td>
                    <dxe:ASPxTextBox ID="txtOtp" runat="server" Password="True" Width="170px">
                    </dxe:ASPxTextBox>
                </td>
            </tr>
        </table>
    </div>
    <div id="divExpand" onclick="Slide();">
        <dxe:ASPxLabel ID="lblAdvanced" runat="server" meta:resourcekey="lblAdvanced">
        </dxe:ASPxLabel>
    </div>
    <div id="divLoginExtra">
        <table>
            <tr>
                <td class="loginColumn">
                    <dxe:ASPxLabel ID="lblLoginAs" runat="server" meta:resourcekey="lblLoginAs">
                    </dxe:ASPxLabel>
                </td>
                <td>
                    <dxe:ASPxTextBox ID="txtLoginAs" runat="server" Width="170px">
                    </dxe:ASPxTextBox>
                </td>
            </tr>
        </table>
    </div>
    <div id="divSubmit">
        <asp:LinkButton ID="btnSubmit" runat="server" Font-Underline="false" OnClick="btnSubmit_Click">&nbsp;&nbsp;&nbsp;&nbsp;</asp:LinkButton>
    </div>
</div>
<asp:HiddenField ID="HiddenFieldReturnUrl" runat="server" />
<br />
<br />
<br />
<br />
<br />
<br />
<br />