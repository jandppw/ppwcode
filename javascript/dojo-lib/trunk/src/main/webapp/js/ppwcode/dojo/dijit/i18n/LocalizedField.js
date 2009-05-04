dojo.provide("ppwcode.dojo.dijit.i18n.LocalizedField");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");

dojo.declare(
  "ppwcode.dojo.dijit.i18n.LocalizedField",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    todo
    //description:
    //    PeriodForm is a dijit
    //
    templateString: '<label for="${fieldid}">${value}</label',
    
    fieldid: 'none',
    bundle: null,
    value: '',

    constructor: function(arguments) {  
	  this.bundle = arguments.bundle;
	  this.fieldid = arguments.fieldid;
	  eval('this.value = this.bundle.' + this.fieldid);
    }
  }
);