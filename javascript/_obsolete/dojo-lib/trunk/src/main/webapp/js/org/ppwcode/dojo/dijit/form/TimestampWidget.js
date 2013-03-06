dojo.provide("org.ppwcode.dojo.dijit.form.TimestampWidget");

dojo.declare(
  "org.ppwcode.dojo.dijit.form.TimestampWidget",
	dijit._Widget,
  {
    //summary:
    //    TimestampWidget is a text box that shows a timestamp as a number which
    //    represents the number of milliseconds.
    //description:
    //    TimestampWidget is a text box that shows a timestamp as a number which
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
