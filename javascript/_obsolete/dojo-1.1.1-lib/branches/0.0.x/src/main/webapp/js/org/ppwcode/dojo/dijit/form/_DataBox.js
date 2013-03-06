dojo.provide("org.ppwcode.dojo.dijit.form._DataBox");

dojo.require("dijit.layout._LayoutWidget");
dojo.require("dijit._Templated");

dojo.declare(
	"org.ppwcode.dojo.dijit.form._DataBox",
	[dijit.layout._LayoutWidget, dijit._Templated],
	{
		gridModel: null,
		
		gridStructure: null,
		
		buildRendering: function() {
			this.inherited(arguments);
			
			//you can only select 1 row in the grid
			this._masterGrid.selection.multiSelect = false;
			
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

	}
);