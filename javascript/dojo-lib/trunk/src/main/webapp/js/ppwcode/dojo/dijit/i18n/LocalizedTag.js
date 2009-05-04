dojo.provide("ppwcode.dojo.dijit.i18n.LocalizedTag");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");

dojo.declare(
  "ppwcode.dojo.dijit.i18n.LocalizedTag",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    A generic dijit to localize the content of a tag using a dojo localization bundle.
    //description:
    //    LocalizedTag.
    //
    templateString: '<span>PROGRAM ERROR !</span>',

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
    },

    postMixInProperties: function() {
      this.templateString = "<" + this.srcNodeRef.tagName + ">${value}</" + this.srcNodeRef.tagName + ">"
    }

  }
);