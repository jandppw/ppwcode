dojo.provide("org.ppwcode.dojo.dijit.form.CrudFormContainer");

dojo.require("dijit.layout.StackContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("org.ppwcode.dojo.util.JavaScriptHelpers");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.CrudFormContainer",
	dijit.layout.StackContainer,
	{
		_formIdMap: null,
		// we use a map from the constructor function NAME to the formid
		// instead of from the constructor FUNCTION, because it is not possible
		// to use a function object as a value in a ComboBox (it is possible
		// but it acts funnily).  We need the ComboBox in the PpwMasterView.
		// when adding new Objects.
		_constructorNameMap: null,
		_displayingformid: null,

//		constructorMap: null,
		
		postMixInProperties: function() {
			this.inherited(arguments);
//			if (this.constructorMap) {
//				this.setConstructorMap(this.constructorMap);
//			}
		},
	
		startup: function() {
			this.inherited(arguments);
			// we assume that our children are content panes that contain
			// exactly one form.
			this._formIdMap = new Object();
			this._constructorNameMap = new Object();
			var children = this.getChildren();
			for (var i = 0; i < children.length; i++) {
				var contentpane = children[i];
				if (dojo.attr(contentpane.domNode, 'isEmptyPane') == "true") {
					this._formIdMap["__empty"] = contentpane;
				} else {
					var list = dojo.query("form[widgetId]", contentpane.containerNode);
					dojo.forEach(list, function(theform) {
							//console.log("child: " + theform + " with id " + dojo.attr(theform, "id") + " and constructor " + dijit.byNode(theform).getConstructorFunction().name);
							var mapproperties = new Object();
							mapproperties.pane = contentpane;
							mapproperties.form = dijit.byNode(theform);
							this._formIdMap[dojo.attr(theform, "id")] = mapproperties;
							this._constructorNameMap[org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(mapproperties.form.getConstructorFunction())] = dojo.attr(theform, "id");
						}, this);
				}
			}
			if (!this._formIdMap["__empty"]) {
				// add an empty pane
				var tmpnode = dojo.doc.createElement('div');
				var emptypage = new dijit.layout.ContentPane(null, tmpnode);
				emptypage.setAttribute("title", "emptyPane");
				// obey the rules
				emptypage.startup();
				//add it to the list
				this._formIdMap["__empty"] = emptypage;
				this.addChild(emptypage);
			}
			//set the empty as active page. (apparently this does not happen when
			//the child is added, this is a bug in the stack container.
			this.clear();
		},

//		setConstructorMap: function(/*Array*/map) {
//			if (map.length != 0) {
//				this._formCreatorFunctionMap = new Object();
//				for (var i = 0; i < map.length; i++) {
//					this._constructorMap[map[i].constructor] = map[i].formid;
//				}
//			}
//		},
		
		displayForm: function(/*String*/formid) {
			var formdata = this._formIdMap[formid];
			if (formdata) {
				this.resetCurrentForm();
				this.selectChild(formdata.pane);
				this._displayingformid = formid;
			}
		},
		
		clear: function() {
			if (this._displayingformid && (this._displayingformid != "__empty")) {
				this.resetCurrentForm();
			}
			this.selectChild(this._formIdMap["__empty"]);
			this._displayingformid = "__empty";
		},
		
		resetCurrentForm: function() {
			if (this._displayingformid && (this._displayingformid != "__empty")) {
				this.resetForm(this._displayingformid);
			}
		},
		
		setCurrentFormInUpdateMode: function() {
			if (this._displayingformid && (this._displayingformid != "__empty")) {
				this.setFormInUpdateMode(this._displayingformid);
			}
		},
		
		setCurrentFormInCreateMode: function() {
			if (this._displayingformid && (this._displayingformid != "__empty")) {
				this.setFormInCreateMode(this._displayingformid);
			}
		},

		setCurrentFormInCreateModeNoReset: function() {
			if (this._displayingformid && (this._displayingformid != "__empty")) {
				this.setFormInCreateModeNoReset(this._displayingformid);
			}
		},

		resetForm: function(formid) {
			this._formIdMap[formid].form.reset();
		},

		setFormInUpdateMode: function(formid) {
			this._formIdMap[formid].form.setUpdateMode();
		},
		
		setFormInCreateMode: function(formid) {
			this._formIdMap[formid].form.setCreateMode();
		},
		
		setFormInCreateModeNoReset: function(formid) {
			this._formIdMap[formid].form.setCreateModeNoReset();
		},
		
		getFormForConstructor: function(/*String*/constructorname) {
			var formid = this._constructorNameMap[constructorname]; 
			if (formid && this._formIdMap[formid]) {
				return this._formIdMap[formid].form;
			} else {
				return null;
			}
		},
		
		getFormsList: function() {
			// summary:
			//   returns a list of objects.  Each object contains information
			//   about a form in the container.
			// description:
			//   The information in each object in the list contains the
			//   following information:
			//   * the id of the form (ID property in the form's HTML tag)
			//   * the human readable name of the object that is displayed in this form
			//   * the JavaScript constructor function that must be used for objects
			//     displayed in this form
			var result = new Array();
			for (var formid in this._formIdMap) {
				if (formid != "__empty") {
					var item = new Object();
					var form = this._formIdMap[formid].form;
					item.constructorFunction = form.getConstructorFunction();
					item.objectName = form.getObjectName();
					item.formId = formid;
					result.push(item);
				}
			}
			return result;
		},
		
		displayItem: function(/*Object*/obj) {
			this.resetCurrentForm();
			var theformid = this._constructorNameMap[org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(obj.constructor)];
			//constructor is defined in the map
			if (theformid) {
				this.displayForm(theformid); 
				this._formIdMap[theformid].form.displayItem(obj);
			} else {
				this.displayForm("__empty");
			}
		},
		
		createObject: function(/*String || Object*/ctornameorproto) {
			var formid = null;
			if (dojo.isString(ctornameorproto)) {
				formid = this._constructorNameMap[ctornameorproto];
			} else {
				formid = this._constructorNameMap[org.ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName(ctornameorproto.constructor)];
			}
			if (formid && this._formIdMap[formid]) {
				this.displayForm(formid);
				this._formIdMap[formid].form.createObject(dojo.isObject(ctornameorproto) ? ctornameorproto : null);
			} else {
				this.displayForm("__empty");
			}
		},
		
		displayErrorMessages: function(/*Object[]*/messages) {
			if (this._displayingformid && this._displayingformid != "__empty") {
				this._formIdMap[this._displayingformid].form.displayErrorMessages(messages);
			}
		},
		
		displayPropertyException: function(/*CompoundPropertyException*/compoundpropertyexception) {
			if (this._displayingformid && this._displayingformid != "__empty") {
				this._formIdMap[this._displayingformid].form.displayPropertyException(compoundpropertyexception);
			}
		}
	}
);