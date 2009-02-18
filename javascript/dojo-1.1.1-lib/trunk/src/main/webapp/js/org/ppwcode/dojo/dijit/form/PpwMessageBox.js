dojo.provide("org.ppwcode.dojo.dijit.form.PpwMessageBox");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare("org.ppwcode.dojo.dijit.form.PpwMessageBox",
	[dijit._Widget, dijit._Templated],
	{
		templateString: "<div class='PpwMessageBox'></div>",

		busyicon: dojo.moduleUrl("org.ppwcode.dojo.dijit.form", "resources/icons/loading.gif"),
		
		buildRendering: function() {
			this.inherited(arguments);
			this._clearContent();
		},
	
		_clearContent: function() {
			this.domNode.innerHTML="&nbsp;";
		},

		clear: function() {
			this._clearContent();
			this._removeClasses();
		},
		
		setMessage: function(htmlcontent) {
			this.clear();
			dojo.addClass(this.domNode, "PpwMessageBoxMessage");
			this.domNode.innerHTML = htmlcontent;
		},
		
		setError: function(htmlcontent) {
			this.clear();
			dojo.addClass(this.domNode, "PpwMessageBoxError");
			this.domNode.innerHTML = htmlcontent;
		},
		
		setLoading: function() {
			this.clear();
			dojo.addClass(this.domNode, "PpwMessageBoxLoading");
			this.domNode.innerHTML="<img src='" + this.busyicon + "'></img>";
		},
		
		_removeClasses: function() {
			dojo.removeClass(this.domNode, "PpwMessageBoxError");
			dojo.removeClass(this.domNode, "PpwMessageBoxMessage");
			dojo.removeClass(this.domNode, "PpwMessageBoxLoading");
		}
	}
);