dojo.provide("org.ppwcode.dojo.dijit.address.AddressForm");

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
    
    //I18N labels
    _streetLabel: null,
    _postalCodeLabel: null,
    _cityNameLabel: null,
    _countryLabel: null,
    
    postCreate: function() {
	  this.inherited(arguments);
    },
    
    constructor: function() {
	  var localizationbundle = dojo.i18n.getLocalization("org.ppwcode.dojo.dijit.address", "AddressBundle");
	  this._streetLabel = localizationbundle.streetLabel;
	  this._postalCodeLabel = localizationbundle.postalCodeLabel;
	  this._cityNameLabel = localizationbundle.cityNameLabel;
	  this._countryLabel = localizationbundle.countryLabel;
    },
	
    setValue: function(/*AddressObject*/ param){
      this._street.setValue(param.street);
      this._postalCode.setValue(param.postalCode);
      this._cityName.setValue(param.city);
      this._country.setValue(param.country);
    },

    getValue: function(){
      //create new address object based on data in widget.
      var address = null;
//      period.begin = this._addTimeToDate(dojo.clone(this._dateBox.value), this._startTimeBox.value);
//      period.end = this._addTimeToDate(dojo.clone(this._dateBox.value), this._endTimeBox.value);
//      period.timeZone = this._timeZone;
      return address;
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
