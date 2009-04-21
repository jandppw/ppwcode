dojo.provide("org.ppwcode.dojo.dojo.data.ObjectArrayStore");

dojo.require("dojo.data.util.simpleFetch");

dojo.declare(
	"org.ppwcode.dojo.dojo.data.ObjectArrayStore",
	null,
	{
		_itemNumPropName: "__OASID__",
		_identifierPropName: "",
		_idNumCounter: 1,

		_arrayOfItems: null,
		_itemsByIdentity: null,
		
		_createdItemsByIdentity: null,
		_deletedItemsByIdentity: null,
		_updatedItemsByIdentity: null,
	
		_data: null,
		
		_labelPropName: "",
		
		constructor: function(keywordParameters) {
			if (keywordParameters.data) {
				this._data = keywordParameters.data;
			}
			if (keywordParameters.identifierPropertyName) {
				this._identifierPropName = keywordParameters.identifierPropertyName;
			}
			if (keywordParameters.labelPropertyName) {
				this._labelPropName = keywordParameters.labelPropName;
			}
		},

		_assertIsItem: function(/* Item */item) {
			var isItem = this.isItem(item);
			if (!this.isItem(item)) {
				throw new Error("ObjectArrayStore: invalid item argument");
			}
		},
		
		_assertIsAttribute: function(/* attribute-name-string */ attribute){
			//copied from ItemFileReadStore
			if(typeof attribute !== "string"){ 
				throw new Error("ObjectArraySTore: Invalid attribute argument.");
			}
		},
		
		_assertHasLabelProperty: function() {
			if (!this._labelPropName) {
				throw new Error("ObjectArrayStore: using labels requires a label property on the store");
			}
		},
		
		getValue: function(	/* item */ item, 
							/* attribute-name-string */ attribute, 
							/* value? */ defaultValue){
			var attributeValue = null;
			this._assertIsItem(item);
			this._assertIsAttribute(attribute);
			if (item[attribute] !== undefined) {
				attributeValue = item[attribute];
			} else {
				attributeValue = defaultValue;
			}
			// note that we possibly return an array here... let's see if this runs
			// us into trouble :/.  The documentation says this method should
			// never return array, but what should we do with multivalued attributes...
			return attributeValue;
		},

		getValues: function(/* item */ item,
							/* attribute-name-string */ attribute){
			var value = this.getValue(item, attribute);
			if (value === undefined) {
				value = [];
			} else if (!dojo.isArray(value)) {
				// this method must always return an array, so if the result
				// is not an array, we make an array containing the one element.
				value = [value];
			}
			return value;
		},

		getAttributes: function(/* item */ item){
			this._assertIsItem(item);
			var attributes = [];
			for (var key in item) {
				if (key != this._itemNumPropName) {
					attributes.push(key);
				}
			}
			return attributes;
		},

		hasAttribute: function(	/* item */ item,
								/* attribute-name-string */ attribute){
			throw new Error('Unimplemented API: dojo.data.api.Read.hasAttribute');
			return false; // boolean
		},

		containsValue: function(/* item */ item,
								/* attribute-name-string */ attribute, 
								/* anything */ value){
			throw new Error('Unimplemented API: dojo.data.api.Read.containsValue');
			return false; // boolean
		},

		isItem: function(/* anything */ something){
			if (something && dojo.isObject(something)) {
				var id = something[this._identifierPropName];
				if (id && (this._itemsByIdentity[id] === something)) {
					return true;
				}
			}
			return false;
		},

		isItemLoaded: function(/* anything */ something) {
			return this.isItem(something);
		},

		loadItem: function(/* object */ keywordArgs){
			//as in ItemFileReadStore
			this._assertIsItem(keywordArgs.item);
		},

		_duplicateArray: function(thearray) {
			var duplicate = [];
			for (var i = 0; i < thearray.length; i++) {
				duplicate[i] = thearray[i];
			}
			return duplicate;
		},
		
		_loadItems: function() {
			for (var i = 0; i < this._data.length; i++) {
				var currentitem = this._data[i];
				// if _identifierPropName is _itemNumPropName, we must assign our
				// own identifier
				if (this._identifierPropName == this._itemNumPropName) {
					currentitem[this._identifierPropName] = this._idNumCounter++;
				}
				this._itemsByIdentity[currentitem[this._identifierPropName]] = currentitem;
				this._arrayOfItems[i] = currentitem;
			}
		},
		
		_fetchItems: function( /* Object */ keywordArgs,
                               /* Function */ findCallback,
                               /* Function */ errorCallback) {
			if (!this._data && !this._arrayOfItems) {
				throw new Error("ObjectArrayStore: cannot fetch without data (set data property on the fetch parameter object)");
			} else if (this._data && !this._arrayOfItems) {
				this._arrayOfItems = [];
				this._itemsByIdentity = {};
				this._createdItemsByIdentity = {};
				this._deletedItemsByIdentity = {};
				this._updatedItemsByIdentity = {};
				this._idNumCounter = 1;

				if (!this._identifierPropName) {
					this._identifierPropName = this._itemNumPropName;
				}
				this._loadItems();
				this._data = null;
				findCallback(this._duplicateArray(this._arrayOfItems), keywordArgs);
			} else if (this._arrayOfItems) {
				findCallback(this._duplicateArray(this._arrayOfItems), keywordArgs);
			} else {
				throw new Error("_fetchItems:  this should never occur: bug in ObjectArrayStore");
			}
		},
		
		getFeatures: function(){
			return {
				'dojo.data.api.Read': true,
				'dojo.data.api.Write': true,
				'dojo.data.api.Identity': true,
				'dojo.data.api.Notification': true
			};
		},

		close: function(/*dojo.data.api.Request || keywordArgs || null */ request){
			this._arrayOfItems = [];
			this._itemsByIdentity = {};
			this._identifierPropName = "";
			this._labelPropName = "";
			this._idNumCounter = 1;
			this._createdItemsByIdentity = {};
			this._deletedItemsByIdentity = {};
			this._updatedItemsByIdentity = {};

		},

		getLabel: function(/* item */ item){
			this._assertIsItem(item);
			if (!this._labelPropName) { 
				throw new Error('ObjectArrayStore: getLabel(): store has no configured label property');
			}
			return item[this._labelPropName];
		},

		getLabelAttributes: function(/* item */ item){
			this._assertIsItem(item);
			return (this._labelPropName) ? this._labelPropName : null;
		},

		getIdentity: function(/* item */ item){
			//	summary:
			//		Returns a unique identifier for an item.  The return value will be
			//		either a string or something that has a toString() method (such as,
			//		for example, a dojox.uuid.Uuid object).
			//	item:
			//		The item from the store from which to obtain its identifier.
			//	exceptions:
			//		Conforming implementations may throw an exception or return null if
			//		item is not an item.
			//	example:
			//	|	var itemId = store.getIdentity(kermit);
			//	|	assert(kermit === store.findByIdentity(store.getIdentity(kermit)));
			this._assertIsItem(item);
			return item[this._identifierPropName];
		},

		getIdentityAttributes: function(/* item */ item){
			var attributes = null;
			if (this._identifierPropName == this._itemNumPropName) {
				return null;
			}
			return [this._identifierPropName];
		},


		fetchItemByIdentity: function(/* object */ keywordArgs){
			if (!keywordArgs.identity) {
				throw new Error("ObjectArrayStore: fetchItemByIdentity() was not passed an identity parameter");
			}
			var item = this._itemsByIdentity[keywordArgs.identity];
			if (item === undefined) {
				item = null;
			}
			if (keywordArgs.onItem) {
				var scope = keywordArgs.scope?keywordArgs.scope:dojo.global;
				keywordArgs.onItem.call(scope, item);
			}
		},

		newItem: function(/* Object? */ keywordArgs, /*Object?*/ parentInfo){
			
			if ( (!dojo.isObject(keywordArgs)) || dojo.isArray(keywordArgs)) {
				throw new Error("ObjectArrayStore: newItem() was not passed an Object");
			}
			
			//get or create an identity
			var newIdentity = null;
			if (this._identifierPropName == this._itemNumPropName) {
				newIdentity = this._idNumCounter++;
			} else {
				newIdentity = keywordArgs[this._identifierPropName];
				if (typeof newIdentity === "undefined"){
					throw new Error("ObjectArrayStore: newItem() was not passed an identity for the new item");
				}
				if (dojo.isArray(newIdentity)){
					throw new Error("ObjectArrayStore: newItem() was not passed an single-valued identity");
				}
			}

			//do this check even if we generate our own identities... just to be sure
			if (this._itemsByIdentity[newIdentity]
			    || this._createdItemsByIdentity[newIdentity]
			    || this._deletedItemsByIdentity[newIdentity]) {
				throw new Error("ObjectArrayStore: newItem() was passed an object with an invalid identity");
			}
			
			var newItem = keywordArgs;
			//assign the ID if we had to generate it ourselves
			if (this._identifierPropName == this._itemNumPropName) {
				newItem[this._identifierPropName] = newIdentity;
			}
			
			this._arrayOfItems.push(newItem);
			this._itemsByIdentity[newIdentity] = newItem;
			this._createdItemsByIdentity[newIdentity] = newItem;
			this.onNew(newItem);
			return newItem; // item
		},

		deleteItem: function(/* item */ item){
			this._assertItem(item);
			var id = item[this._identifierPropName];

			//remove out of array of items
			var index = dojo.indexOf(this._arrayOfItems, item);
			this._arrayOfItems.splice(index, 1);

			//remove out of by Id hashmap
			delete this._itemsByIdentity[id];

			//add to deleted items map.
			this._deletedItemsByIdentity[id] = item;
			
			this.onDelete(item);
			return false; // boolean
		},

		setValue: function(	/* item */ item, 
							/* string */ attribute,
							/* almost anything */ value){
			//	summary:
			//		Sets the value of an attribute on an item.
			//		Replaces any previous value or values.
			//
			//	item:
			//		The item to modify.
			//	attribute:
			//		The attribute of the item to change represented as a string name.
			//	value:
			//		The value to assign to the item.
			//
			//	exceptions:
			//		Throws an exception if *item* is not an item, or if *attribute*
			//		is neither an attribute object or a string.
			//		Throws an exception if *value* is undefined.
			//	example:
			//	|	var success = store.set(kermit, "color", "green");
			this._assertIsItem(item);
			this._assertIsAttribute(attribute);
			
			if (value === undefined) {
				throw new Error("ObjectArrayStore:  setValue() must be called with an item, attribute and a value");
			}
			
			var oldvalue = item[attribute];
			item[attribute] = value;
			
			if (!this._updatedItemsByIdentity[item[this._identifierPropName]]) {
				this._updatedItemsByIdentity[item[this._identifierPropName]] = item;
			}
			this.onSet(item, attribute, oldvalue, value);
			return true;
		},

		setValues: function(/* item */ item,
							/* string */ attribute, 
							/* array */ values){
			return this.setValue(item, attribute, values);
		},

		unsetAttribute: function(	/* item */ item, 
									/* string */ attribute){
			this._assertIsItem(item);
			this._assertIsAttribute(attribute);

			var oldValue = undefined;
			if (item[attribute] !== undefined) {
				oldValue = item[attribute];
				delete item[attribute];
			}
			this.onSet(item, attribute, oldValue, undefined);
			return true;
		},

		save: function(/* object */ keywordArgs){
			//	summary:
			//		Saves to the server all the changes that have been made locally.
			//		The save operation may take some time and is generally performed
			//		in an asynchronous fashion.  The outcome of the save action is 
			//		is passed into the set of supported callbacks for the save.
			//   
			//	keywordArgs:
			//		{
			//			onComplete: function
			//			onError: function
			//			scope: object
			//		}
			//
			//	The *onComplete* parameter.
			//		function();
			//
			//		If an onComplete callback function is provided, the callback function
			//		will be called just once, after the save has completed.  No parameters
			//		are generally passed to the onComplete.
			//
			//	The *onError* parameter.
			//		function(errorData); 
			//
			//		If an onError callback function is provided, the callback function
			//		will be called if there is any sort of error while attempting to
			//		execute the save.  The onError function will be based one parameter, the
			//		error.
			//
			//	The *scope* parameter.
			//		If a scope object is provided, all of the callback function (
			//		onComplete, onError, etc) will be invoked in the context of the scope
			//		object.  In the body of the callback function, the value of the "this"
			//		keyword will be the scope object.   If no scope object is provided,
			//		the callback functions will be called in the context of dojo.global.  
			//		For example, onComplete.call(scope) vs. 
			//		onComplete.call(dojo.global)
			//
			//	returns:
			//		Nothing.  Since the saves are generally asynchronous, there is 
			//		no need to return anything.  All results are passed via callbacks.
			//	example:
			//	|	store.save({onComplete: onSave});
			//	|	store.save({scope: fooObj, onComplete: onSave, onError: saveFailed});
			throw new Error('Unimplemented API: dojo.data.api.Write.save');
		},

		revert: function(){
			//	summary:
			//		Discards any unsaved changes.
			//	description:
			//		Discards any unsaved changes.
			//
			//	example:
			//	|	var success = store.revert();
			throw new Error('Unimplemented API: dojo.data.api.Write.revert');
			return false; // boolean
		},

		_isEmpty: function(arrayOrMap) {
			var isempty = true;
			for (var k in arrayOrMap) {
				isempty = false;
				break;
			}
			return isempty;
		},
		
		isDirty: function(/* item? */ item){
			var isdirty = false;
			if (item) {
				this._assertIsItem(item);
				var id = item[this._identifierPropName];
				isdirty = (new Boolean(this._createdItemsByIdentity[id]
					|| this._updatedItemsByIdentity[id]
					|| this._deletedItemsByIdentity[id])).valueOf();
			} else {
				isdirty = this._isEmpty(this._createdItemsByIdentity)
				         || this._isEmpty(this._updatedItemsByIdentity)
				         || this._isEmpty(this._deletedItemsByIdentity);
			}
			return isdirty;
		},
		
		/* dojo.data.api.Notification */

		onSet: function(/* item */ item, 
						/*attribute-name-string*/ attribute, 
						/*object | array*/ oldValue,
						/*object | array*/ newValue){
			// summary: See dojo.data.api.Notification.onSet()
			
			// No need to do anything. This method is here just so that the 
			// client code can connect observers to it.
		},

		onNew: function(/* item */ newItem, /*object?*/ parentInfo){
			// summary: See dojo.data.api.Notification.onNew()
			
			// No need to do anything. This method is here just so that the 
			// client code can connect observers to it. 
		},

		onDelete: function(/* item */ deletedItem){
			// summary: See dojo.data.api.Notification.onDelete()
			
			// No need to do anything. This method is here just so that the 
			// client code can connect observers to it. 
		}
	}
);

dojo.extend(org.ppwcode.dojo.dojo.data.ObjectArrayStore,dojo.data.util.simpleFetch);
