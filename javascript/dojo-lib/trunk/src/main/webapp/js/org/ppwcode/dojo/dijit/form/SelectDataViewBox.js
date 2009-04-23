dojo.provide("org.ppwcode.dojo.dijit.form.SelectDataViewBox");

dojo.require("org.ppwcode.dojo.dijit.form._FormDataViewBox");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.SelectDataViewBox",
	org.ppwcode.dojo.dijit.form._FormDataViewBox,
	{
		templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/SelectDataViewBox.html"),

		widgetsInTemplate: true,
		
		setValue: function(newValue) {
			this.selectItem(newValue);
		},
		
		getValue: function() {
			return this.getSelectedItem();
		}
	}
);