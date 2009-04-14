dojo.provide("org.ppwcode.dojo.dijit.form._DataBoxEditButtonsMixin");

dojo.require("dijit.form.Button");
dojo.require("dijit.Menu");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");

dojo.declare(
	"org.ppwcode.dojo.dijit.form._DataBoxEditButtonsMixin",
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
			this._createButton.setAttribute('disabled', disable);
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
				this._deleteButton.setAttribute('disabled', disable);
			}
		},	
	
		disableButtons: function(/*boolean*/disable) {
			this.setAddButtonDisabled(disable);
			this.setDeleteButtonDisabled(disable);
		},
	
		setAddMenu: function(/*Array*/buttondata) {
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
			         onClick: dojo.hitch(this, this._onmenuitemclick, buttondata[i].value)
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
		
		setMultiButton: function(/*Array*/nameConstructorMap) {
			this.setAddMenu(nameConstructorMap);
		},
		
		_oncreatebuttonclick: function(e) {
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
		
		_ondeletebuttonclick: function(e) {
			// placeholder
		},
		
		onDeleteButtonClick: function(e) {
			// summary:
			//   Hook method for the Delete button.
			// description:
			//   Hook method for the Delete button.
		},

		_onmenuitemclick: function(e) {
			// placeholder
		}
	}
);