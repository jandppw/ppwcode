dojo.provide("ppwcode.dojo.dijit.form.ImageBox");

dojo.require("dijit._Templated");
dojo.require("dijit.form._FormWidget");

dojo.declare(
	"ppwcode.dojo.dijit.form.ImageBox",
	dijit.form._FormValueWidget,
	{
		constructor: function() {
			console.log("Imagebox, blank Gif:" + this._blankGif);
		},
		
		imgSrc: "",
		
		templateString: "<div dojoAttachPoint='focusNode'><img dojoAttachPoint='imgNode' src='${_blankGif}'></img></div>",
		
		_setValueAttr: function(newValue) {
			dojo.attr(this.imgNode, "src", newValue);
			this.inherited(arguments);
		}
	}
);
		     