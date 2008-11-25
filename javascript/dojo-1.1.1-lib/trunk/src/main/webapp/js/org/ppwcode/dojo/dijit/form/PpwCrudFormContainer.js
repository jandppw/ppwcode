dojo.provide("org.ppwcode.dojo.dijit.form.PpwCrudFormContainer");

dojo.require("dijit.layout.StackContainer");
dojo.require("dijit.layout.ContentPane");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwCrudFormContainer",
	dijit.layout.StackContainer,
	{
		_formlist: null,

		startup: function() {
			this.inherited(arguments);
			// we assume that our children are content panes that contain
			// exactly one form.
			this._formlist = new Object();
			var children = this.getChildren();
			for (var i = 0; i < children.length; i++) {
				var child = children[i];
				var list = dojo.query("> [widgetId]", child.containerNode)
				dojo.forEach(list, function(item) {
						console.log("child: " + item + " with id " + dojo.attr(item, "id")); 
						this._formlist[dojo.attr(item, "id")] = child;
					}, this);
			}
			// add an empty pane
			var tmpnode = dojo.doc.createElement('div');
			var emptypage = new dijit.layout.ContentPane(null, tmpnode);
			emptypage.setAttribute("title", "emptyPane");
			// obey the rules
			emptypage.startup();
			//add it to the list
			this._formlist["__empty"] = emptypage;
			this.addChild(emptypage);
			//set it as active page. (apparently this does not happen when
			//the child is added, this is a bug in the stack container.
			this.clearDisplay(emptypage);
		},
		
		displayForm: function(/*String*/formid) {
			this.selectChild(this._formlist[formid]);
		},
		
		clearDisplay: function() {
			this.selectChild(this._formlist["__empty"]);
		}
	}
);