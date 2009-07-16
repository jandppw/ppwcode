dojo.provide("ppwcode.dojo.dijit.form.AddressBox");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.FilteringSelect");
dojo.requireLocalization("ppwcode.dojo.dijit.form", "AddressBox");

dojo.declare("ppwcode.dojo.dijit.form.AddressBox", [ dijit._Widget, dijit._Templated ], {
  // summary:
  // AddressBox is a generic dijit for address input.
  // description:
  // AddressBox is a generic dijit for address input consisting of several standard dojo widget
  // for input of streetAddress, city, postalCode and country.
  templatePath : dojo.moduleUrl("ppwcode", "dojo/dijit/form/templates/AddressBox.html"),
  widgetsInTemplate : true,

  countryDataStore : "",
  constructorFunction : null,

  // I18N labels
  _streetLabel : null,
  _postalCodeLabel : null,
  _cityNameLabel : null,
  _countryLabel : null,

  constructor : function() {
    var localizationbundle = dojo.i18n.getLocalization("ppwcode.dojo.dijit.form", "AddressBox");
    this._streetLabel = localizationbundle.streetLabel;
    this._postalCodeLabel = localizationbundle.postalCodeLabel;
    this._cityNameLabel = localizationbundle.cityNameLabel;
    this._countryLabel = localizationbundle.countryLabel;
  },

  setValue : function(/* PostalAddress */address) {
    this._streetBox.setValue(address.streetAddress);
    this._cityBox.setValue(address.city);
    if (address.postalCode != null) {
      this._postalCodeBox.setValue(address.postalCode.identifier);
      this._countryBox.setValue(address.postalCode.country);
    }
  },

  getValue : function() {
    var address = new this.constructorFunction();
    var postalCode;
    address.locale = dojo.locale;
    address.streetAddress = this._streetBox.value;
    address.city = this._cityBox.value;
    var country = this._countryBox.value;
    // TODO (dvankeer): Postal code construction should not be hard-coded like this
    if (country == "BE") {
      postalCode = new BePostalCode();
    } else {
      postalCode = new WildCardPostalCode();
      postalCode.country = country;
    }
    postalCode.identifier = this._postalCodeBox.value;
    address.postalCode = postalCode;
    return address;
  },

  setAttribute : function(/* String */attr, /* anything */value) {
    this.inherited(arguments);
    switch (attr) {
      case "disabled":
        this._streetBox.setAttribute("disabled", value);
        this._postalCodeBox.setAttribute("disabled", value);
        this._cityBox.setAttribute("disabled", value);
        this._countryBox.setAttribute("disabled", value);
        break;
      default:
        console.log("AddressBox setAttribute with " + attr);
    }
  }

});
