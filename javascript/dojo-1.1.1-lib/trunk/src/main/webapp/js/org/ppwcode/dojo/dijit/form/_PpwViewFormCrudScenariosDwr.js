dojo.provide("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr");

dojo.declare("org.ppwcode.dojo.dijit.form._PpwViewFormCrudScenariosDwr",
		null,
		{
			//selectioncriterium is used to automatically select a certain
			//row in the PpwMasterView.  This is done, for example, after
			//refreshing the contents of the PpwMasterView.
			_selectioncriterium: null,
		
			_dwrRetrieveFunctionParameters: [],

			dwrRetrieveFunction: null,
			dwrCreateFunction: null,
			dwrUpdateFunction: null,

			_copyDwrRetrieveFunctionParameters: function() {
				var params = new Array();
				for (var i = 0; i < this._dwrRetrieveFunctionParameters.length; i++) {
					params[i] = this._dwrRetrieveFunctionParameters[i];
				}
				return params;
			},
			//----------------------------------------------------------------
			//                      RETRIEVE: Grid; datafill
			//----------------------------------------------------------------
			setDwrRetrieveFunctionParameters: function(/*Array*/params) {
				//summary:
				//    Set the parameters that must be passed when the retrieve
				//    DWR method is called.
				//description:
				//    Set the parameters that must be passed when the retrieve
				//    DWR method is called.  Parameters must be passed in an
				//    array.
				//params:
				//    DWR method parameters.
				this._dwrRetrieveFunctionParameters = params;
			},
			
			fillMasterView: function() {
				//summary:
				//    Instruct the PpwController to execute the configured 
				//    retrieve function and fill the PpwMasterView with the
				//    resulting data array.
				//description:
				//    This method encapsulates the creation (creating the method
				//    call and handling the callback) and execution of a DWR call
				//    that executes the configured retrieve method (using the
				//    dwrRetrieveFunction attribute in the tag that defines the
				//    controller widget).
				var self = this;
				
				var callback = function(data) {
					self._view.setData(data);
				}
				
				var errorhandler = function(errorString, exception) {
					self.fillMasterViewErrorHandler(errorString, exception);
				}
				
				var params = this._copyDwrRetrieveFunctionParameters();
				params.push({callback: callback, errorHandler: errorhandler});
				this.dwrRetrieveFunction.apply(this, params);
				
			},
			
			fillMasterViewErrorHandler: function(errorString, exception) {
				//summary:
				//    override if you want to do something in case an error occurs
				//    during the execution of the DWR retrieve method.
				//errorString:
				//    Error message.
				//exception:
				//    The exception that was thrown as a result of this method.
				//    Note that you must configure DWR to serialize exceptions
				//    back to the client.
				console.log("fillMasterViewErrorHandler, please override: " + errorString);
			},
			

			////////////////////////////////////////////////////////////////////
			//                           UPDATE
			////////////////////////////////////////////////////////////////////
			
			/*
			 * step 1: perform the update
			 */

			_doItemUpdate: function(e) {
				//console.log("This is only the beginning of an update action...");
				var self = this;
				
				var callback = function(updatedobject) {
					//determine the selection criterium, so we can select the object again
					//after the grid is refreshed.
					var idfields = self._form.getObjectIdFields();
					self._selectioncriterium = new Object();
					for (var i = 0; i < idfields.length; i++) {
						self._selectioncriterium[idfields[i]] = updatedobject[idfields[i]];
					}
					//allow the application to update things in the user interface after
					//the object was updated.  The object itself will presumably be refreshed
					//by this controller, but in case of parent-child relations, the parent
					//object may need maintenance.
					self.afterItemUpdate(updatedobject);
					//refresh the grid
					self._doMasterViewDataRefreshAfterUpdate(updatedobject);
				}
				
				var errorhandler = function(errorString, exception) {
					self.doItemUpdateErrorHandler(errorString, exception);
				}
				
				//Give the user the chance to do some stuff to the object before
				//the object is given to DWR.
				var obj = e.formObject;
				if (this._viewIsChild) {
	                //console.log("Executing a child update");
					this.beforeItemUpdate(obj, this._viewviewcontroller._getParentSelectedItem());
				} else {
	                //console.log("Executing a parent update");
					this.beforeItemUpdate(obj);
				}
				
				var params = [ obj ];
				params.push({callback: callback, errorHandler: errorhandler});

				this.dwrUpdateFunction.apply(this, params);
			},
			
			beforeItemUpdate: function(/*Object*/updateditem, /*Object?*/parentitem) {
				//summary:
				//    override when you wish to do something before the DWR update
				//    method is performed.
				//updateditem:
				//    The item that will be updated.
				//parentitem:
				//    In case this controller is a child controller, the item that is
				//    currently selected in the parent PpwMasterView is passed as
				//    a second parameter to this method.
			},
			
			afterItemUpdate: function(updateditem) {
				//summary:
				//    override when you wish to do something after the DWR update
				//    method was executed.
				//updateditem:
				//    the item that was updated.
			},

			doItemUpdateErrorHandler: function(errorString, exception) {
				//summary:
				//    override, as you should probably inform the user that the
				//    update probably failed.
				//description:
				//    override, as you should probably inform the user that the
				//    update probably failed.(emphasis on probably, there could
				//    have occurred an error in the network communication while
				//    communicating the return result to the client.
				//errorString:
				//    Error message.
				//exception:
				//    The exception that was thrown as a result of this method.
				//    Note that you must configure DWR to serialize exceptions
				//    back to the client.
				console.log("doItemUpdateErrorHandler, please override: " + errorString);
			},


			/*
			 * step 2: grid refresh
			 */
			// override
			_doMasterViewDataRefreshAfterUpdate: function(updatedobject) {
				var self = this;
				
				var callback = function(data) {
					self._view.setData(data);
					self._doSelectItem();
				}
				
				var errorhandler = function(errorString, exception) {
					self.doMasterViewDataRefreshAfterUpdateErrorHandler(errorString, exception);
				}
				
				//Do the callback to the application.
				this.beforeMasterViewDataRefreshAfterUpdate(updatedobject);
				
				var params = this._copyDwrRetrieveFunctionParameters();
				params.push({callback: callback, errorHandler: errorhandler});

				this.dwrRetrieveFunction.apply(this, params);
			},

			beforeMasterViewDataRefreshAfterUpdate: function(updateditem) {
				//summary:
				//    override this function if you want to do something before the
				//    Refresh function is called, and after an update was performed.
				//description:
				//    Currently, this is mainly meant to reconfigure the Retrieve 
				//    Method's parameters using
				//    this.setDwrRetrieveFunctionParameters() if needed.
				//updateditem:
				//    The item that was updated.
			},
			
			doMasterViewDataRefreshAfterUpdateErrorHandler: function(errorString, exception) {
				//summary:
				//    override, as you should inform the user that refreshing
				//    the master view failed after performing an update.
				// description:
				//    override, as you should inform the user that refreshing the
				//    master view probably failed after performing an update.
				//    (emphasis on probably, there could have occurred an error
				//    in the network communication while communicating the result
				//    to the client.
				//errorString:
				//    Error message.
				//exception:
				//    The exception that was thrown as a result of this method.
				//    Note that you must configure DWR to serialize exceptions
				//    back to the client.
				console.log("doMasterViewDataRefreshAfterUpdateErrorHandler, please override: " + errorString);
			},


			/*
			 * step 3: select the updated object
			 */
			_doSelectItem: function() {
				this._view.clearSelection();
				if (this._selectioncriterium) {
					this._view.selectItem(this._selectioncriterium);
					this._selectioncriterium = null;
				}
			},


			////////////////////////////////////////////////////////////////////
			//                          CREATE
			////////////////////////////////////////////////////////////////////

			_doItemCreate: function(e) {
				var self = this;
				
				var callback = function(createdobject) {
					var idfields = self._form.getObjectIdFields();
					self._selectioncriterium = new Object();
					for (var i = 0; i < idfields.length; i++) {
						self._selectioncriterium[idfields[i]] = createdobject[idfields[i]];
					}
					//allow the application to update things in the user interface after
					//the object was created.  The object itself will presumably be refreshed
					//by this controller, but in case of parent-child relations, the parent
					//object may need maintenance.
					self.afterItemCreate(createdobject);
					//refresh view and select updatedobject
					self._doMasterViewDataRefreshAfterCreate(createdobject);
				}
				
				var errorhandler = function(errorString, exception) {
					self.doItemCreateErrorHandler(errorString, exception);
				}
				
				//Give the user the chance to do some stuff to the object before
				//the object is given to DWR.  In the case of child views, this
				//allows the application to establish the relation between the parent
				//and the child in some way or another
				var obj = e.formObject;
				if (this._viewIsChild) {
					this.beforeItemCreate(obj, this._viewviewcontroller._getParentSelectedItem());
				} else {
					this.beforeItemCreate(obj)
				}

				var params = [ e.formObject ];
				params.push({callback: callback, errorHandler: errorhandler});
				
				this.dwrCreateFunction.apply(this, params);
			},

			beforeItemCreate: function(/*Object*/item, /*Object?*/parentitem) {
				//summary:
				//    override this function if you want to do something before
				//    a new item is created (for example, disable some buttons
				//    in the user interface)
				//description:
				//    override this function if you want to do something before
				//    a new item is created (for example, disable some buttons
				//    in the user interface).
				//item:
				//    The item that is subject to creation
				//parentitem:
				//    In case this controller is a child controller, the item
				//    that is selected in the parent master view is passed in
				//    the second parameter of this method.
			},
			
			afterItemCreate: function(/*Object*/item) {
				//summary:
				//    override this function if you want to do something after
				//    a new item was created (for example, enable some buttons
				//    in the user interface).
				//description:
				//    override this function if you want to do something after
				//    a new item was created (for example, enable some buttons
				//    in the user interface).
				//item:
				//    the item that was created.
			},
			
			doItemCreateErrorHandler: function(errorString, exception) {
				//summary:
				//    override, as you should presumably inform the user that
				//    creating the item probably failed.
				//description:
				//    override, as you should presumably inform the user that
				//    creating the item probably failed.  (emphasis on probably
				//    because it is possible that network communication failed
				//    while communicating the return value of the DWR method
				//    call.
				//errorString:
				//    Error message.
				//exception:
				//    The exception that was thrown as a result of this method.
				//    Note that you must configure DWR to serialize exceptions
				//    back to the client.
				console.log("doItemCreateErrorHandler, please override: " + errorString);
			},

			/*
			 * step 2: grid refresh
			 */
			// override
			_doMasterViewDataRefreshAfterCreate: function(createdobject) {
				var self = this;
				
				var callback = function(data) {
					self._view.setData(data);
					self._doSelectItem();
				}
				
				var errorhandler = function(errorString, exception) {
					self.doMasterViewDataRefreshAfterCreateErrorHandler(errorString, exception);
				}
				
				if (this._viewIsChild) {
					this.beforeMasterViewDataRefreshAfterCreate(createdobject, this._viewviewcontroller._getParentSelectedItem());
				} else {
					this.beforeMasterViewDataRefreshAfterCreate(createdobject);
				}
				
				var params = this._copyDwrRetrieveFunctionParameters();
				params.push({callback: callback, errorHandler: errorhandler});
				//console.log("Parameters:");
				//for (var i = 0; i < params.length; i++) console.log("\t" + params[i]);
				this.dwrRetrieveFunction.apply(this, params);
			},


			beforeMasterViewDataRefreshAfterCreate: function(createditem) {
				//summary:
				//    override this function if you want to do something before the
				//    Refresh function is called and after an item was created.
				//description:
				//    Currently, this is mainly meant to reconfigure the Retrieve 
				//    Method's parameters using
				//    this.setDwrRetrieveFunctionParameters() if needed.
				//createditem:
				//    The item that was created.
			},
			
			doMasterViewDataRefreshAfterCreateErrorHandler: function(errorString, exception) {
				//summary:
				//    override, as you should inform the user that refreshing
				//    the master view failed after performing a create.
				// description:
				//    override, as you should inform the user that refreshing the
				//    master view probably failed after performing a create.
				//    (emphasis on probably, there could have occurred an error
				//    in the network communication while communicating the result
				//    to the client.
				//errorString:
				//    Error message.
				//exception:
				//    The exception that was thrown as a result of this method.
				//    Note that you must configure DWR to serialize exceptions
				//    back to the client.
				console.log("doMasterViewDataRefreshAfterCreateErrorHandler default, please override; " + errorString);
			}
			/*
			 * step 3: select the created object
			 */
			// select according to selection criterium:  identical to update scenario
		}
);
