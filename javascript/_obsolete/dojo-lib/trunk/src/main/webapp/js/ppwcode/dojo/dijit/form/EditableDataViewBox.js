dojo.provide("ppwcode.dojo.dijit.form.EditableDataViewBox");

dojo.require("ppwcode.dojo.dijit.form._FormDataViewBox");
dojo.require("ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");
dojo.require("ppwcode.dojo.util.JavaScriptHelpers");

dojo.declare(
	"ppwcode.dojo.dijit.form.EditableDataViewBox",
	[ppwcode.dojo.dijit.form._FormDataViewBox, ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin],
	{
		templatePath: dojo.moduleUrl("ppwcode", "dojo/dijit/form/templates/EditableDataViewBox.html"),
		
		widgetsInTemplate: true,
		
		constructorFunction: null,
		
		_constructorNameToConstructorMap: null,
		
		_createButtonLabel: '+',
		_deleteButtonLabel: '-',
		
		buildRendering: function() {
			this.inherited(arguments);
			dojo.connect(this._createButton, "onClick", this, "_onCreateButtonClick");
			dojo.connect(this._deleteButton, "onClick", this, "_onDeleteButtonClick");
			dojo.connect(this._masterGrid, "modelDatumChange", this, "onChange");
			dojo.connect(this._masterGrid, "modelInsertion", this, "onChange");
			dojo.connect(this._masterGrid, "modelRemoval", this, "onChange");
		},
		
		removeSelectedItem: function() {
			this._masterGrid.removeSelectedRows();
		},
		
		addItem: function(object) {
			this._dataStore.newItem(object);
			this._masterGrid.edit.setEditCell(this._masterGrid.getCell(0), this._masterGrid.rowCount - 1);
		},
		
		_setValueAttr: function(/*Array*/newValue) {
			var thevalue = newValue ? newValue : [];
			this._masterGrid.edit.cancel();
			this._clearSelection();
			this.setData(dojo.clone(thevalue));
		},

		_getValueAttr: function() {
        	// in case we're getting the value while an edit is still in
        	// progress, we first apply that edit and be done with it
        	this._masterGrid.edit.apply();
        	return this.getData();
        },

        _setDisabledAttr: function(value) {
        	this._masterGrid.edit.apply();
        	this.inherited(arguments);
        	this.disableButtons(value);
        },

        setCreateMenu: function(/*Array*/nameConstructorMap) {
        	var buttonmap = [];
        	this._constructorNameToConstructorMap = new Object();
        	for (var i = 0; i < nameConstructorMap.length; i++) {
				var ctorname = ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(nameConstructorMap[i].constructorFunction);
				this._constructorNameToConstructorMap[ctorname] = nameConstructorMap[i].constructorFunction;
				buttonmap[i] = new Object();
				buttonmap[i].label = nameConstructorMap[i].objectName;
				buttonmap[i].value = ctorname;
        	}
        	this._setCreateMenu(buttonmap);
        },
        
        ////////////////////////// Event handling //////////////////////////

        _onCreateMenuItemClick: function(ctorname, event) {
        	this.clearSelection();
        	var forwardevent = (event) ? event : new Object();
        	var obj = new this._constructorNameToConstructorMap[ctorname]();
        	this.onBeforeAddItem(obj);
        	this.addItem(obj);
        	this.onCreateButtonClick(forwardevent);
        },
        
		_onCreateButtonClick: function(e) {			
			//console.log("EditableDataViewBox: _onCreateButtonClick");
			this.clearSelection();
			var obj = dojo.isFunction(this.constructorFunction) ? new this.constructorFunction() : new Object();
			this.onBeforeAddItem(obj);
			this.addItem(obj);
			this.onCreateButtonClick(e);
		},
		
		onBeforeAddItem: function(item) {
			//summary:
			//   Hook method to initialize an object that will
			//   be added to the DataViewBox
			//item:
			//   the item that will be added
		},
		
		onCreateButtonClick: function(e) {
			//summary:
			//   Hook method for the Add button.
			//description:
			//   Hook method for the Add button.
		},
		
		_onDeleteButtonClick: function(e) {			
			this.removeSelectedItem();
			//console.log("PpwMasterView: _onaddbuttonclick");			
			this.onDeleteButtonClick(e);
		},
		
		onDeleteButtonClick: function(e) {
			//summary:
			//   Hook method for the Delete button.
			//description:
			//   Hook method for the Delete button.
		},
		
		onChange: function(e) {
			//NOP
		}
	}
);