dojo.provide("ppwcode.dojo.dijit.form.ImageBox");

dojo.require("dijit._Templated");
dojo.require("dijit.form._FormWidget");

dojo.declare(
	"ppwcode.dojo.dijit.form.ImageBox",
	dijit.form._FormValueWidget,
	{
		imgSrc: "",
		
		templateString: "<img dojoAttachPoint='focusNode' src='${imgSrc}'></img>",
		
		_setValueAttr: function(newValue) {
			dojo.attr(this.domNode, "src", newValue);
			this.inherited(arguments);
		}
	}
);
		     