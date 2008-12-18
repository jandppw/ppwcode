dojo.provide("org.ppwcode.dojo.dijit.form.PpwMasterView");

dojo.require("dijit._Templated");
dojo.require("dijit.layout._LayoutWidget");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Button");
dojo.require("dojox.grid.Grid");
dojo.require("dojo.i18n");
dojo.require("org.ppwcode.dojo.dojox.DataDropDown");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");

dojo.requireLocalization("org.ppwcode.dojo.dijit.form", "PpwMasterView");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwMasterView", 
	[dijit.layout._LayoutWidget, dijit._Templated],
	{
		// summary:
		// The PpwMasterView is a Dojo Grid with an Add button.  The aim is to be able to use the PpwMasterView
		// standalone with inline editing of the Grid cells.  The Add button should allow you to add records
		// to the table.  If used in conjunction with a PpwCrudForm (and a PpwMasterViewController), the 
		// Add button creates a new record using the PpwCrudForm.
		//
		// description:
		// The PpwMasterView is a Dojo Grid with an Add button.  The general idea behind the PpwMasterView is that
		// it can be used standalone as well as in conjunction with a PpwCrudForm.  If used standalone, it should
		// be possible to edit rows in the table.  If used in conjunction with a PpwCrudForm, the grid would not
		// be editable; editing a row would be done by altering fields in the form.  However, up till now, it's
		// only been tested with a PpwCrudForm.
		// 
		// The PpwCrudForm works with Dojox.Grid 1.1 data models (hence, it does not use the more generic datastores
		// that became the default in Dojo 1.2), and has only been tested with the org.ppwcode.dojo.dojox.grid.data.PpwObjects
		// which is a slightly modified version of dojox.grid.data.Objects.  Future versions of this component should
		// be made Grid version 1.2 compliant. In fact, Grid 1.1 also supports models that implement the dojo.data API;
		// so maybe this is the path to follow in a transition to Grid 1.2.
		// 
		// PpwMasterView currently assumes the data in the grid consists of an array of objects, although that's probably
		// dependent of the data model you're using.  It's very much possible that the data consists of a two dimensional
		// array, but again, that data model has not been tested.
		// 
		// The PpwMasterView exposes all Grid configuration methods, such as setting the layout, datamodel and data.  
		// It delegates selection events, and provides a number of convenience methods:
		// * getting the javascript object displayed in the selected row
		// * updating the selected row (getSelectedRow())
		// * clearing the selection (clearSelection())
		// * Selecting a row based on some selection criterium (selectObject())
		// 
		// The following actions fire events:
		// * Setting new data in the grid datamodel (onSetData)
		// * Selecting a row (onGridRowClick(event))
		// * Selecting a header (onGridHeaderClick(event))
		// * clearing a selection (onClearSelection)
		// * updating the selected row (onSelectedRowUpdate)
		// * clicking the Add button (onAddButtonClick(event))
		//
		templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/PpwMasterView.html"),
		
		widgetsInTemplate: true,

		// summary:
		//    HTML attribute to set the DataModel of the Grid encapsulated in the PpwMasterView
		// description:
		//    HTML attribute to set the DataModel of the Grid encapsulated in the PpwMasterView
		gridModel: null,
		
		// summary:
		//    HTML attribute to set the structure of the Grid encapsulated in the PpwMasterView
		// description:
		//    HTML attribute to set the structure of the Grid encapsulated in the PpwMasterView
		gridStructure: null,
		
		_addChooser: null,
		_addButton: null,
		
		buildRendering: function() {
			this.inherited(arguments);
			//Initially the addWidget is defined as a button in our template,
			//only allow to select 1 row 
			this._masterGrid.selection.multiSelect = false;
			dojo.connect(this._addButton, "onClick", this, "_onaddbuttonclick");
			dojo.connect(this._masterGrid, "onRowClick", this, "_ongridrowclick");
			dojo.connect(this._masterGrid, "onHeaderClick", this, "_ongridheaderclick");
			this._addButton.setLabel(dojo.i18n.getLocalization("org.ppwcode.dojo.dijit.form","PpwMasterView").addButtonLabel);
			if (this.gridModel) {
				this.setModel(this.gridModel);
			}
			if (this.gridStructure) {
				this.setStructure(this.gridStructure);
			}
		},
		
  	  	setStructure: function(/*Array*/newstructure) {
			// summary: 
			//   Set the structure of the grid that is encapsulated in this PpwMasterView. 
			// description: 
			//   Set the structure of the grid that is encapsulated in this PpwMasterView.
			//
			// newstructure:
			//   Array containing a Grid structure.
			this._masterGrid.setStructure(newstructure);
		},
		
		setModel: function(/*Object*/newmodel) {
			//summary:
			//   Set the data model of the grid that is encapsulated in this PpwMasterView
			//description:
			//   Set the data model of the grid that is encapsulated in this PpwMasterView
			// newmodel:
			//   The datamodel that will be used to contain the data displayed in the Grid.
			//delegation to grid
			this._masterGrid.setModel(newmodel);
		},
		
		setData: function(/*Array*/newdata) {
			//summary:
			//   configure the PpwMasterView with a new dataset that must be displayed in
			//   its Grid.
			//description:
			//   This method is delegated to the Dojo Grid, that is responsible for the actual
			//   displaying of the data.  Calling this method also calls the setData event
			//   method.
			//newdata:
			//   The array containing the new data Items to be displayed in the grid.
			
			//first clear the selection
			this.clearSelection();
			//remove sort
			this._masterGrid.sortInfo = 0;
			//delegation to grid's model
			this._masterGrid.model.setData(newdata);
			//call event
			this._onSetData();
		},

		setMultiButton: function(/*Array*/formslist) {
			//remove the current widget;
			this._addChooser = null;
			
			var buttonData = { identifier: "value",
			        label: "label",
			        items: []
			       };
			
			for (var i = 0; i < formslist.length; i++) {
				buttonData.items[i] = new Object();
				buttonData.items[i].value = org.ppwcode.dojo.util.JavaScriptHelpers.getFunctionName(formslist[i].constructorFunction);
				buttonData.items[i].label = formslist[i].objectName;
			}
			var buttonDataStore = new dojo.data.ItemFileReadStore({data: buttonData});
			var inputNode = dojo.doc.createElement('button');
			this._buttonPane.domNode.insertBefore(inputNode, this._addButton.domNode);
			this._addChooser =
				new org.ppwcode.dojo.dojox.DataDropDown({store: buttonDataStore}, inputNode);
			this._addChooser.startup();
		},
		
		clearSelection: function() {
			//summary:
			//   Clear the selection in the DataGrid.
			//description:
			//   Clears the selection in the Datagrid.  Calling this method
			//   also triggers the onClearSelection event method.
			this._masterGrid.selection.clear();
			this._onclearselection();
		},
		
		setAddButtonDisabled: function(/*boolean*/disable) {
			//summary:
			//   Disable the add button.
			//description:
			//   Disables the add button below the form.
			//disable:
			//   boolean:  pass true if the button must be disabled; false if
			//   the button must be enabled.
			dojo.deprecated("setAddButtonDisabled is deprecated. Please use disableButtons() instead.");
			this._addButton.setAttribute('disabled', disable);
		},
		
		disableButtons: function(/*boolean*/disable) {
			this._addButton.setAttribute('disabled', disable);
			if (this._addChooser) {
				this._addChooser.setAttribute('disabled', disable);
			}
		},
		
		getSelectedItem: function() {
			//summary:
			//   Get the selected Item from the grid encapsulated in this PpwMasterView.
			//description:
			//   returns the selected Item from the grid.  Note that multiselect is turned
			//   off by default in PpwMasterView, so there will always be only one selected
			//   Item.  The method hence returns an Object, not an array of objects.
			return this._masterGrid.model.getRow(this._masterGrid.selection.getSelected()[0]);
		},

		getSelectedRow: function() {
			dojo.deprecated("getSelectedRow() is deprecated. Use getSelectedItem() instead.", "");
			return this.getSelectedItem();
		},
		
		updateSelectedRow: function(/*Object*/object) {
			//summary:
			//   Update the object that is selected at this moment in the grid's data model
			//description:
			//   Update the object that is selected at this moment in the grid's data model.
			//   Precondition to this method is that there is an object selected.
			//object:
			//   The object that must be put in the grid's data model on the position of
			//   the currently selected item.  Note that this object must have the required
			//   properties.
			this._masterGrid.model.setRow(object, this._masterGrid.selection.getSelected()[0]);
			this._onSelectedRowUpdate();
		},
		
		selectObject: function(/*Object*/criterium) {
			//summary:
			//   Automatically select an object in the PpwMasterView's Grid based an a selection
			//   criterium.
			//description:
			//   Select a row in the Grid based on a selection criterium.  The selection criterium
			//   is an object that contains at least one property that also exists in the objects
			//   that are being displayed in the grid.  For an Item in the Grid's datamodel to
			//   be selected, the values of all properties in the criterium must be equal to the
			//   values of the corresponding properties in the Item.  Only the first Item that
			//   matches will be selected in the Grid.  If no Item matches the criterium, nothing
			//   will be selected.
			//criterium:
			//   Object with a number of properties (and also values) that must be satisfied for
			//   an Item in the grid to be selected.
			
			var datasetsize = this._masterGrid.model.getRowCount();
			var found = false;
			var location = -1;
			// copy criterium properties in Array.
			var properties = new Array();
			var tmpi = 0;
			for (properties[tmpi++] in criterium);
			if (properties.length == 0) {
				//no criterium? -> no select
				return;
			}
			//look for the first matching record
			for (var i = 0; !found && (i < datasetsize) ; i++) {
 				for (var j = 0, rowmatch = true; rowmatch && (j < properties.length); j++) {
					if (this._masterGrid.model.getRow(i)[properties[j]] != criterium[properties[j]]) {
						rowmatch = false;
					}
				}
				found = rowmatch;
				location = i;
			}

			if (found) {
				this._masterGrid.scrollToRow(location);
			    //not sure if this is the way to go, but it has the desired result:
			    //the row is selected in the grid, and the detail form shows the
			    //correct information
			    this._masterGrid.doclick({rowIndex: location});
			}
		},
		
		selectItem: function(criterium) {
			//summary:
			//   Same as selectObject, this method should be preferred.
			//description:
			//   Same as selectObject, this method should be preferred.
			this.selectObject(criterium);
		},
		
		////////////////////////// Layout ////////////////////////////

		layout: function() {
			// stole this from _LayoutWidget:  this calculates the size of the pane
			// in which the grid resides.  Since we're a layoutcontainer, we must tell
			// our children how to draw themselves, so here goes (nothing).
			//var marginbox = dojo.marginBox(this._masterGridPane.domNode);
			//var contentbox = dijit.layout.marginBox2contentBox(this._masterGridPane.domNode, marginbox);

			//if we do not do this call, the Big Bad Grid (tm) doesn't
			//resize() upon resizing of this layout widget
			this.getChildren()[0].resize(this._contentBox);
		},
		
		////////////////////////// Event handling //////////////////////////

		_onSetData: function() {
			this.onSetData();
		},
		
		onSetData: function() {
			//summary:
			//   Hook method:  called when the data in the Grid's datamodel is
			//   renewed.
			//description:
			//   Hook method:  called when the data in the Grid's datamodel is
			//   renewed.
		},
		
		_onclearselection: function() {
			this.onClearSelection();
		},
		
		onClearSelection: function() {
			//summary:
			//   Hook method: called when the selection in the PpwMasterView's Grid is
			//   cleared.
			//description:
			//   Hook method: called when the selection in the PpwMasterView's Grid is
			//   cleared.
		},
		
		_onSelectedRowUpdate: function() {
			this.onSelectedRowUpdate();
		},
		
		onSelectedRowUpdate: function() {
			//summary:
			//   Hook method:  called when the selected Item in the Grid is overwritten
			//   with a new object.
			//description:
			//   Hook method:  called when the selected Item in the Grid is overwritten
			//   with a new object.
		},
		
		_onaddbuttonclick: function(e) {			
			//console.log("PpwMasterView: _onaddbuttonclick");
			this.clearSelection();
			if (this._addChooser) {
				e.addChooserValue = this._addChooser.getValue();
			}
			this.onAddButtonClick(e);
		},
		
		onAddButtonClick: function(e) {
			//summary:
			//   Hook method for the Add button.
			//description:
			//   Hook method for the Add button.
		},
		
		_ongridrowclick: function(e) {
			//console.log("PpwMasterView: _ongridrowclick");
			//call user event
			this.onGridRowClick(e);
		},

		onGridRowClick: function(event) {
			//summary:
			//   Hook method that is called when a Row in the PpwMasterView is clicked(selected).
			//description:
			//   Hook method that is called when a Row in the PpwMasterView is clicked(selected).
			//event:
			//   The same event that you would receive when receiving a row select event on a
			//   Dojox.Grid.
		},
		
		_ongridheaderclick: function(e) {
			//console.log("PpwMasterView: _ongridheaderclick");
			//sorting... clear selection.
			this.clearSelection();
			//call user event
			this.onGridHeaderClick();
		},
		
		onGridHeaderClick: function(e) {
			//summary:
			//   Hook method that is called when a Header row in the PpwMasterView is clicked(selected).
			//description:
			//   Hook method that is called when a Header row in the PpwMasterView is clicked(selected).
			//event:
			//   The same event that you would receive when receiving a header select event on a
			//   Dojox.Grid.
		}
		
	}
);