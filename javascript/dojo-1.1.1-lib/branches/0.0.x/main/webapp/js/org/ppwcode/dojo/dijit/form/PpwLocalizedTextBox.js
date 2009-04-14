dojo.provide("org.ppwcode.dojo.dijit.form.PpwLocalizedTextBox");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.form.TextBox");
dojo.require("org.ppwcode.dojo.dojox.DataDropDown");

dojo.declare(
  "org.ppwcode.dojo.dijit.form.PpwLocalizedTextBox",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    PpwLocalizedTextBox is a text box with a locale added in front of it.
    //description:
    //    The PpwLocalizedTextBox is a text box which has a combobox added in
    //    front of it to specify the locale of the text in the text box.
    //

    templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/PpwLocalizedTextBox.html"),

    widgetsInTemplate: true,

    localesDataStore: "",


    setValue: function(/*object*/ param){
      this._localeDropDownSelect.setValue(param.locale);
      this._descriptionTextBox.setValue(param.text);
    },

    getValue: function(){
      var s = new LocalizedString();
      s.locale = this._localeDropDownSelect.getValue();
      s.text = this._descriptionTextBox.getValue();
      return s;
    },

    setAttribute: function(/*String*/ attr, /*anything*/ value){
      this.inherited(arguments);
      switch(attr){
        case "disabled":
          this._localeDropDownSelect.setAttribute("disabled", value);
          this._descriptionTextBox.setAttribute("disabled", value);
          break;
        default:
          console.log("PpwLocalizedTextBox.setAttribute with "+attr);
      }
    }

  }
);
