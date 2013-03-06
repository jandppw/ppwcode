dojo.provide("ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin");

dojo.require("dijit.form.Button");
dojo.require("dijit.Menu");

dojo.declare(
	"ppwcode.dojo.dijit.form._DataViewBoxEditButtonsMixin",
	null,
	{
		_createButtonLabel: "",
		
		_deleteButtonLabel: "",
		
		setAddButtonDisabled: function(/*boolean*/disable) {
			// summary:
			//   Disable the add button.
			// description:
			//   Disables the add button below the form.
			//disable:
			//   boolean:  pass true if the button must be disabled; false if
			//   the button must be enabled.
			this._createButton.attr('disabled', disable);
		},
	
		setDeleteButtonDisabled: function(/*boolean*/disable) {
			// summary:
			//   Disable the delete button.
			// description:
			//   Disables the delete button below the form.
			// disable:
			//   boolean:  pass true if the button must be disabled; false if
			//   the button must be enabled.
			if (this._deleteButton) {
				this._deleteButton.attr('disabled', disable);
			}
		},	
	
		disableButtons: function(/*boolean*/disable) {
			this.setAddButtonDisabled(disable);
			this.setDeleteButtonDisabled(disable);
		},
	
		_setCreateMenu: function(/*Array*/buttondata) {
			// summary:
			//    set the contents of the multibutton, and display the button
			// buttondata:
			//    array of objects.  Objects must contain property 'value' and 'label'
			
			//remove the current widget;
			var menu = new dijit.Menu({ style: "display: none;"});

			for (var i = 0, menuitem = null; i < buttondata.length; i++) {
				menuitem = new dijit.MenuItem({
			         label: buttondata[i].label,
			         iconClass:"dijitTreeIcon dijitLeaf",
			         onClick: dojo.hitch(this, this._onCreateMenuItemClick, buttondata[i].value)
			     });
				menu.addChild(menuitem);
			}
			// remove and destroy old add button
			this._buttonPane.domNode.removeChild(this._createButton.domNode);
			this._createButton.destroyRecursive();
			//put add menu in place as a dropdownbutton
			this._createButton = new dijit.form.DropDownButton({
		         label: this._createButtonLabel,
		         name: "Create",
		         dropDown: menu
		     });
			dojo.place(this._createButton.domNode, this._buttonPane.domNode, "first");
				
		},
		
		setCreateMenu: function(/*Array*/arr) {
			this._setCreateMenu(arr);
		},
		
		_onCreateButtonClick: function(e) {
			console.error("_DataViewBoxEditButtonsMixin: _onCreateButtonClick should be overridden");
			// placeholder
		},
		
		_onCreateMenuItemClick: function(value, event) {
			console.error("_DataViewBoxEditButton:  _onCreateMenuItemClick should be overridden");
			// placeholder
		},
		
		_onDeleteButtonClick: function(e) {
			console.error("_DataViewBoxEditButton:  _onDeleteButtonClick should be overridden");
			// placeholder
		},

		onBeforeAddItem: function(item) {
			// summary:
			//   Hook method to initialize an object that will
			//   be added to the DataViewBox
			// item:
			//   the item that will be added
		},
		
		onCreateButtonClick: function(e) {
			// summary:
			//   Hook method for the Add button.
			// description:
			//   Hook method for the Add button.
		},
		
		onDeleteButtonClick: function(e) {
			// summary:
			//   Hook method for the Delete button.
			// description:
			//   Hook method for the Delete button.
		}

	}
);