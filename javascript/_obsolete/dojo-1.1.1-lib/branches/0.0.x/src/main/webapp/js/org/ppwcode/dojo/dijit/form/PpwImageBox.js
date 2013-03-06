dojo.provide("org.ppwcode.dojo.dijit.form.PpwImageBox");

dojo.require("dijit._Templated");
dojo.require("dijit.form._FormWidget");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwImageBox",
	dijit.form._FormValueWidget,
	{
		imgSrc: "",
		
		templateString: "<img dojoAttachPoint='focusNode' src='${imgSrc}'></img>",
		
		setValue: function(newValue) {
			dojo.attr(this.domNode, "src", newValue);
			this.inherited(arguments);
		}
	}
);
		     