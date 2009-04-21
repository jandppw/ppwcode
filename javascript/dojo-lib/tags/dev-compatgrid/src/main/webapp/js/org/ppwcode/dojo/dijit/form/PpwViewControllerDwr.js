dojo.provide("org.ppwcode.dojo.dijit.form.PpwViewControllerDwr");

dojo.require("dijit._Widget");
dojo.require("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwViewControllerDwr",
	//The controller is a widget, solely to have the dojo.parser call lifecycle functions...
	[dijit._Widget, org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr],
	{
		_view: null,
		
		//these should be set only through the dojo parser
		view: null,	

		postCreate: function() {
			// only do this when the dojo parser sets the view and form
			// properties.  This can be done by setting the form and
			// view attributes on the tag that defines this widget.
			if (this.view) {
				this.setView(this.view);
			}
			this.inherited(arguments);
		},
		
		setView: function(view) {
			//summary:
			//    Set the PpwMasterView and the PpwCrudForm that this
			//    controller will be controlling.
			//description:
			//    This method wires events that are fired by the
			//    PpwMasterView and PpwCrudForm to this controller
			//    using dojo.connect().
			this._view = view;
		}
	}
);