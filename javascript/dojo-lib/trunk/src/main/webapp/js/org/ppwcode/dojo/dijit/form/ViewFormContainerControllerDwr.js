dojo.provide("org.ppwcode.dojo.dijit.form.ViewFormContainerControllerDwr");

dojo.require("dijit._Widget");
dojo.require("org.ppwcode.dojo.dijit.form._ViewFormCrudScenariosDwr");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.ViewFormContainerControllerDwr",
	[dijit._Widget, org.ppwcode.dojo.dijit.form._ViewFormCrudScenariosDwr],
	{
		//summary:
		//    Documentation placeholder
		//description:
		//    Documentation placeholder
		view: null,
		formContainer: null,

		//array of form ID's that can be requested by this controller
		controlledForms: [],
		//map of objects, contains
		// * formid,
		// * constructorFunction 
		// * objectName
		// for each form controlled by this controller. Key in the map
		// is the _name_ (string) of the constructorfunction
		_controlledFormsMap: null,
		
		_form: null,
		_view: null,
		_container: null,
		
		viewviewcontroller: null,

		_vieweventconnections: null,
		_formeventconnections: null,
		
		constructor: function() {
			this._vieweventconnections = new Object();
			this._formeventconnections = new Array();
		},
		
		startup: function() {
			if ((this.view) && (this.formContainer)) {
				this.configure(this.view, this.formContainer, this.viewviewcontroller ? this.viewviewcontroller : null);
			}
			this.inherited(arguments);
		},

		uninitialize: function() {
			this._disconnectEventHandlers();
			this.inherited(arguments);
		},
		
		_connectEventHandlers: function() {
			//connect to events from view
			this._vieweventconnections["onClear"] = dojo.connect(this._view, "onClear", this, "_doViewOnClear");
			this._vieweventconnections["onGridRowClick"] = dojo.connect(this._view, "onGridRowClick", this, "_doViewGridRowClick");
			this._vieweventconnections["onGridHeaderClick"] = dojo.connect(this._view, "onGridHeaderClick", this, "_doViewGridHeaderClick");
			this._vieweventconnections["onAddButtonClick"] = dojo.connect(this._view, "onAddButtonClick", this, "_doViewOnAddButtonClick");
			this._vieweventconnections["onSelectItemSuccess"] = dojo.connect(this._view, "onSelectItemSuccess", this, "_doViewOnSelectItemSuccess");
			this._vieweventconnections["onReSelectCurrentItemSuccess"] = dojo.connect(this._view, "onReSelectCurrentItemSuccess", this, "_doViewOnReSelectCurrentItemSuccess");
			if (!this._viewIsChild) {
				this._vieweventconnections["onSetData"] = dojo.connect(this._view, "onSetData", this, "_doViewOnSetData");
				this._vieweventconnections["onSelectItemFail"] = dojo.connect(this._view, "onSelectItemFail", this, "_doViewOnSelectItemFail");
			}
		},
		
		_disconnectEventHandlers: function() {
			for (var key in this._vieweventconnections) {
				dojo.disconnect(this._vieweventconnections[key]);
				delete this._vieweventconnections[key];
			}
		},
		
		configure: function (/*MasterView*/view,
				             /*CrudFormContainer*/formContainer,
				             /*ViewViewController?*/viewviewcontroller) {
			this._view = view;
			this._container = formContainer;

			this._setControlledForms();
			this._connectEventHandlers();

			if (viewviewcontroller) {
				this.setViewIsChild(viewviewcontroller);
			}
		},
		
		_createControlledFormsMap: function(formslist) {
			this._controlledFormsMap = new Object();
			for (var i = 0; i < formslist.length; i++) {
				var ctorname = org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(formslist[i].constructorFunction);
				this._controlledFormsMap[ctorname] = formslist[i];
				this._controlledFormsMap[ctorname].constructorFunctionName = ctorname;
			}
		},
		
		_createButtonData: function() {
			var buttondata = new Array(), i = 0;
			for (var ctorname in this._controlledFormsMap) {
				buttondata[i] = new Object();
				buttondata[i].label = this._controlledFormsMap[ctorname].objectName;
				buttondata[i++].value = this._controlledFormsMap[ctorname].constructorFunctionName;
			}
			return buttondata;
		},
		
		_setControlledForms: function() {
			if (this.controlledForms.length > 0) {
				var formslist = this._container.getFormsList();
				var result = new Array();
				//match and filter the formslist from the container
				for (var i = 0; i < this.controlledForms.length; i++) {
					for (var j = 0, found = false; !found && (j < formslist.length); j++) {
						if (this.controlledForms[i] == formslist[j].formId) {
							found = true;
							result.push(formslist[j]);
						}
					}
				}
				if (result.length > 0) {
					this._createControlledFormsMap(result);
				}
				if (result.length > 1) {
					//more than one form for this controller, so we configure
					//the masterview with a multibutton.
					this._view.setMultiButton(this._createButtonData());
				}
			} else {
				this._createControlledFormsMap(this._container.getFormsList());
				this._view.setMultiButton(this._createButtonData());
			}
		},
		
		setControlledForms: function(/*Array of strings*/formlist) {
			this.controlledForms = formlist;
			this._setControlledForms();
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
			//    possible to pass in a MasterView as parameter as well.
			//viewviewcontroller:
			//    The viewviewcontroller that coordinates the behavior between
			//    a parent MasterView and a child MasterView.
			this._viewIsChild = true;
			this._view.disableButtons(true);
			//from now on, all grid refreshes are delegated to the viewviewcontroller, both in the
			//case of creates and updates
			this._disconnectEventHandlers();
			dojo.mixin(this, org.ppwcode.dojo.dijit.form.ViewFormContainerControllerDwr.ChildController);
			this._connectEventHandlers();
			this._viewviewcontroller = viewviewcontroller;
			//in case of a create or update, not we, but the viewviewcontroller will update our view
			//by calling view.setData.  We must hence listen to that event on our view, so we can act
			//correspondingly.  Note that the onViewSetData is normally not available, but mixed in
			//by the above code.
			//dojo.connect(this._view, "onSetData", this, "onViewSetData");
		},

		_doViewOnClear: function() {
			this._clearFormEventConnections();
			this._clearContainer();
		},
		
		_doViewOnSetData: function() {
			this._clearFormEventConnections();
			this._clearContainer();
		},
		
		_displaySelectedItemInFormContainer: function() {
			//remove the event connections to the buttons on the currently
			//displaying form
			this._clearFormEventConnections();
			// get the selected item and the form corresponding with its
			// constructor
			var item = this._view.getSelectedItem();
			var theform = this._container.getFormForConstructor(org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(item.constructor));
			
			if (theform) {
				//if there is a form, connect to its buttons and display the object
				this._formeventconnections.push(dojo.connect(theform, "onUpdateModeSaveButtonClick", this, "_doItemUpdate"));
				this._container.displayItem(this._view.getSelectedItem());
				this._form = theform;
			} else {
				// otherwise clear the form
				// TODO: maybe a fatal error should occur here if this happens
				this._clearContainer();
			}
		},
		
		_doViewGridRowClick: function() {
			this._displaySelectedItemInFormContainer();
		},

		_doViewGridHeaderClick: function() {
			this._clearFormEventConnections();
			this._clearContainer();
		},

		_doViewOnAddButtonClick: function(event) {
			//console.log("ViewFormContainerController::_doViewOnAddButtonClick with addButtonChooserValue \"" + event.addChooserValue + "\"")
			this._clearFormEventConnections();
			var prototype = null;
			var constructorFunctionName = null;
			if (event.addChooserValue) {
				constructorFunctionName = event.addChooserValue;
				prototype = new this._controlledFormsMap[event.addChooserValue].constructorFunction();
			} else {
				// in this case, there should only be one entry in the controlledFormsMap
				// but I see no other way to get it out of a map)
				for (var ctorname in this._controlledFormsMap) {
					prototype = new this._controlledFormsMap[ctorname].constructorFunction();
					constructorFunctionName = this._controlledFormsMap[ctorname].constructorFunctionName;
				}
			}
			this.doInitializePrototypeBeforeCreate(prototype);
			var theform = this._container.getFormForConstructor(constructorFunctionName);
			if (theform) {
				//if there is a form, connect to its buttons and display the object
				this._formeventconnections.push(dojo.connect(theform, "onCreateModeSaveButtonClick", this, "_doItemCreate"));
				this._formeventconnections.push(dojo.connect(theform, "onCreateModeCancelButtonClick", this, "_doCancelAction"));
				this._container.createObject(prototype);
				this._form = theform;
			} else {
				this._clearContainer();
			}
		},
		
		doInitializePrototypeBeforeCreate: function(prototype) {
			//summary:
			//   initialize the object that is to be created (on the server)
			//   before it can be edited by the user in the form.
			
			//override
		},
		
		_doViewOnSelectItemSuccess: function() {
			this._displaySelectedItemInFormContainer();
		},
		
		_doViewOnSelectItemFail: function() {
			this._clearContainer();
		},
		
		_doViewOnReSelectCurrentItemSuccess: function() {
			//viewform(container)controller interprets this the same as
			//normal click on the masterview's row
			this._displaySelectedItemInFormContainer();
		},
		
		_doViewOnReselectItemFail: function() {
			throw new Error("Failed to reselect on parent masterview, should never occur.");
		},
		
		_doCancelAction: function(e) {
			this._clearFormEventConnections();
			this._clearContainer();
		},
		
		_clearFormEventConnections: function() {
			while (this._formeventconnections.length > 0) {
				dojo.disconnect(this._formeventconnections.pop());
			}
		},
		
		_clearContainer: function() {
			this._container.clear();
			this._form = null;
		},
		
        // DWR Scenario hooks 
        _doItemUpdateErrorHandlerHook: function(errorString, exception) {
			this._container.setCurrentFormInUpdateMode();
        },
        
        _doItemCreateErrorHandlerHook: function(errorString, exception) {
			this._container.setCurrentFormInCreateModeNoReset();
        }
	}
);

