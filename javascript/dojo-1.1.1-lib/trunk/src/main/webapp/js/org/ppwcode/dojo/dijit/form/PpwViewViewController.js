dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewViewController");

dojo.require("dijit._Widget");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewViewController",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	[dijit._Widget],
	{
		_parentView: null,
		_childView: null,
		
		parentView: null,
		childView: null,
		
		_eventconnections: null,
		
		constructor: function() {
			this._eventconnections = new Array();
		},
		
		postCreate: function() {
			//var theview, themodel;
			//only do this when the dojo parser set the view and form properties
			//(code taken from Grid.js: this is probably too liberal, the view and
			// model should exist, and should not be created in here.)
			if((this.parentView) && (this.childView)) {
				this.setViews(this.parentView, this.childView);
			}
			this.inherited(arguments);
		},

		
		setViews: function(parentview, childview) {
			this._parentView = parentview;
			this._childView = childview;
			//clear previous connections
			while (this._eventconnections.length > 0) {
				dojo.disconnect(this._eventconnections.pop());
			}
			this._eventconnections.push(dojo.connect(this._parentView, "onClearSelection", this, "_doClearSelection"));
			this._eventconnections.push(dojo.connect(this._parentView, "onGridRowClick", this, "_doFillChildView"));
		},

		_doClearSelection: function() {
			this._childView.setAddButtonDisabled(true);
			this._childView.clearSelection();
			this._childView.setData([]);
		},
		
		_doFillChildView: function() {
			this._childView.setAddButtonDisabled(false);
			this._childView.clearSelection();
			this.doFillChildView(this._parentView.getSelectedRow());
		},
		
		_getParentSelectedRow: function() {
			return this._parentView.getSelectedRow();
		},
		
		requestRefresh: function() {
			if (this._parentView.getSelectedRow()) {
				this._doFillChildView();
			}
		},
		
		doFillChildView: function(parentObject) {
			console.log("PpwViewViewController:  please override doFillChildView()");
			//override
		},
		
		doFillChildViewCallback: function(data) {
			this._childView.setData(data);
		},
		
		doFillChildViewErrorHandler: function(errorString, exception) {
			//what to do, what to do
		}
	}
);
