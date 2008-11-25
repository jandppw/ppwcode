dojo.provide("org.ppwcode.dojo.dijit.form.PpwLocalizedTextBox");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("org.ppwcode.dojo.dojox.DataDropDown")

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
      this._localeFilteringSelect.setValue(param.locale);
      this._descriptionTextBox.setValue(param.description);
    },

    getValue: function(){
      return { locale: this._localeFilteringSelect.getValue(), description: this._descriptionTextBox.getValue() };
    },

//    postCreate: function() {
//      console.debug("postCreate");
//     this.inherited(arguments);
//     var dropDown = new dijit.Menu();
//     this._localeDropDownButton.dropDown = dropDown;
//     dojo.forEach(this._availableLocales, function(x){
//       dropDown.addChild(new dijit.MenuItem({label: x}));
//     });
//    }


    setAttribute: function(/*String*/ attr, /*anything*/ value){
      this.inherited(arguments);
      switch(attr){
        case "disabled":
          this._localeFilteringSelect.setAttribute("disabled", value);
          this._descriptionTextBox.setAttribute("disabled", value);
          break;
        default:
          console.log("PpwLocalizedTextBox.setAttribute with "+attr);
      }
    }

  }
);
