/*
Copyright 2013 - $Date $ by PeopleWare n.v.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value", "dojo/number", "dojo/_base/lang", "module"],
  function(declare, Value, number, lang, module) {

    var DoubleValue = declare([Value], {

      _c_invar: [
        function() {return this._c_prop_mandatory("numericValue");},
        function() {return this._c_prop_number("numericValue");}
      ],

      constructor: function(/*Object*/ props) {
        this._c_pre(function() {return props;});
        this._c_pre(function() {return this._c_prop_mandatory(props, "numericValue");});
        this._c_pre(function() {return this._c_prop_number(props, "numericValue");});

        this.numericValue = props.numericValue;
      },

      compare: function(/*DoubleValue*/ other) {
        this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(DoubleValue));});

        if (!other) {
          return +1;
        }
        return this.numericValue < other.numericValue ? -1 : (this.numericValue === other.numericValue ? 0 : +1);
      },

      equals: function(/*DoubleValue*/ other) {
        return this.inherited(arguments) && (this.numericValue === other.numericValue);
      },

      add: function(/*DoubleValue*/ other) {
        var sum = this.numericValue + other.numericValue;
        return new (this.constructor)({numericValue: sum});
      },

      getValue: function() {
        return this.numericValue + "";
      },

      _extendJsonObject: function(/*Object*/ json) {
        json.numericValue = this.numericValue;
      },

      _stateToString: function(/*Array of String*/ toStrings) {
        toStrings.push("numericValue: " + this.numericValue);
      }
    });

    DoubleValue.format = function(doubleValue, options) {
      if (!doubleValue) {
        return (options && (options.na || options.na === "")) ? options.na : 'N/A';
      }
      else {
        return number.format(doubleValue.numericValue, options);
      }
    };

    DoubleValue.parse = function(str, options) {
      if (!str || str === (options && options.na ? options.na : 'N/A')) {
        return null;
      }
      else {
        // with options.places, we need to supply str with this exact decimal places!!! 0's aren't added, number isn't truncated
        var opt = options ? lang.clone(options) : {};
        delete opt.places;
        var d = number.parse(str, opt);
        return new DoubleValue({numericValue: d});
      }
    };

    DoubleValue.mid = module.id;

    return DoubleValue;
    }
);
