dojo.provide("org.ppwcode.dojo.dijit.address.AddressForm");
dojo.experimental("org.ppwcode.dojo.dijit.address.AddressForm", "TODO: regular expression check on postal code");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ComboBox");
dojo.require("dojo.i18n");
dojo.requireLocalization("org.ppwcode.dojo.dijit.address", "AddressBundle");

dojo.declare(
  "org.ppwcode.dojo.dijit.address.AddressForm",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    This component is a Address Widget which will allow a user to fill in his address.
    //description:
    //    The component has a
    //
    templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/address/templates/AddressForm.html"),

    widgetsInTemplate: true,
    
    dataStore: null,
    
    //I18N labels
    _streetLabel: null,
    _postalCodeLabel: null,
    _cityNameLabel: null,
    _countryLabel: null,
    
   
    constructor: function(arguments) {    	
    	
	  var localizationbundle = dojo.i18n.getLocalization("org.ppwcode.dojo.dijit.address", "AddressBundle");
	  this._streetLabel = localizationbundle.streetLabel;
	  this._postalCodeLabel = localizationbundle.postalCodeLabel;
	  this._cityNameLabel = localizationbundle.cityNameLabel;
	  this._countryLabel = localizationbundle.countryLabel;
	  this.dataStore = arguments.dataStore;
    },
    
	postCreate: function(){
    	//pass the datastore to the country combobox
    	this._country.store = this.dataStore;
		this.inherited(arguments);
	},
	
    setValue: function(/*PostalAddress*/ param){
      this._street.setValue(param.$streetAddress);
      this._postalCode.setValue(param.$postalCode);
      this._cityName.setValue(param.$city);
      this._country.setValue(param.$locale);
    },

    getValue: function(){
      //create new address object based on data in widget.
      var postalAddress = new PostalAddress();
      postalAddress.postalCode = this._postalCode.getValue(); 
      postalAddress.locale = this._country.getValue();
      postalAddress.city = this._cityName.getValue();
      postalAddress.streetAddress = this._street.getValue(); 
      return postalAddress;
    },

    setAttribute: function(/*String*/ attr, /*anything*/ value){
      this.inherited(arguments);
      switch(attr){
        case "disabled":
          this._street.setAttribute("disabled", value);
          this._postalCode.setAttribute("disabled", value);
          this._cityName.setAttribute("disabled", value);
          this._country.setAttribute("disabled", value);          
          break;
        default:
          console.log("AddressForm.setAttribute with " + attr);
      }
    }
  }
);
