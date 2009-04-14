dojo.provide("org.ppwcode.dojo.dijit.form.PpwObjectForm");

dojo.require("dijit.form.Form");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwObjectForm",
	dijit.form.Form,
	{
		// contains the mapping between javascript object properties and form fields
		_formmap: null,
		_byFieldIdMap: null,
		_byPropertyNameMap: null,

		_thedisplayobject: null,

		objectName: "",

		setFormMap: function(/*Array*/formmap) {
			// summary:
			//   Set a new formmap on the form.  The formmap links input fields
			//   in the form to properties/attributes in the javaobjects that
			//   will be displayed in the form, and resets the form.
			// description.
			//   To be able to display an object, the PpwCrudForm must know how to
			//   map the properties of an object to the fields in the form. This is
			//   done by setting the fromMap (setFormMap(map)) of the PpwCrudForm.
			//   A formMap is array of mapping objects:
			//
			//   var formmap = [{property: "prop1", fieldId: "textfield1", isId: true},
			//                  {property: "prop2", fieldId: "hiddenfield1},
			//                   ...
			//                  {property: "propN", feieldId: "datefieldN"}];
			// formmap: an array of mapping objects.
			this._formmap = formmap;
			this._byFieldIdMap = new Object();
			this._byPropertyNameMap = new Object();
			for (var i = 0; i < formmap.length; i++) {
				if ( (formmap[i].fieldid !== undefined) && dojo.isString(formmap[i].fieldid) ) {
					if (!dojo.isString(formmap[i].property)) {
						throw new Error("Invalid Form Map");
					}
					this._byFieldIdMap[formmap[i].fieldid] = new Object();
					this._byFieldIdMap[formmap[i].fieldid].widget = dijit.byId(formmap[i].fieldid);
					this._byFieldIdMap[formmap[i].fieldid].property = formmap[i].property;
					//this._byFieldIdMap[formmap[i].fieldid].domNode = dojo.byId(formmap[i].fieldid);
					if (formmap[i].isEditable === false) {
						this._byFieldIdMap[formmap[i].fieldid].isEditable = formmap[i].isEditable;
					} else {
						this._byFieldIdMap[formmap[i].fieldid].isEditable = true;
					}
					this._byPropertyNameMap[formmap[i].property] = new Object();
					this._byPropertyNameMap[formmap[i].property].widget = dijit.byId(formmap[i].fieldid);
					this._byPropertyNameMap[formmap[i].property].domNode = dojo.byId(formmap[i].fieldid);
					if (formmap[i].editable === false) {
						this._byPropertyNameMap[formmap[i].property].isEditable = formmap[i].isEditable;
					} else {
						this._byPropertyNameMap[formmap[i].property].isEditable = true;
					}
				}
			}
			this.reset();
		},

		_disableFormFields: function(/*boolean*/disabled){
  			for (var fieldid in this._byFieldIdMap) {
			   var entry = this._byFieldIdMap[fieldid];
			   entry.widget.setAttribute("disabled", entry.isEditable ? disabled : true );
		   }
		},
		
		reset: function() {
        	this._thedisplayobject = null;
        	this.inherited(arguments);
		},
		
		displayItem: function(obj) {
        	this._displayObject(obj);
		},

		_displayObject: function(obj) {
			// summary:
			//   display an object in the form.  This object will be layed out
			//   in the form according to the formmap.
			// description:
			//   This object iterates over all the properties in the object and
			//   locates a input field in the form that corresponds with the
			//   property.  This mapping between properties and input fields is
			//   defined in the formmap.  If a property in the formmap yields
        	//   "undefined", it is silently ignored. (TODO it is considered a 
        	//   programming error, maybe it should throw an error).
			// obj:
			//   The object that will be displayed in the form.
			this.reset();
			this._thedisplayobject = obj;
			if (obj) {
				//copy fields in the object to the form.
				for (var fieldid in this._byFieldIdMap) {
					var propnamelist = this._byFieldIdMap[fieldid].property.split('.');
					var result = obj;
					var i = 0, abort = false;
					do {
						var propvalue = result[propnamelist[i++]];
						// don't display the property:
						// if intermediate/final result is undefined
						//  OR
						// if the property name is not fully resolved and null/undefined.
						if ( (propvalue === undefined)
							 || ((i != propnamelist.length) && !propvalue) ) {
							abort = true;
						} else {
							result = propvalue;
						}
					} while (!abort && i < propnamelist.length);
					// if we have value that is not 'undefined' the property is displayed
					if (!abort) {
						this._byFieldIdMap[fieldid].widget.setValue(result);
					}
				}
			}
		},

		getObjectName: function() {
			// summary:
			//    returns the name of the object that this form shows
			//    in a human readable form
			// description:
			//    this property can be set using the objectName attribute
			//    in the defining HTML tag.
			return this.objectName;
		},
		
		getObjectIdFields: function() {
			// summary:
			//    Returns an array containing the names of the properties
			//    that establish the id of the objects that are displayed
			//    in the form.
			// description:
			//    Returns an array of strings:  each entry is a property
			//    name that was defined in the formmap and was tagged with
			//    isId: true.
			var idfields = new Array();
			for (var i = 0; i < this._formmap.length; i++) {
				if (this._formmap[i].isId === true) {
					idfields.push(this._formmap[i].property);
				}
			}
			return idfields;
		}
	}
);
             