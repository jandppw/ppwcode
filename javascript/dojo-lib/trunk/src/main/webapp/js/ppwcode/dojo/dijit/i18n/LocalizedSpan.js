dojo.provide("ppwcode.dojo.dijit.i18n.LocalizedSpan");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");

dojo.declare(
  "ppwcode.dojo.dijit.i18n.LocalizedSpan",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    A simple string with contains a localized message, fetched from a dojo localization bundle.
    //description:
    //    LocalizedString.
    //
    templateString: '<span>${value}</span>',

    bundle: null,
    key: '',
    value: '',

    constructor: function(arguments) {
      this.bundle = arguments.bundle;
      this.key = arguments.key;

      if (this.bundle == null) {
        this.value = 'ERROR: bundle not defined.';
      } else {
        if (this.bundle[this.key] == null) {
          this.value = 'ERROR: key [' + this.key + '] not defined.';
        } else {
          this.value = this.bundle[this.key];
        }
      }
    }
  }
);