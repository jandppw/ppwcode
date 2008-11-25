dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr");

dojo.require("dijit._Widget");
dojo.require("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	[dijit._Widget, org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr],
	{
		//summary:
		//    A PpwViewFormControllerDwr is a user interface controller that
		//    coordinates CRUD behavior between a PpwMasterView and a
		//    PpwCrudForm.  
		//description:
		//    The PpwViewFormControllerDwr has a double function.  First, it is
		//    a user interface controller.  Second it coordinates a number
		//    of XHR calls, which are realized by DWR.  These DWR calls solely
		//    deal with creating, retrieving, updating and deleting items
		//    in an application (CRUD).  At the time of writing, there is no
		//    support yet for deleting objects.
		//
		//    Although a PpwCrudForm and a PpwMasterView can be used as
		//    separate user interface widgets, the PpwViewFormControllerDwr
		//    glues them together, so both widgets are perceived as a single
		//    widget.  Concretely, this means the following.
		//    * Clicking the Add button in the PpwMasterView clears the
		//      selection in the PpwMasterView and puts the PpwCrudForm in
		//      create mode.
		//    * Selecting a row in the PpwMasterView displays the selected
		//      Item in the PpwCrudForm, and puts the form in Viewing mode
		//      (Edit button and optionally a Delete button)
		//    * Clicking the Delete button in the PpwCrudForm starts the
		//      Delete scenario (see later).
		//    * Clicking the Save button in the PpwCrudForm in Edit mode
		//      starts the Update Scenario (see later.)
		//    * Clicking the Cancel button resets the form with the values
		//      of the selected item it the PpwMasterView
		//    * Clicking the Save button in the PpwCrudForm in Create mode
		//      starts the Create Scenario (see later.)
		//    * Clicking the Cancel button resets the form (no values, input
		//      fields disabled, no buttons).
		//
		//    Additionally, if a PpwViewFormControllerDwr can function as a
		//    child controller. This means that the View this
		//    PpwViewFormControllerDwr is controlling is displaying properties
		//    of a parent item. Typically, this child view shows many side of
		//    a one-to-many relationship with the parent item.  This has
		//    consequences on how the retrieve scenario is performed for the
		//    child controller. Configuring a PpwViewFormControllerDwr is done
		//    by calling the setViewIsChild() function.
		//   
		//    The second responsibility of the PpwViewFormControllerDwr is
		//    coordinating communication between the web client and the server
		//    when a CRUD action is performed.  The PpwViewFormControllerDwr
		//    uses DWR as communication medium, and encapsulates all DWR
		//    specific bookkeeping.  For each CRUD operation a scenario exists
		//    although at the time of writing there is no scenario yet for
		//    deletion.
		//
		//    The Retrieve scenario consists of performing a DWR method call.
		//    The result should be an array of items (typically realized by a
		//    Java Collection which is passed as an array of javascript
		//    objects by DWR). which is passed as is to the MasterView's
		//    Grid Dataset.  The name of the DWR method to call can be set
		//    using the dwrRetrieveFunction attribute of the HTML tag that
		//    defines this controller.  The retrieve scenario is usually user
		//    interface specific (for instance, fill out a search form) and
		//    must be triggered explicitly by the application by calling
		//    fillMasterView() on the controller. Setting the parameters of
		//    this method call is done using the
		//    setDwrRetrieveFunctionParameters() function on this controller,
		//    which must obviously be called prior to fillMasterView().
		//
		//    The Update and Create Scenario are similar to each other.  Both
		//    are triggered by clicking the Update or Create button in the
		//    PpwCrudForm.  Both scenarios consist of 3 steps.  Step 1 is
		//    updating or creating the item.  Step 2 is refreshing the
		//    PpwMasterView.  Step 3 is selecting the update or created item
		//    in the PpwMasterView (provided it is in there).  Only the first
		//    two steps execute DWR calls.
		//    
		//    Creating or Updating the item (step 1) is done by calling the
		//    create or update DWR function which can be configured using the
		//    dwrUpdateFunction and dwrCreateFunction respectively (attributes
		//    on the controllers HTML tag).  At the time of writing, the 
		//    controller assumes that the first (and only) parameter to these
		//    functions is the item that must be updated or created
		//    respectively.  When the callback function of this first DWR
		//    method is called, the second step is executed.  The error
		//    function should be overridden by the application developer to
		//    notify the end user of the error.  Step 1 also provides 2 hooks,
		//    one before the update or create is performed, and one after the
		//    update or create was performed successfully.
		//
		//    Step 2 consists of refreshing the PpwMasterView, which is done by
		//    calling the dwrRetrieveFunction.  In this step, the necessary
		//    callbacks are available to change the parameters of the retrieve
		//    function if desired.
		//
		//    Step 3 selects the item the PpwMasterView that adheres to a
		//    selection criterium.  The selection criterium is determined by
		//    the properties that are tagged as "isId" in the PpwCrudForm's
		//    formmap.  We refer to the documentation for an in depth
		//    understanding of this automatic selection process.
		
		//these should be set only through the dojo parser
		view: null,
		form: null,
		viewviewcontroller: null,
		
		_form: null,
		_view: null,

		_viewIsChild: false,
		
		_eventconnections: null,
		
		constructor: function() {
			this._eventconnections = [];
		},	

		postCreate: function() {
			// only do this when the dojo parser sets the view and form
			// properties.  This can be done by setting the form and
			// view attributes on the tag that defines this widget.
			if ((this.view) && (this.form)) {
				this.setViewAndGrid(this.view, this.form);
			}
			if (this.viewviewcontroller) {
				this.setViewIsChild(this.viewviewcontroller);
			}
			this.inherited(arguments);
		},
		
		setViewAndGrid: function(view, form) {
			//summary:
			//    Set the PpwMasterView and the PpwCrudForm that this
			//    controller will be controlling.
			//description:
			//    This method wires events that are fired by the
			//    PpwMasterView and PpwCrudForm to this controller
			//    using dojo.connect().
			this._view = view;
			this._form = form;
			//clear previous connections
			while (this._eventconnections.length > 0) {
				dojo.disconnect(this._eventconnections.pop());
			}
			//connect to update and create events from the form
			this._eventconnections.push(dojo.connect(this._form, "onUpdateModeSaveButtonClick", this, "_doItemUpdate"));
			this._eventconnections.push(dojo.connect(this._form, "onCreateModeSaveButtonClick", this, "_doItemCreate"));
			//connect to events from view
			this._eventconnections.push(dojo.connect(this._view, "onClearSelection", this, "_doViewClearSelection"));
			this._eventconnections.push(dojo.connect(this._view, "onSelectedRowUpdate", this, "_doViewSelectedRowUpdate"));
			this._eventconnections.push(dojo.connect(this._view, "onAddButtonClick", this, "_doViewAddButtonClick"));
			this._eventconnections.push(dojo.connect(this._view, "onGridRowClick", this, "_doViewGridRowClick"));
			//this._eventconnections.push(dojo.connect(this._view, "onGridHeaderClick", this, "_doViewGridHeaderClick"));
		},

		setViewIsChild: function(viewviewcontroller) {
			//summary:
			//    Configure this controller as a child ViewFormController in case
			//    multiple views are preset in the user interface.  The second
			//    view can for instance be used to display a one to many
			//    relation with the object in the first view.
			//description:
			//    Configure this controller as a child controller.  This means
			//    that updates for the child view are determined by what happens
			//    with the parent view (selects, refreshes, ...).  To be able
			//    to function as a child controller, the ViewFormController must
			//    be configured with a ViewViewController.  TODO:  It should be
			//    possible to pass in a PpwMasterView as parameter as well.
			//viewviewcontroller:
			//    The viewviewcontroller that coordinates the behavior between
			//    a parent PpwMasterView and a child PpwMasterView.
			this._viewIsChild = true;
			this._view.setAddButtonDisabled(true);
			//from now on, all grid refreshes are delegated to the viewviewcontroller, both in the
			//case of creates and updates
			dojo.mixin(this, org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr.ChildController);
			this._viewviewcontroller = viewviewcontroller;
			//in case of a create or update, not we, but the viewviewcontroller will update our view
			//by calling view.setData.  We must hence listen to that event on our view, so we can act
			//correspondingly.  Note that the onViewSetData is normally not available, but mixed in
			//by the above code.
			dojo.connect(this._view, "onSetData", this, "onViewSetData");
		},


        doViewAddButtonClick: function() {
            // method that can be overriden
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
		
	}
);

org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr.ChildController = {
	
	_viewviewcontroller: null,
		
	//mixing in these properties overwrites the default implementations
	_doMasterViewDataRefreshAfterUpdate: function() {
		//console.log("refresh in child");
		this._viewviewcontroller.doFillChildView(this._viewviewcontroller._getParentSelectedRow());
	},

	_doMasterViewDataRefreshAfterCreate: function() {
		//console.log("refresh in child");
		this._viewviewcontroller.doFillChildView(this._viewviewcontroller._getParentSelectedRow());
	},

	onViewSetData: function() {
		//summary:
		//    In case of child view, we respond to setData events.  These are
		//    done by the viewviewcontroller.  If this happens, we select the
		//    object that was subject to a create or update operation.
		this._doSelectItem();
	}
}

