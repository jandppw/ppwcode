dojo.provide("org.ppwcode.dojo.data.EjbRetrieveAllDataStore");

dojo.require("dojo.data.api.Identity");
dojo.require("dojo.data.api.Read");
dojo.require("dojo.data.util.simpleFetch");


dojo.declare("org.ppwcode.dojo.data.EjbRetrieveAllDataStore", null, {

  // public fields (parameters)
  dwrRetrieveFunction: null,
  label: "",
  localizedAttribute: "",
  startParams: [],
  dirty: false,

  // private fields (internal parameters)
  _localized: false,
  _locale: dojo.locale,

  _features: null,
  _loadInProgress: false,
  _loadFinished: false,

  _attributes: [],
  _items: [],
  _allItems: [],

  constructor: function(/*arguments */ args) {
    this._features = { 'dojo.data.api.Read': true, 'dojo.data.api.Identity': true };

    this.dwrRetrieveFunction = args.dwrRetrieveFunction;
    this.startParams = args.startParams;
    this.label = args.label;
    this.localizedAttribute = args.localizedAttribute;

    this._attributes = [this.label];			// e.g. ["Title", "Year", "Producer"]
    this._localized = this.localizedAttribute != null && this.localizedAttribute !== undefined;

    this.fetch(null);
  },

  getFeatures: function() {
    //	summary:
    //		See dojo.data.api.Read.getFeatures()
    //		See dojo.data.api.Identity.getFeatures()
    return this._features; //Object
  },

  _assertIsItem: function(/* item */ item) {
    //	summary:
    //      This function tests whether the item passed in is indeed an item in the store.
    //	item:
    //		The item to test for being contained by the store.
    if (!this.isItem(item)) {
      throw new Error("org.ppwcode.dojo.data.EjbRetrieveAllDataStore: a function was passed an item argument that was not an item");
    }
  },

  _assertIsAttribute: function(/* item || String */ attribute) {
    //	summary:
    //      This function tests whether the item passed in is indeed a valid 'attribute' like type for the store.
    //	attribute:
    //		The attribute to test for being contained by the store.
    if (!dojo.isString(attribute)) {
      throw new Error("org.ppwcode.dojo.data.EjbRetrieveAllDataStore: a function was passed an attribute argument that was not an attribute object nor an attribute name string");
    }
  },

  /***************************************
   dojo.data.api.Read API
   ***************************************/
  getValue: function(/* item */ item, /* attribute || attribute-name-string */ attribute, /* value? */ defaultValue) {
    //	summary:
    //      See dojo.data.api.Read.getValue()
    //		Note that for the CsvStore, an empty string value is the same as no value,
    // 		so the defaultValue would be returned instead of an empty string.
    console.log("Getting value");
    this._assertIsItem(item);
    this._assertIsAttribute(attribute);
    var itemValue = defaultValue;
    if (this.hasAttribute(item, attribute)) {
      var itemData = this._allItems[this.getIdentity(item)].value;
      itemValue = itemData[attribute];
      if (this._localized) {
        return getLocalizedValueForLocale(this._locale, this.localizedAttribute, itemValue);
      }
    }
    return itemValue; //String
  },

  getValues: function(/* item */ item, /* attribute || attribute-name-string */ attribute) {
    //	summary:
    //		See dojo.data.api.Read.getValues()
    // 		CSV syntax does not support multi-valued attributes, so this is just a
    // 		wrapper function for getValue().
    var value = this.getValue(item, attribute);
    return (value ? [value] : []); //Array
  },

  getAttributes: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Read.getAttributes()
    this._assertIsItem(item);
    var attributes = [];
    var itemData = this._allItems[this.getIdentity(item)];
    for (var i = 0; i < itemData.length; i++) {
      // Check for empty string values. CsvStore treats empty strings as no value.
      if (itemData[i] != "") {
        attributes.push(this._attributes[i]);
      }
    }
    return attributes; //Array
  },

  hasAttribute: function(/* item */ item, /* attribute || attribute-name-string */ attribute) {
    //	summary:
    //		See dojo.data.api.Read.hasAttribute()
    // 		The hasAttribute test is true if attribute has an index number within the item's array length
    // 		AND if the item has a value for that attribute. Note that for the CsvStore, an
    // 		empty string value is the same as no value.
    this._assertIsItem(item);
    this._assertIsAttribute(attribute);
    var itemData = this._allItems[this.getIdentity(item)].value;
    return (itemData[attribute] !== undefined);
  },

  containsValue: function(/* item */ item, /* attribute || attribute-name-string */ attribute, /* anything */ value) {
    //	summary:
    //		See dojo.data.api.Read.containsValue()
    var regexp = undefined;
    if (typeof value === "string") {
      regexp = dojo.data.util.filter.patternToRegExp(value, false);
    }
    return this._containsValue(item, attribute, value, regexp); //boolean.
  },

  _containsValue: function(/* item */ item, /* attribute || attribute-name-string */ attribute, /* anything */ value, /* RegExp?*/ regexp) {
    //	summary:
    //		Internal function for looking at the values contained by the item.
    //	description:
    //		Internal function for looking at the values contained by the item.  This
    //		function allows for denoting if the comparison should be case sensitive for
    //		strings or not (for handling filtering cases where string case should not matter)
    //
    //	item:
    //		The data item to examine for attribute values.
    //	attribute:
    //		The attribute to inspect.
    //	value:
    //		The value to match.
    //	regexp:
    //		Optional regular expression generated off value if value was of string type to handle wildcarding.
    //		If present and attribute values are string, then it can be used for comparison instead of 'value'
    var values = this.getValues(item, attribute);
    for (var i = 0; i < values.length; ++i) {
      var possibleValue = values[i];
      if (typeof possibleValue === "string" && regexp) {
        return (possibleValue.match(regexp) !== null);
      } else {
        //Non-string matching.
        if (value === possibleValue) {
          return true; // Boolean
        }
      }
    }
    return false; // Boolean
  },

  isItem: function(/* anything */ something) {
    //	summary:
    //		See dojo.data.api.Read.isItem()
    if (something) {
      var identity = something.identity;
      if (identity != null) {
        return true; //Boolean
      }
    }
    return false; //Boolean
  },

  isItemLoaded: function(/* anything */ something) {
    //	summary:
    //		See dojo.data.api.Read.isItemLoaded()
    return this.isItem(something); //Boolean
  },

  loadItem: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Read.loadItem()
    //	description:
    //		TODO implement for performance
    console.log("Loading item");
  },

  getLabel: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Read.getLabel()
    if (this.label && this.isItem(item)) {
      return this.getValue(item, this.label); //String
    }
    return undefined; //undefined
  },

  getLabelAttributes: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Read.getLabelAttributes()
    if (this.label) {
      return [this.label]; //array
    }
    return null; //null
  },

  close: function(/*dojo.data.api.Request || keywordArgs || null */ request) {
    //	summary:
    //		See dojo.data.api.Read.close()

    // no-op
  },

  // the parameters contain
  // on index 0: the parameters to fill
  // on index 1: the request object containing data on the query to execute
  fillParameterArray: function(/* the parameters to fill */ params) {
    // Override
    console.log("Override if parameters are needed for the dwr function");
  },

  _executeDwrFunction: function(/* Callback function */ errorCallBack, /* request */ queryParams) {
    var self = this;
    var callbackHandlerCorrect = function(data) {
      self._allItems = [];
      for (var i = 0; i < data.length; i++) {        
        self._allItems.push(self._createItemFromIndex(i, data[i]));
        self._items = self._allItems;
      }
    };

    queryParams.push({callback: callbackHandlerCorrect, errorHandler: errorCallBack});

    self.dwrRetrieveFunction.apply(self, queryParams);
  },

  _createItemFromIndex: function(/* int */ index, /* the value object */ value){
		var item = {};
		item.value = value;
		item.identity = index;
		return item; //Object
	},

  _fetchItems: function(	/* Object */ request, /* Function */ findCallback, /* Function */ errorCallback) {
		//	summary:
		//		See dojo.data.util.simpleFetch.fetch()
    console.log("Executing _fetchItems in EjbRetrieveAllDataStore");

    if (!this.dirty) {
      this._executeDwrFunction(errorCallback, this.startParams);
      this.dirty = true;
    } else if (request.query[this.label] != "*") {
      console.log("Do a dirty search in the fetched items !!");
      var queryParams = new Array();
      var parameters = [queryParams, request.query[this.label].substring(0, request.query[this.label].indexOf("*"))];

      this.fillParameterArray(parameters);
      this._items = [];

      for (var i = 0; i < this._allItems.length; i++) {
        var value = this.getValue(this._allItems[i], this.label);
        if (value.toLowerCase().indexOf(queryParams[0].toLowerCase()) == 0) {
          this._items.push(this._allItems[i]);
        }
      }
    } else {
      this._items = this._allItems;
    }

    findCallback(this._items, request);
  },  


  /***************************************
   dojo.data.api.Identity API
   ***************************************/
  getIdentity: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Identity.getIdentity()
    if (this.isItem(item)) {
      return item.identity; //String
    }
    return null; //null
  },

  fetchItemByIdentity: function(/* Object */ keywordArgs) {
    //	summary:
    //		See dojo.data.api.Identity.fetchItemByIdentity()

    //Hasn't loaded yet, we have to trigger the load.


    console.log("fetchItemByIdentity : " + keywordArgs);
  },

  getIdentityAttributes: function(/* item */ item) {
    //	summary:
    //		See dojo.data.api.Identity.getIdentifierAttributes()
    return "identity";
  }

});

//Mix in the simple fetch implementation to this class.
dojo.extend(org.ppwcode.dojo.data.EjbRetrieveAllDataStore, dojo.data.util.simpleFetch);