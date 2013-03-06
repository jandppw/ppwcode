dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewViewControllerDwr");

dojo.require("dijit._Widget");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewViewControllerDwr",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	[dijit._Widget],
	{
		//summary:
		//    Connects and controls two PpwMasterViews, a child view and a
		//    parent view.  The parent view shows a list of objects (items),
		//    the child view shows a list of properties (i.e. a multivalued
		//    property) of the selected item in the parent view (referred to
		//    as the parent item).
		//description:
		//    The PpwViewViewControllerDwr has a double responsibility.
		//    First, it is a user interface controller.  Second it coordinates
		//    Ajax calls, which are realized by DWR.  The Ajax call deals
		//    with data synchronization between the parent and the child
		//    view.  This controller assumes that the data which must be
		//    displayed in the client view is not yet available on the
		//    web client; it is fetched from the server using a DWR call.
		//
		//    User interface wise, the controller merely monitors the
		//    selection and deselection events on the parent view and updates
		//    the parent view accordingly: clear the child view on
		//    deselection, execute a DWR call and update the child view
		//    on selection.
		//
		//    The DWR call that is executed is triggered automatically
		//    by a selection event on the parent view.  It is however also
		//    possible to trigger the updating of the child view manually
		//    by calling requestRefresh() on the PpwViewViewController, for
		//    instance when the application has modified an item in the child
		//    view, and the server must be contacted to resynchronize the child
		//    view.
		
		_parentView: null,
		_childView: null,
		
		parentView: null,
		childView: null,

		dwrRetrieveFunction: null,
		_dwrRetrieveFunctionAdditionalParameters: [],
		
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
			//summary:
			//    Set the parent and the child PpwMasterview.
			//description:
			//    Connects this controller to selection events in the parent
			//    PpwMasterView, allowing it to control the contents of the
			//    child view.
			this._parentView = parentview;
			this._childView = childview;
			//clear previous connections
			while (this._eventconnections.length > 0) {
				dojo.disconnect(this._eventconnections.pop());
			}
			this._eventconnections.push(dojo.connect(this._parentView, "onClear", this, "_doParentOnClear"));
			this._eventconnections.push(dojo.connect(this._parentView, "onSetData", this, "_doParentOnSetData"));
			this._eventconnections.push(dojo.connect(this._parentView, "onRefreshData", this, "_doParentOnRefreshData"));
			this._eventconnections.push(dojo.connect(this._parentView, "onGridRowClick", this, "_doParentOnGridRowClick"));
			this._eventconnections.push(dojo.connect(this._parentView, "onGridHeaderClick", this, "_doParentOnGridHeaderClick"));
			this._eventconnections.push(dojo.connect(this._parentView, "onSelectItemSuccess", this, "_doParentOnSelectItemSuccess"));
			this._eventconnections.push(dojo.connect(this._parentView, "onAddButtonClick", this, "_doParentOnAddButtonClick"));
			
			this._eventconnections.push(dojo.connect(this._childView, "onSelectItemFail", this, "_doChildOnSelectItemFail"));
			this._eventconnections.push(dojo.connect(this._childView, "onGridHeaderClick", this, "_doChildOnGridHeaderClick"));
		},

		_doParentOnClear: function() {
			this._childView.disableButtons(true);
			this._childView.clear();
		},
		
		_doParentOnSetData: function() {
			this._childView.disableButtons(true);
			this._childView.clear();
		},

		_doParentOnRefreshData: function() {
			this._childView.disableButtons(true);
			this._childView.clear();
		},
		
		_doParentOnGridRowClick: function() {
			this._childView.disableButtons(false);
			this.doFillChildView(this._parentView.getSelectedItem());
		},
		
		_doParentOnGridHeaderClick: function() {
			this._childView.disableButtons(true);
			this._childView.clear();
		},
		
		_doParentOnSelectItemSuccess: function() {
			this._childView.disableButtons(false);
			this.doFillChildView(this._parentView.getSelectedItem());
		},
		
		_doParentOnAddButtonClick: function() {
			this._childView.disableButtons(true);
			this._childView.clear();
		},
		
		_doChildOnSelectItemFail: function() {
			this._parentView.reSelectCurrentItem();
		},
		
		_doChildOnGridHeaderClick: function() {
			this._parentView.reSelectCurrentItem();
		},
		
		requestRefresh: function() {
			//summary:
			//    Request this controller to refresh the child view using
			//    the currently selected item in the parent view
			var parentitem = this._parentView.getSelectedItem();
			if (parentitem) {
				var selecteditem = this._childView.getSelectedItem();
				if (selecteditem) {
					this.doFillChildViewWithSelect(parentitem, selecteditem);
				} else {
					this.doFillChildView(parentitem);
				}
			}
		},
		
		setDwrRetrieveFunctionAdditionalParameters: function(/*Array*/params) {
			//summary:
			//    Set the parameters that must be passed along when retrieving
			//    the data for the child view.
			//description:
			//    Set the parameters that must be passed along when retrieving
			//    the data for the child view.  Note that this method is
			//    typically called from the beforeFillChildView() callback
			//    function.
			//params:
			//    An Array containing the parameter values (in the correct order
			//    of course
			this._dwrRetrieveFunctionAdditionalParameters = params;
		},
		
		doFillChildView: function(/*Object*/parentObject, /*Function?*/callback) {
			//summary:
			//    Method call that implements a DWR call to determine the
			//    contents of the child view.
			//description:
			//    Calls a DWR call that can be configured with the
			//    dwrRetrieveFunction attribute in the HTML tag that defines
			//    this widget.  All DWR bookkeeping (callback function
			//    configuration) is encapsulated in this widget.
			//parentObject:
			//    the object that is functions as the parent object, i.e.
			//    the to-one side of a one-to-many relation.
			var self = this;
			
			var mycallback = function(data) {
				self._childView.setData(data);
				self.afterFillChildView(data);
			}
			// overwrite if defined
			if (callback && dojo.isFunction(callback)) {
				mycallback = callback;
			}
			
			var myerrorhandler = function(errorString, exception) {
				self.doFillChildViewErrorHandler(errorString, exception);
			}
			
			this.beforeFillChildView(parentObject);
			
			var params = new Array();
			//Array.concat gives me hell, so we do this old skool :(
			for (var i = 0; i < this._dwrRetrieveFunctionAdditionalParameters.length; i++) {
				params.push(this._dwrRetrieveFunctionAdditionalParameters[i]);
			}
			params.push({callback: mycallback, errorHandler: myerrorhandler});
			//console.log("PpwViewViewControllerDWR: doFillChildView parameters");
			//for (var i = 0; i<params.length; i++) console.log("\t" + params[i])
			this.dwrRetrieveFunction.apply(this, params);
		},
		
		doFillChildViewWithSelect: function(/*Object*/parentObject, /*Object*/criterium) {
			var self = this;
			var mycallback = function(data) {
				self._childView.setData(data);
				self._childView.selectItem(criterium);
				self.afterFillChildView(data);
			}
			this.doFillChildView(parentObject, mycallback);
		},
		
		beforeFillChildView: function(parentObject) {
			//summary:
			//    overridable method that is called before the
			//    dwrRetrieveFunction is called.  This method exists mainly
			//    here to set additional parameters before the Retrieve
			//    function is executed.
			//parentObject:
			//    the item that is selected in the parent PpwMasterView.
		},
		
		afterFillChildView: function(data) {
			//summary:
			//    overridable method that is called after the
			//    dwrRetrieveFunction is called.
			//data:
			//    result of the DWR call
		},
		
		doFillChildViewErrorHandler: function(errorString, exception) {
			//summary:
			//    Override, as you should inform the user that refreshing
			//    the child PpwMasterView failed after performing an update.
			// description:
			//    Override, as you should inform the user that refreshing the
			//    child view probably failed after performing an update.
			//    (emphasis on probably, there could have occurred an error
			//    in the network communication while communicating the result
			//    to the client.
			//errorString:
			//    Error message.
			//exception:
			//    The exception that was thrown as a result of this method.
			//    Note that you must configure DWR to serialize Java
			//    exceptions back to the client.
		},
		
		_getParentSelectedItem: function() {
			return this._parentView.getSelectedItem();
		}
	}
);
