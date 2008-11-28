dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewDataControllerDwr");

dojo.require("dijit._Widget");
dojo.require("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewDataControllerDwr",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	[dijit._Widget, org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr],
	{
		
		_view: null,
		
		//these should be set only through the dojo parser
		view: null,
		
		dwrRetrieveFunction: null,
		_dwrRetrieveFunctionParameters: [],
		
		_viewIsChild: false,
		
		_eventconnections: null,
		
		constructor: function() {
			this._eventconnections = [];
		},	

		postCreate: function() {
			// only do this when the dojo parser sets the view and form
			// properties.  This can be done by setting the form and
			// view attributes on the tag that defines this widget.
			if (this.view) {
				this.setView(this.view, this.form);
			}
			this.inherited(arguments);
		},
		
		setView: function(view) {
			//summary:
			//    Set the PpwMasterView and the PpwCrudForm that this
			//    controller will be controlling.
			//description:
			//    This method wires events that are fired by the
			//    PpwMasterView and PpwCrudForm to this controller
			//    using dojo.connect().
			this._view = view;
			//clear previous connections
			while (this._eventconnections.length > 0) {
				dojo.disconnect(this._eventconnections.pop());
			}
		},

		_doViewClearSelection: function() {
			this._form.reset();
		},
		
		//View; Add object
		_doViewAddButtonClick: function(e) {
			this._form.createObject();
            this.doViewAddButtonClick(e);
		},
		
		//View; called when someone updated the item in the selected row in 
		//the PpwMasterView.
		_doViewSelectedRowUpdate: function() {
			this._form.displayObject(this._view.getSelectedRow());
		},
		
		//View; Click row
		_doViewGridRowClick: function(e) {
			this._form.displayObject(this._view.getSelectedRow());
		},
		
		//View; Click header
		_doViewGridHeaderClick: function(e) {
			this._form.reset();
		},
		
		//selectioncriterium is used to automatically select a certain
		//row in the PpwMasterView.  This is done, for example, after
		//refreshing the contents of the PpwMasterView.
		_selectioncriterium: null,

		_copyDwrRetrieveFunctionParameters: function() {
			var params = new Array();
			for (var i = 0; i < this._dwrRetrieveFunctionParameters.length; i++) {
				params[i] = this._dwrRetrieveFunctionParameters[i];
			}
			return params;
		}
	}
);