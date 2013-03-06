dojo.provide("org.ppwcode.dojo.dijit.form.PpwDataViewBox");

dojo.require("dijit.layout._LayoutWidget");
dojo.require("dijit._Templated");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.Grid");
dojo.require("dijit.form.Button");
dojo.require("dijit.Menu");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");


dojo.declare("org.ppwcode.dojo.dijit.form.PpwDataViewBox",
	[dijit.layout._LayoutWidget, dijit._Templated],
	{
		templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/PpwDataViewBox.html"),

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

		_selectable: true,
		_resizeeventhandle: null,
		
		buildRendering: function() {
			this.inherited(arguments);

			dojo.connect(this._masterGrid, "onRowClick", this, "_ongridrowclick");
			dojo.connect(this._masterGrid, "onHeaderClick", this, "_ongridheaderclick");

			//only allow to select 1 row 
			this._masterGrid.selection.multiSelect = false;
			
			if (this.gridModel) {
				this.setModel(this.gridModel);
			}
			if (this.gridStructure) {
				this.setStructure(this.gridStructure);
			}
		},
		
		postCreate: function() {
			this._connectToParentContainer();
		},
		
		uninitialize: function() {
			if (this._resizeeventhandle) {
				dojo.disconnect(this._resizeeventhandle);
			}
			this.inherited(arguments);
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
		
		setSelectable: function(/*Boolean*/isSelectable) {
			this._selectable = isSelectable;
			this._applySelectable(this._selectable);
		},
		
		_applySelectable: function(/*Boolean*/isSelectable) {
			this._masterGrid.onCanSelect = function() {
				return isSelectable;
			}	
			this._masterGrid.onCanDeselect = function() {
				return isSelectable;
			}
		},
		
		clearSelection: function() {
			//summary:
			//   Clear the selection in the DataGrid.
			//description:
			//   Clears the selection in the Datagrid.  Calling this method
			//   also triggers the onClearSelection event method.
			if (this._selectable) {
				this._masterGrid.selection.clear();
				this._onclearselection();
			}
		},
		
		_clearSelection: function() {
			this._applySelectable(true);
			this._masterGrid.selection.clear();
			this._applySelectable(this._selectable);
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
			//delegation to grid's model
			this._masterGrid.model.setData(newdata);
			//call event
			this._onSetData();
		},

        getData: function() {
			//delegation to grid's model
			return this._masterGrid.model.data;
		},

		setValue: function(/*Object*/newValue) {
			this.selectItem(newValue);
		},

		getValue: function() {
			return this.getSelectedItem();
		},
		
		reset: function() {
			this.clearSelection();
		},
		
		setAttribute: function(/*String*/ attr, /*anything*/ value) {
			this.inherited(arguments);
			switch(attr){
			case "disabled":
				this.setSelectable(!value);
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

		selectItem: function(/*Object*/criterium) {
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
				this._applySelectable(true);
				this._masterGrid.scrollToRow(location);
			    //not sure if this is the way to go, but it has the desired result:
			    //the row is selected in the grid, and the detail form shows the
			    //correct information
			    this._masterGrid.doclick({rowIndex: location});
			    this._applySelectable(this.selectable);
			}
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
			//this.getChildren()[0].resize(this._contentBox);
			this.getChildren()[0].resize();
		},
		
		
		_connectToParentContainer: function() {
			// summary:
			//   connect to a container up in the DOM tree so we can redraw
			//   ourselves upon resizing of the parent container
			// description:
			//   this is a hack :/... It's the only way I found to redraw
			//   the grid if resize events other than the window resize
			//   events occur.
			var potentialNode = this.domNode.parentNode;
			var found = false, nodesdijit = null;
			while (!found && potentialNode != dojo.body()) {
				nodesdijit = dijit.byNode(potentialNode);
				if ( nodesdijit && ( (nodesdijit instanceof dijit.layout._LayoutWidget) 
			        	             || (nodesdijit instanceof dijit.layout.ContentPane) )    
			       ) {
				  found = true;
				} else {
                  potentialNode = potentialNode.parentNode;
				}
			}
			if (found) {
				//console.log("PpwDataViewBox._connectToParentContainer(): potentialNode is " + potentialNode);
				//console.log("PpwDataViewBox._connectToParentContainer(): dijit is " + nodesdijit);
				this._resizeeventhandle = dojo.connect(nodesdijit, "resize", this, "layout");
			} //else {
			//	console.log("PpwDataViewBox._connectToParentContainer(): nothing found");
			//}
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

dojo.declare("org.ppwcode.dojo.dijit.form.PpwEditableDataViewBox",
	org.ppwcode.dojo.dijit.form.PpwDataViewBox,
	{
		templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/PpwEditableDataViewBox.html"),
	
		widgetsInTemplate: true,
	
		constructorFunction: null,
		
		_addChooser: null,
		_constructorNameToConstructorMap: null,
		
		buildRendering: function() {
			this.inherited(arguments);
			dojo.connect(this._addButton, "onClick", this, "_onaddbuttonclick");
			dojo.connect(this._deleteButton, "onClick", this, "_ondeletebuttonclick");
			dojo.connect(this._masterGrid, "modelDatumChange", this, "onChange");
			dojo.connect(this._masterGrid, "modelInsertion", this, "onChange");
			dojo.connect(this._masterGrid, "modelRemoval", this, "onChange");
		},
	
		setAddButtonDisabled: function(/*boolean*/disable) {
			//summary:
			//   Disable the add button.
			//description:
			//   Disables the add button below the form.
			//disable:
			//   boolean:  pass true if the button must be disabled; false if
			//   the button must be enabled.
			this._addButton.setAttribute('disabled', disable);
			if (this._addChooser) {
				this._addChooser.setAttribute('disabled', disable);
			}
		},
		
		setDeleteButtonDisabled: function(/*boolean*/disable) {
			//summary:
			//   Disable the delete button.
			//description:
			//   Disables the delete button below the form.
			//disable:
			//   boolean:  pass true if the button must be disabled; false if
			//   the button must be enabled.
			this._deleteButton.setAttribute('disabled', disable);
		},	
		
		disableButtons: function(/*boolean*/disable) {
			this.setAddButtonDisabled(disable);
			this.setDeleteButtonDisabled(disable);
		},
		
		setMultiButton: function(/*Array*/nameConstructorMap) {
			//remove the current widget;
			this._addChooser = null;

			var menu = new dijit.Menu({ style: "display: none;"});

			this._constructorNameToConstructorMap = new Object();

			for (var i = 0; i < nameConstructorMap.length; i++) {
				//maintain a map between constructorname and the actual function 
				var ctorname = org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(nameConstructorMap[i].constructorFunction);
				this._constructorNameToConstructorMap[ctorname] = nameConstructorMap[i].constructorFunction;

				var menuitem = new dijit.MenuItem({
			         label: nameConstructorMap[i].objectName,
			         iconClass:"dijitTreeIcon dijitLeaf",
			         onClick: dojo.hitch(this, this._onmenuitemclick, ctorname)
			     });
				menu.addChild(menuitem);
			}
			
			//remove and destroy old add button
			this._buttonPane.domNode.removeChild(this._addButton.domNode);
			this._addButton.destroy();

			//put add menu in place as a dropdownbutton
			this._addButton = new dijit.form.DropDownButton({
		         label: "+",
		         name: "Create",
		         dropDown: menu
		     });
			dojo.place(this._addButton.domNode, this._buttonPane.domNode, "first");

		},
		
        removeSelectedItem: function() {
            this._masterGrid.removeSelectedRows();
        },

        addItem: function(object) {
            this._masterGrid.addRow(object);
        },
        
        reset: function() {
        	this.inherited(arguments);
        	this._clearSelection();
        	this.setData(null);
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

        ////////////////////////// Event handling //////////////////////////

        _onmenuitemclick: function(ctorname, event) {
        	this.clearSelection();
        	var forwardevent = (event) ? event : new Object();
        	var obj = new this._constructorNameToConstructorMap[ctorname]();
        	this.onBeforeAddItem(obj);
        	this.addItem(obj);
        	this.onAddButtonClick(forwardevent);
        },
        
		_onaddbuttonclick: function(e) {			
			//console.log("PpwMasterView: _onaddbuttonclick");
			this.clearSelection();
			var obj = dojo.isFunction(this.constructorFunction) ? new this.constructorFunction() : new Object();
			this.onBeforeAddItem(obj);
			this.addItem(obj);
			this.onAddButtonClick(e);
		},
		
		onBeforeAddItem: function(item) {
			//summary:
			//   Hook method to initialize an object that will
			//   be added to the DataViewBox
			//item:
			//   the item that will be added
		},
		
		onAddButtonClick: function(e) {
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
