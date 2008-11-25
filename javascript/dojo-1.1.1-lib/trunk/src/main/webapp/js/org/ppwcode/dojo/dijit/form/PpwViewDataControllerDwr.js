dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewDataControllerDwr");

dojo.require("dijit._Widget");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewDataControllerDwr",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	dijit._Widget,
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
		},

		//----------------------------------------------------------------
		//                      RETRIEVE: Grid; datafill
		//----------------------------------------------------------------
		setDwrRetrieveFunctionParameters: function(/*Array*/params) {
			//summary:
			//    Set the parameters that must be passed when the retrieve
			//    DWR method is called.
			//description:
			//    Set the parameters that must be passed when the retrieve
			//    DWR method is called.  Parameters must be passed in an
			//    array.
			//params:
			//    DWR method parameters.
			this._dwrRetrieveFunctionParameters = params;
		},
		
		fillMasterView: function() {
			//summary:
			//    Instruct the PpwController to execute the configured 
			//    retrieve function and fill the PpwMasterView with the
			//    resulting data array.
			//description:
			//    This method encapsulates the creation (creating the method
			//    call and handling the callback) and execution of a DWR call
			//    that executes the configured retrieve method (using the
			//    dwrRetrieveFunction attribute in the tag that defines the
			//    controller widget).
			var self = this;
			
			var callback = function(data) {
				self._view.setData(data);
			}
			
			var errorhandler = function(errorString, exception) {
				self.fillMasterViewErrorHandler(errorString, exception);
			}
			
			var params = this._copyDwrRetrieveFunctionParameters();
			params.push({callback: callback, errorHandler: errorhandler});
			this.dwrRetrieveFunction.apply(this, params);
			
		},
		
		fillMasterViewErrorHandler: function(errorString, exception) {
			//summary:
			//    override if you want to do something in case an error occurs
			//    during the execution of the DWR retrieve method.
			//errorString:
			//    Error message.
			//exception:
			//    The exception that was thrown as a result of this method.
			//    Note that you must configure DWR to serialize exceptions
			//    back to the client.
			console.log("fillMasterViewErrorHandler, please override: " + errorString);
		}
	}
);