dojo.provide("org.ppwcode.dojo.dijit.form.DurationNumberSpinner");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.form.TextBox");
dojo.require("org.ppwcode.dojo.dojox.DataDropDown")

dojo.declare(
  "org.ppwcode.dojo.dijit.form.DurationNumberSpinner",
  [dijit._Widget, dijit._Templated],
  {
    // summary:
    //    DurationNumberSpinner is a NumberSpinner for setting
    //    a duration.
    // description:
    //    DurationNumberSpinner currently provides a number spinner
    //    for setting a duration in hours.  The spinner increases or
    //    decreases the duration with increments of 30 minutes.  The
    //    duration is shown as a floating point number with 2 decimal
    //    places.

    templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/form/templates/DurationNumberSpinner.html"),

    widgetsInTemplate: true,

    initialValue: 0,

    postCreate: function() {
      this._numberSpinner.setAttribute("constraints", {min:0, max:10000, places:2});
      this._numberSpinner.setAttribute("smallDelta", 0.5 );
      this._numberSpinner.setAttribute("largeDelta", 2 );
      this._numberSpinner._resetValue = this.initialValue;
    },

    setValue: function(/*object*/ param){
      if (!param) {
       this._numberSpinner.reset();
      } else {
        var hours = 1.0 * param.milliseconds / 3600000;
        this._numberSpinner.setValue(hours);
      }
    },

    getValue: function(){
      var millis = 3600000 * this._numberSpinner.getValue();
      var d = new Duration();
      d.milliseconds = millis;
      return d;
    },

    setAttribute: function(/*String*/ attr, /*anything*/ value){
      this.inherited(arguments);
      switch(attr){
        case "disabled":
          this._numberSpinner.setAttribute("disabled", value);
          break;
        //default:
          //console.log("DurationNumberSpinner.setAttribute with "+attr);
      }
    }

  }
);
