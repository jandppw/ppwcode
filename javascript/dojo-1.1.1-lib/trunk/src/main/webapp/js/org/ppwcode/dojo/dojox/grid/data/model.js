dojo.provide('org.ppwcode.dojo.dojox.grid.data.model');
dojo.require('dojox.grid._data.model');
dojo.require('dijit._Widget');

dojo.declare("org.ppwcode.dojo.dojox.grid.data.PpwObjects",
        [dojox.grid.data.Table, dijit._Widget],
{
    constructorFunction: null,

    inFields: null,
    
    allChange: function() {
        this.inherited(arguments);
        this.notify("FieldsChange");
    },
    
    autoAssignFields: function() {
    	if (this.inFields) {
    		//console.log("using this.inFields: " + this.inFields);
    		for (var i = 0; i < this.inFields.length; i++) {
    			var field = this.fields.get(i++);
    			if (!dojo.isString(field.key)) {
    				field.key = this.inFields[i];
    			}
    		}
    	} else if (this.constructorFunction && dojo.isFunction(this.constructorFunction)) {
            //console.log("constructorFunction is " + this.constructorFunction);
    		var i = 0;
        	for (var f in new this.constructorFunction()) {
        		//console.log("creating field: " + f);
        		var field = this.fields.get(i++);
        		if (!dojo.isString(field.key)) {
        			field.key = f;
        		}
        	}
        } else {
        	//console.log("normal autoAssignFields");
       		var d = this.data[0], i = 0, field;
       		for(var f in d){
       			field = this.fields.get(i++);
       			if (!dojo.isString(field.key)){
       				field.key = f;
       			}
       		}
    	}
	},
    setData: function(inData) {
        this.data = (inData || []);
        this.autoAssignFields();
        this.allChange();
    },
    getDatum: function(inRowIndex, inColIndex) {
        return this.data[inRowIndex][this.fields.get(inColIndex).key];
    },
    setDatum: function(inDatum, inRowIndex, inColIndex) {
        this.data[inRowIndex][this.fields.get(inColIndex).key] = inDatum;
        this.datumChange(inDatum, inRowIndex, inColIndex);
    },
    copyRow: function(inRowIndex) {
        var obj = dojo.clone(this.getRow(inRowIndex));
        //console.log("PpwObjects copyRow:");
        //for (var f in obj) {
        //    console.log("obj[" + f + "] = " + obj[f]);
        //}
        return obj;
    }
});