org.ppwcode.dojo.dijit.form.ViewFormContainerControllerDwr.ChildController = {
		
		_viewviewcontroller: null,
		
		//mixing in these properties overwrites the default implementations
		_doMasterViewDataRefreshAfterUpdate: function() {
			//console.log("refresh in child");
			this._viewviewcontroller.doFillChildViewWithSelect(this._viewviewcontroller._getParentSelectedItem(), this._selectioncriterium);
		},

		_doMasterViewDataRefreshAfterCreate: function() {
			//console.log("refresh in child");
			this._viewviewcontroller.doFillChildViewWithSelect(this._viewviewcontroller._getParentSelectedItem(), this._selectioncriterium);
		},

		_doViewGridHeaderClick: function() {
			this._clearFormEventConnections();
		},
		
		_doViewOnClear: function() {
			this._clearFormEventConnections();
		},
		
		_doCancelAction: function() {
			this._clearFormEventConnections();
			//select will fail, causing a reselect on the parent.
			this._view.selectItem(null);
		},
		
		_doViewHeaderClick: function() {
			//in case we're a child, we remove the formeventconnections but we
			//don't clear the container, the viewviewcontroller
			//will reselect the item in the parent MasterView, and the respective
			//viewFormContainerController will select the appropriate form
			this._clearFormEventConnections();
		}
}
