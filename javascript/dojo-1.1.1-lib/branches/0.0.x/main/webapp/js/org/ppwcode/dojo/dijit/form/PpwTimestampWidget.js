dojo.provide("org.ppwcode.dojo.dijit.form.PpwTimestampWidget");

dojo.declare(
  "org.ppwcode.dojo.dijit.form.PpwTimestampWidget",
	dijit._Widget,
  {
    //summary:
    //    PpwTimestampTextBox is a text box that shows a timestamp as a number which
    //    represents the number of milliseconds.
    //description:
    //    PpwTimestampTextBox is a text box that shows a timestamp as a number which
    //    represents the number of milliseconds.
    //

    millis : 0,

    setValue: function(/*object*/ param){
      if (dojo.isString(param)) {
        console.log("param is a string ["+param+"]");
      }
      if (param == null) {
        console.log("param is null or is undefined");
      }
      if ((param != "") && (param != null)) {
        this.millis = param.getTime();
        // this.inherited(this.millis);
      }
    },

    getValue: function(){
      // this.millis = dojo.number.parse(this.inherited());
      return new Date(this.millis);
    }

  }
);
