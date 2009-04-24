dojo.provide("org.ppwcode.dojo.dijit.form._DataViewBox");

dojo.require("dijit.layout._LayoutWidget");
dojo.require("dijit._Templated");
dojo.require("org.ppwcode.dojo.dojo.data.ObjectArrayStore");

dojo.declare(
	"org.ppwcode.dojo.dijit.form._DataViewBox",
	[dijit.layout._LayoutWidget, dijit._Templated],
	{
		gridStructure: null,
		
		identifierPropertyName: "",
		comparatorMap: null,
		
		_data: null,
		_dataStore: null,
		_comparatorMap: null,
		
		constructor: function(kwArgs) {
			if (kwArgs) {
				if (kwArgs.identifierPropertyName) {
					this.identifierPropertyName = kwArgs.identifierProperty;
				}
				if (kwArgs.gridStructure) {
					this.gridStructure = kwArgs.gridStructure;
				}
				if (kwArgs.comparatorMap) {
					this.comparatorMap = kwArgs.comparatorMap; 
				}
			}
		},
		
		postMixInProperties: function() {
			this._comparatorMap = this.comparatorMap;
		},
		
		buildRendering: function() {
			this.inherited(arguments);
			
			//you can only select 1 row in the grid
			this._masterGrid.selection.setMode("single");
			
			if (this.gridStructure) {
				this.setStructure(this.gridStructure);
			}
			
			dojo.connect(this._masterGrid, "onRowClick", this, "_ongridrowclick");
			dojo.connect(this._masterGrid, "onHeaderClick", this, "_ongridheaderclick");

		},

		setStructure: function(/*Array*/newstructure) {
			// summary: 
			//   Set the structure of the grid that is encapsulated in this PpwMasterView. 
			// description: 
			//   Set the structure of the grid that is encapsulated in this PpwMasterView.
			//
			// newstructure:
			//   Array containing a Grid structure.
			this._masterGrid.attr('structure', newstructure);
		},
		
		_createDojoDataTemplate: function() {
			return {identifier: this.identifierProperty};
		},
		
		_setData: function(newdata) {
			//first clear the selection
			this.clearSelection();
			//remove sort
			this._masterGrid.sortInfo = 0;
			//delegation to grid's model
			this._data = newdata;
			var storeParams = {data: this._data};
			if (this.identifierPropertyName) {
				storeParams.identifierPropertyName = this.identifierPropertyName;
			}
			if (this._comparatorMap) {
				storeParams.comparatorMap = this._comparatorMap; 
			}
			this._dataStore = new org.ppwcode.dojo.dojo.data.ObjectArrayStore(storeParams);
			this._masterGrid.setStore(this._dataStore);
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
			
			this._setData(newdata);
			//call event
			this._onSetData();
		},

        getData: function() {
			// delegation to grid
			var result = [];
			var i = 0;
			var item = this._masterGrid.getItem(i);
			while (item) {
				result.push(item);
				item = this._masterGrid.getItem(++i);
			}
			return result;
		},
		
		clear: function() {
			this._clearSelection();
			this._data = this._createDojoDataTemplate();
			this._data.items = [];
			this._dataStore = new org.ppwcode.dojo.dojo.data.ObjectArrayStore({data: []});
			this._masterGrid.setStore(this._dataStore);

			this._onClear();
		},
		
		clearSelection: function() {
			// summary:
			//   Clear the selection in the DataGrid.
			// description:
			//   Clears the selection in the Datagrid if the selection is possible.
			//   Calling this method also triggers the onClearSelection event method.
			this._clearSelection();
			this._onclearselection();
		},
		
		_clearSelection: function() {
			//clear grid's selection
			this._masterGrid.selection.clear();
			//reset focus cell
			this._masterGrid.focus.setFocusIndex(-1, 0)
		},

		
		getSelectedItem: function() {
			// summary:
			//   Get the selected Item from the grid encapsulated in this PpwMasterView.
			// description:
			//   returns the selected Item from the grid.  Note that multiselect is turned
			//   off by default in PpwMasterView, so there will always be only one selected
			//   Item.  The method hence returns an Object, not an array of objects.
			return this._masterGrid.selection.getFirstSelected();
		},

		_findItem: function(/*Object*/criterium) {
			//summary:
			//   Find an object in the PpwMasterView's Grid based an a selection criterium.
			//   Returns the row number in the Grid, or -1 in case no Item in the grid matches
			//   the criterium
			//description:
			//   Find a row in the Grid based on a selection criterium.  The selection criterium
			//   is an object that contains at least one property that also exists in the objects
			//   that are being displayed in the grid.  For an Item in the Grid's datamodel to
			//   match, the values of all properties in the criterium must be equal to the
			//   values of the corresponding properties in the Item.  Only the first Item that
			//   matches will returned.  If no Item matches the criterium, nothing
			//   will be return.
			//criterium:
			//   Object with a number of properties (and also values) that must be satisfied for
			//   an Item in the grid to be selected.

			// copy criterium properties in Array.
			var properties = new Array();
			var tmpi = 0;
			for (properties[tmpi++] in criterium);
			if (properties.length == 0) {
				//no criterium? -> no select
				return -1;
			}

			//look for the first matching record
			var found = false;
			var i = 0;
			var currentItem = this._masterGrid.getItem(i);
			while (!found && currentItem) {
				for (var j = 0, rowmatch = true; rowmatch && (j < properties.length); j++) {
					//Dates are special... 
					var crit = criterium[properties[j]];
					var subject = currentItem[properties[j]];
					if ((crit instanceof Date) && (subject instanceof Date)) {
						if (crit.getTime() != subject.getTime()) {
							rowmatch = false;
						}
					} else { //no dates, so we do value checks
						if (crit != subject) {
							rowmatch = false;
						}
					}
				}
				found = rowmatch;
				if(!found) {
					currentItem = this._masterGrid.getItem(++i);
				}
			}
			//console.log((found ? "Found" : "Did not find") + " object" + (found ? " on row " + i : ""));
			if (found) {
				return i;
			} else {
				return -1;
			}
		},
		
		_selectInGrid: function(rownumber) {
			this._masterGrid.scrollToRow(rownumber);
			this._masterGrid.selection.select(rownumber);
		},
		
		selectItem: function(/*Object*/criterium) {
			//summary:
			//   Automatically select an object in the PpwMasterView's Grid based an a selection
			//   criterium.  This method programmatically clicks the selected row (and hence fires
			//   click events).
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
			
			var rownumber = this._findItem(criterium);

			if (rownumber != -1) {
				this._selectInGrid(rownumber);
				//not sure if this is the way to go, but it has the desired result:
				//the row is selected in the grid, and the detail form shows the
				//correct information
				this.onSelectItemSuccess();
			} else {
				this.onSelectItemFail();
			}
		},

		//////////////////////////////// Layout ////////////////////////////
		
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

		///////////////////////////// Event Handling ///////////////////////
		
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

		_onClear: function() {
			this.onClear();
		},
		
		onClear: function() {
			
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
		},
		
		onSelectItemSuccess: function() {
			// NOP
		},
		
		onSelectItemFail: function() {
			// NOP
		}

	}
);