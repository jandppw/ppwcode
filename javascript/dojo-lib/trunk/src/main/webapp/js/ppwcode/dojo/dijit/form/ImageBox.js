dojo.provide("ppwcode.dojo.dijit.form.ImageBox");

dojo.require("dijit._Templated");
dojo.require("dijit.form._FormWidget");

dojo.declare(
	"ppwcode.dojo.dijit.form.ImageBox",
	dijit.form._FormValueWidget,
	{
		imgSrc: "",
		
		templateString: "<div dojoAttachPoint='focusNode'><img dojoAttachPoint='imgNode' src='${_blankGif}'></img></div>",
		
		_setValueAttr: function(newValue) {
			if (!newValue) {
				dojo.attr(this.imgNode, "src", this._blankGif);
			} else {
				dojo.attr(this.imgNode, "src", newValue);
			}
			this.inherited(arguments);
		}
	}
);
		     