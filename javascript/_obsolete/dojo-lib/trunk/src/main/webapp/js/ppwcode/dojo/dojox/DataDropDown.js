dojo.provide("ppwcode.dojo.dojox.DataDropDown");

dojo.require("dojox.form.DropDownSelect");

// code found at http://www.toonetown.com/projects/downloads/DataDropDown.js

dojo.declare(
    "ppwcode.dojo.dojox.DataDropDown",
    dojox.form.DropDownSelect,
    {
      store: null,

      _onNewItem: function(item){
        var store = this.store;
        this.addOption(store.getIdentity(item), store.getLabel(item));
      },

      _onDeleteItem: function(item){
        var store = this.store;
       this.removeOption(store.getIdentity(item));
      },

      _onSetItem: function(item){
        var store = this.store;
        this.setOptionLabel(store.getIdentity(item), store.getLabel(item));
      },

      constructor: function(kwArgs){
        if(kwArgs.value){
          this._origVal = kwArgs.value;
        }
      },

      postCreate: function(){
        this.inherited(arguments);
        if(this.store && this.store.getFeatures()["dojo.data.api.Notification"])
          {
            var store = this.store;
            this.connect(store, "onNew", "_onNewItem");
            this.connect(store, "onDelete", "_onDeleteItem");
            this.connect(store, "onSet", "_onSetItem");
          }
      },

      startup: function(){
        this.inherited(arguments);
        if(this.store){
          var store = this.store;
          var fx = function(item){
            this.addOption(store.getIdentity(item), store.getLabel(item));
          };
          var fxComp = function(items){
            if(this._origVal){
              this.setAttribute("value", this._origVal);
            }
          };
          store.fetch({onItem: fx, onComplete: fxComp, scope: this});
        }
      }

    });
