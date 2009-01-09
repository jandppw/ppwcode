dojo.provide("org.ppwcode.dojo.dijit.i18n.LocalizedField");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");

dojo.declare(
  "org.ppwcode.dojo.dijit.i18n.LocalizedField",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    todo
    //description:
    //    PeriodForm is a dijit
    //
    templateString: '<label>${value}</label>',
    
    property: 'none',
    bundle: null,
    value: '',

    constructor: function(arguments) {  
	  this.bundle = arguments.bundle;
	  this.property = arguments.property;
	  eval('this.value = this.bundle.' + this.property);
    }
  }
);
