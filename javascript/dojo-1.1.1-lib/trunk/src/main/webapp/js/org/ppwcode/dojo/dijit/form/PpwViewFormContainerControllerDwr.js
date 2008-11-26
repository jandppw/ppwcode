dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewFormContainerControllerDwr");

dojo.require("dijit._Widget");
dojo.require("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewFormContainerControllerDwr",
	[dijit._Widget, org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr],
	{
		//summary:
		//    Documentation placeholder
		//description:
		//    Documentation placeholder
		view: null,
		formContainer: null,
		formContainerMap: null,
		
		_form: null,
		_view: null,
		_container: null,
		
		viewviewcontroller: null,
		
		_vieweventconnections: null,
		_formeventconnections: null,
		
		_typemap: null,
		
		constructor: function() {
			this._vieweventconnections = new Array();
			this._formeventconnections = new Array();
		},
		
		postCreate: function() {
			if ((this.view) && (this.formContainer) && (this.formContainerMap)) {
				this.configure(this.view, this.formContainer, this.formContainerMap);
			}
			if (this.viewviewcontroller) {
				this.setViewIsChild(this.viewviewcontroller);
			}
			this.inherited(arguments);
		},
		
		configure: function (/*PpwMasterView*/view,
				                           /*PpwCrudFormContainer*/formContainer,
				                           /*Array*/formContainerMap) {
			this._view = view;
			this._container = formContainer;
			this._typemap = new Object();
			for (var i = 0; i < formContainerMap.length; i++) {
				this._typemap[formContainerMap[i].type] = formContainerMap[i];
			}
			//connect to events from view
			this._vieweventconnections.push(dojo.connect(this._view, "onClearSelection", this, "_doViewClearSelection"));
			this._vieweventconnections.push(dojo.connect(this._view, "onSelectedRowUpdate", this, "_doViewSelectedRowUpdate"));
			this._vieweventconnections.push(dojo.connect(this._view, "onAddButtonClick", this, "_doViewAddButtonClick"));
			this._vieweventconnections.push(dojo.connect(this._view, "onGridRowClick", this, "_doViewGridRowClick"));
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
			dojo.mixin(this, org.ppwcode.dojo.dijit.form.PpwViewFormContainerControllerDwr.ChildController);
			this._viewviewcontroller = viewviewcontroller;
			//in case of a create or update, not we, but the viewviewcontroller will update our view
			//by calling view.setData.  We must hence listen to that event on our view, so we can act
			//correspondingly.  Note that the onViewSetData is normally not available, but mixed in
			//by the above code.
			dojo.connect(this._view, "onSetData", this, "onViewSetData");
		}

	}
);

org.ppwcode.dojo.dijit.form.PpwViewFormContainerControllerDwr.ChildController = {
		
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
