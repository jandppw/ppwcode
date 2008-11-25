dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewFormController");

dojo.require("dijit._Widget");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewFormController",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	dijit._Widget,
	{
		_view: null,
		_form: null,
		
		//these should be set only through the dojo parser
		view: null,
		form: null,
		viewviewcontroller: null,
		
		_viewIsChild: false,
		
		_eventconnections: null,
		
		constructor: function() {
			this._eventconnections = [];
		},	

		postCreate: function() {
			//var theview, themodel;
			//only do this when the dojo parser set the view and form properties
			//(code taken from Grid.js: this is probably too liberal, the view and
			// model should exist, and should not be created in here.)
			if ((this.view) && (this.form)) {
				//look up of instantiate the view
				/*
				var v = this.view;
				if(dojo.isString(v)){
					v = dojo.getObject(v);
				}
				theview = (dojo.isFunction(v)) ? new v() : v;
				
				var f = this.form;
				if (dojo.isString(f)) {
					f = dojo.getObect(f);
				}
				theform = (dojo.isFunction(f)) ? new f() : f;
				*/
				//let's assume that we've got 2 objects now, if not, we would have run
				//into a programming error by now, and that should never occur
				this.setViewAndGrid(this.view, this.form);
			}
			if (this.viewviewcontroller) {
				this.setViewIsChild(this.viewviewcontroller);
			}
			this.inherited(arguments);
		},
		
		setViewAndGrid: function(view, form) {
			this._view = view;
			this._form = form;
			//clear previous connections
			while (this._eventconnections.length > 0) {
				dojo.disconnect(this._eventconnections.pop());
			}
			//connect to update and create events from the form
			this._eventconnections.push(dojo.connect(this._form, "onUpdateModeSaveButtonClick", this, "_doFormObjectUpdate"));
			this._eventconnections.push(dojo.connect(this._form, "onCreateModeSaveButtonClick", this, "_doFormObjectCreate"));
			//connect to events from view
			this._eventconnections.push(dojo.connect(this._view, "onClearSelection", this, "_doViewClearSelection"));
			this._eventconnections.push(dojo.connect(this._view, "onSelectedRowUpdate", this, "_doViewSelectedRowUpdate"));
			this._eventconnections.push(dojo.connect(this._view, "onAddButtonClick", this, "_doViewAddButtonClick"));
			this._eventconnections.push(dojo.connect(this._view, "onGridRowClick", this, "_doViewGridRowClick"));
			//this._eventconnections.push(dojo.connect(this._view, "onGridHeaderClick", this, "_doViewGridHeaderClick"));
		},

		setViewIsChild: function(viewviewcontroller) {
			this._viewIsChild = true;
			this._view.setAddButtonDisabled(true);
			//from now on, all grid refreshes are delegated to the viewviewcontroller, both in the
			//case of creates and updates
			dojo.mixin(this, org.ppwcode.dojo.dijit.form.PpwViewFormController.ChildController);
			this._viewviewcontroller = viewviewcontroller;
			//in case of a create or update, not us, but the viewviewcontroller will update our view
			//by calling view.setData.  So now we listen to that event on our view, so we can act
			//correspondingly.  Note that the onViewSetData is normally not available, but mixed in
			//by the above code.
			dojo.connect(this._view, "onSetData", this, "onViewSetData");
		},
		
		//----------------------------------------------------------------
		//                       Form; Update object
		//----------------------------------------------------------------
		
		_doFormObjectUpdate: function(e) {
			//console.log("This is only the beginning of an update action...");
			this.doFormObjectUpdate(e.formObject);
		},
		
		doFormObjectUpdate: function(obj) {
			//console.log("to override");
			//override function.
		},
		
		
		//----------------------------------------------------------------
		//                       Form; Update object
		//----------------------------------------------------------------

		_doFormObjectCreate: function(e) {
			//console.log("This is only the beginning of a create action...");
			this.doFormObjectCreate(e.formObject);
		},
		
		doFormObjectCreate: function(obj) {
			//override function
		},
		
		//What to do when someone asks the form to clear the selection
		//TODO shouldn't objects ask the controller to clear the selection?  if so,
		//what if the PpwMasterView is used in isolation,  For now, clearing the selection
		//is done on the PpwMasterView and the controller connects to the event.
		
		_doViewClearSelection: function() {
			this._form.reset();
		},
		
		//View; Add object
		_doViewAddButtonClick: function(e) {
			this._form.createObject();
		},
		
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
		
		
		_selectioncriterium: null,

		////////////////////////////////////////////////////////////////////
		//                           UPDATE
		////////////////////////////////////////////////////////////////////
		
		
		/*
		 * step 1: perform the update
		 */
		doFormObjectUpdate: function(obj) {
			//override function.
			//console.log("PpwViewformController: please override doFormObjectUpdate");
			this.doFormObjectUpdateCallback(obj);
		},

		doFormObjectUpdateCallback: function(updatedobject) {
			//console.log("doFormObjectUpdateCallback called");
			//determine the selection criterium, so we can select the object again
			//after the grid is refreshed.
			var idfields = this._form.getObjectIdFields();
			this._selectioncriterium = new Object();
			for (var i = 0; i < idfields.length; i++) {
				this._selectioncriterium[idfields[i]] = updatedobject[idfields[i]];
			}
			//refresh the grid
			this.doViewDataRefreshAfterUpdate();
			//refresh view and select updatedobject
		},

		doFormObjectUpdateErrorHandler: function(errorString, exception) {
			//override, as you should probably show something to the user.
			//what to do, what to do...
		},


		/*
		 * step 2: grid refresh
		 */
		// override
		doViewDataRefreshAfterUpdate: function() {
			//console.log("doViewDataRefreshAfterUpdate (original) called");
			this.doViewDataRefreshAfterUpdateCallback(this._view._masterGrid.model.data);
		},

		doViewDataRefreshAfterUpdateCallback: function(data) {
			this._view.setData(data);
			this._doSelectObject();
		},

		doViewDataRefreshAfterUpdateErrorHandler: function(errorString, exception) {
			//override, as you should probably show something to the user.
			//what to do, what to do...
		},


		/*
		 * step 3: select the updated object
		 */

		_doSelectObject: function() {
			this._view.clearSelection();
			if (this._selectioncriterium) {
				this._view.selectObject(this._selectioncriterium);
				this._selectioncriterium = null;
			}
		},


		////////////////////////////////////////////////////////////////////
		//                          CREATE
		////////////////////////////////////////////////////////////////////
		
		/*
		 * step 1: perform the create
		 */	
		doFormObjectCreate: function(obj) {
			this.doFormObjectCreateCallback(obj);
		},

		doFormObjectCreateCallback: function(createdobject) {
			var idfields = this._form.getObjectIdFields();
			this._selectioncriterium = new Object();
			for (var i = 0; i < idfields.length; i++) {
				this._selectioncriterium[idfields[i]] = createdobject[idfields[i]];
			}
			this.doViewDataRefreshAfterCreate(createdobject);
			//refresh view and select updatedobject
		},

		doFormObjectCreateErrorHandler: function(errorString, exception) {
			//what to do, what to do
		},

		/*
		 * step 2: grid refresh
		 */
		// override
		doViewDataRefreshAfterCreate: function(createdobject) {
			this.doViewDataRefreshAfterCreateCallback(this._view._masterGrid.model.data);
		},

		doViewDataRefreshAfterCreateCallback: function(data) {
			this._view.setData(data);
			this._doSelectObject();
		},

		doViewDataRefreshAfterCreateErrorHandler: function(errorString, exception) {
			//what to do, what to do...
		}

	
		/*
		 * step 3: select the created object
		 */
		// select according to selection criterium:  identical to update scenario
	}
);

org.ppwcode.dojo.dijit.form.PpwViewFormController.ChildController = {
	_viewviewcontroller: null,
	
	//child View Form controllers pass along the parent object to doFormObjectUpdate
	_doFormObjectUpdate: function(e) {
		//console.log("This is only the beginning of a child update action...");
		this.doFormObjectUpdate(e.formObject, this._viewviewcontroller._getParentSelectedRow());
	},

	//child View Form controllers pass along the parent object to doFormObjectCreate
	_doFormObjectCreate: function(e) {
		//console.log("This is only the beginning of a child create action...");
		this.doFormObjectCreate(e.formObject, this._viewviewcontroller._getParentSelectedRow());
	},

	//mixing in these properties overwrites the default implementations
	doViewDataRefreshAfterUpdate: function() {
		//console.log("refresh in child");
		this._viewviewcontroller._doFillChildView();
	},

	doViewDataRefreshAfterCreate: function() {
		//console.log("refresh in child");
		this._viewviewcontroller._doFillChildView();
	},

	//in case of child view, we respond to setData events.  These are done by
	//the viewviewcontroller.  If this happens, we select the object that
	//was subject to a create or update operation
	onViewSetData: function() {
		this._doSelectObject();
	}
}

