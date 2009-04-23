dojo.provide("org.ppwcode.dojo.dijit.form.EditableDataViewBox");

dojo.require("org.ppwcode.dojo.dijit.form._FormDataViewBox");
dojo.require("org.ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.EditableDataViewBox",
	[org.ppwcode.dojo.dijit.form._FormDataViewBox, org.ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin],
	{
		templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/EditableDataViewBox.html"),
		
		widgetsInTemplate: true,

		_constructorNameToConstructorMap: null,
		
		_createButtonLabel: '+',
		_deleteButtonLabel: '-',
		
		buildRendering: function() {
			this.inherited(arguments);
			dojo.connect(this._addButton, "onClick", this, "_oncreatebuttonclick");
			dojo.connect(this._deleteButton, "onClick", this, "_ondeletebuttonclick");
			dojo.connect(this._masterGrid, "modelDatumChange", this, "onChange");
			dojo.connect(this._masterGrid, "modelInsertion", this, "onChange");
			dojo.connect(this._masterGrid, "modelRemoval", this, "onChange");
		},
		
		removeSelectedItem: function() {
			this._masterGrid.removeSelectedRows();
		},
		
		addItem: function(object) {
			this._dataStore.newItem(object);
			this._masterGrid.addRow(object);
		},
		
		reset: function(object) {
			this._clearSelection();
			this.setData([]);
		},
		
		setValue: function(/*Array*/newValue) {
			this._masterGrid.edit.cancel();
			this._clearSelection();
			this.setData(dojo.clone(newValue));
		},

		getValue: function() {
        	// in case we're getting the value while an edit is still in
        	// progress, we first apply that edit and be done with it
        	this._masterGrid.edit.apply();
        	return this.getData();
        },

		setAttribute: function(/*String*/ attr, /*anything*/ value) {
        	this.inherited(arguments);
        	switch(attr) {
        	case "disabled":
        		this.disableButtons(value);
        		//MUDO: we should not assume on the availability of setModifiable here!!!
        		//first we apply any ongoing edit before we turn off modifiability
        		this._masterGrid.edit.apply();
        		this._masterGrid.model.setModifiable(!value);
        		break;
        	}	
        },
        
        
        setCreateMenu: function(/*Array*/nameConstructorMap) {
        	var buttonmap = [];
        	this._constructorNameToConstructorMap = new Object();
        	for (var i = 0; i < nameConstructorMap.length; i++) {
				var ctorname = org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(nameConstructorMap[i].constructorFunction);
				this._constructorNameToConstructorMap[ctorname] = nameConstructorMap[i].constructorFunction;
				buttonmap[i] = new Object();
				buttonmap[i].label = nameConstructorMap[i].objectName;
				buttonmap[i].value = ctorname;
        	}
        	this._setCreateMenu(buttonmap);
        },
        
        ////////////////////////// Event handling //////////////////////////

        _onmenuitemclick: function(ctorname, event) {
        	this.clearSelection();
        	var forwardevent = (event) ? event : new Object();
        	var obj = new this._constructorNameToConstructorMap[ctorname]();
        	this.onBeforeAddItem(obj);
        	this.addItem(obj);
        	this.onCreateButtonClick(forwardevent);
        },
        
		_oncreatebuttonclick: function(e) {			
			//console.log("PpwMasterView: _onaddbuttonclick");
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
		
		_ondeletebuttonclick: function(e) {			
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