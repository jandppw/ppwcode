dojo.provide("ppwcode.dojo.dijit.form.SelectDataViewBox");

dojo.require("ppwcode.dojo.dijit.form._FormDataViewBox");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");

dojo.declare(
	"ppwcode.dojo.dijit.form.SelectDataViewBox",
	ppwcode.dojo.dijit.form._FormDataViewBox,
	{
		templatePath: dojo.moduleUrl("ppwcode", "dojo/dijit/form/templates/SelectDataViewBox.html"),

		widgetsInTemplate: true,
		
		_setValueAttr: function(newValue) {
			this.selectItem(newValue);
		},
		
		_getValueAttr: function() {
			return this.getSelectedItem();
		}
	}
);