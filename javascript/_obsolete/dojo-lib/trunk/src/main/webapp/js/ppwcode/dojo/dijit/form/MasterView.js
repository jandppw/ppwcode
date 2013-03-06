dojo.provide("ppwcode.dojo.dijit.form.MasterView");

dojo.require("ppwcode.dojo.dijit.form._DataViewBox");
dojo.require("ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.i18n");

dojo.requireLocalization("ppwcode.dojo.dijit.form", "MasterView");

dojo.declare(
	"ppwcode.dojo.dijit.form.MasterView",
	[ppwcode.dojo.dijit.form._DataViewBox, ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin],
	{
	
		templatePath: dojo.moduleUrl("ppwcode", "dojo/dijit/form/templates/MasterView.html"),
		
		widgetsInTemplate: true,

    // function that returns string with supported operations, e.g. "CRUD"
    supportedOperations: null,

    // function to query wether an operation ("C", "R", "U", "D") is supported
    // if supportedOperations has not been set, the by default supported operations are: "C", "R" and "U"
    supportedOperation: function(/*String*/ operation){
      if (this.supportedOperations == null) {
        return ("CRU".indexOf(operation) != -1);
      } else {
        return ((this.supportedOperations()).indexOf(operation) != -1);
      }
    },

    buildRendering: function() {
			this.inherited(arguments);

      // create the 'create'-button
      var tempButton = dojo.doc.createElement('button');
      this._buttonPane.setContent(tempButton);
      dojo.addClass(tempButton, "MasterViewAddButton");
      this._createButton = new dijit.form.Button({}, tempButton);
      this._createButton.startup();

      if (!this.supportedOperation("C")) {
        this._buttonPane.domNode.style.visibility = 'hidden';
      }

      dojo.connect(this._createButton, "onClick", this, "_onCreateButtonClick");
  	  this._createButtonLabel = dojo.i18n.getLocalization("ppwcode.dojo.dijit.form","MasterView").createButtonLabel;
		  this._createButton.attr("label", this._createButtonLabel);
		},
		
		refreshData: function(/*Array*/refreshdata) {
			//summary:
			//   configure the PpwMasterView with a refreshed dataset that must be displayed in
			//   its Grid.
			//description:
			//   This method is delegated to the Dojo Grid, that is responsible for the actual
			//   displaying of the data.  Calling this method also calls the onRefreshData event
			//   method.  This method does the same thing as setData, except it is meant as a
			//   call to REFRESH the data in the grid.  It is up to the programmer to decide 
			//   whether new data serves as a refresh or as setting new Data.
			//newdata:
			//   The array containing the new data Items to be displayed in the grid.

			this._setData(refreshdata);
			//call event.
			this._onRefreshData();
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
			if (this._masterGrid.selection.getSelectedCount() != 0) {
				var item = this._masterGrid.selection.getFirstSelected();
				for (var key in object) {
					this._dataStore.setValue(item, key, object[key]);
				}
				this._onSelectedRowUpdate();
			}
		},

		reSelectCurrentItem: function() {
			//summary:
			//   Automatically reselects the currently selected object.
			//description:
			//   reselects the currently selected object, this can be done to
			//   get the user interface in a correct state.  Events (onReSelectItemSuccess
			//   and onReSelectItemFail) are fired that can be picked up by controllers.  

			if (this._masterGrid.selection.getSelectedCount() != 0) {
				this._selectInGrid(this._masterGrid.selection.getFirstSelected());
				this.onReSelectCurrentItemSuccess();
			} else {
				this.onReSelectCurrentItemFail();
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
			this.getChildren()[0].resize(this._contentBox);
		},

		////////////////////////// Event handling //////////////////////////
		
		_onCreateButtonClick: function(e) {
			this._clearSelection();
			this.onCreateButtonClick(e);
		},
		
		_onCreateMenuItemClick: function(value, event) {
			console.log("MasterView: _onCreateMenuItemClick with " + value + " and " + event);
			this._clearSelection();
			var forwardevent = null;
			if (event) {
				forwardevent = event;
			} else {
				forwardevent = new Object();
			}
			forwardevent.createChooserValue = value;
			this.onCreateButtonClick(forwardevent);
		},

		_onRefreshData: function() {
			this.onRefreshData();
		},
		
		onRefreshData: function() {
			//summary:
			//   Hook method:  called when the data in the Grid's datamodel is
			//   refreshed.
			//description:
			//   Hook method:  called when the data in the Grid's datamodel is
			//   refreshed.
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
		
		onReSelectCurrentItemSuccess: function() {
			//NOP
		},
		
		onReSelectCurrentItemFail: function() {
			//NOP
		}

	}
);