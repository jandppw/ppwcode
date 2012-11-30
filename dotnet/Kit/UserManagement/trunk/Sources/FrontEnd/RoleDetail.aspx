<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="RoleDetail.aspx.cs" Inherits="FrontEnd.RoleDetail" %>

<%@ Register src="~/Components/RoleComponents/RoleDetail.ascx" tagname="RoleDetail" tagprefix="uc1" %>


<asp:Content ID="Content1" ContentPlaceHolderID="cphMain" runat="server"> 
    <div>
    
        <uc1:RoleDetail ID="RoleDetailComponent" runat="server" />
    
    </div>
</asp:Content>
