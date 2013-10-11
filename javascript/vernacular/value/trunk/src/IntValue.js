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

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value", "dojo/_base/lang", "dojo/number", "module"],
  function(declare, Value, lang, number, module) {

    var IntValue = declare([Value], {

      _c_invar: [
        function() {return this._c_prop_mandatory("integerValue");},
        function() {return this._c_prop_int("integerValue");}
      ],

      constructor: function(/*Object*/ props) {
        this._c_pre(function() {return props;});
        this._c_pre(function() {return this._c_prop_mandatory(props, "integerValue");});
        this._c_pre(function() {return this._c_prop_int(props, "integerValue");});

        this.integerValue= props.integerValue;
      },

      compare: function(/*IntValue*/ other) {
        this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(IntValue));});

        if (!other) {
          return +1;
        }
        return this.integerValue < other.integerValue ? -1 : (this.integerValue === other.integerValue ? 0 : +1);
      },

      equals: function(/*IntValue*/ other) {
        return this.inherited(arguments) && (this.integerValue === other.integerValue);
      },

      add: function(/*IntValue*/ other) {
        var sum = this.integerValue + other.integerValue;
        return new (this.constructor)({integerValue: sum});
      },

      getValue: function() {
        return this.integerValue + "";
      },

      _extendJsonObject: function(/*Object*/ json) {
        json.integerValue = this.integerValue;
      },

      _stateToString: function(/*Array of String*/ toStrings) {
        toStrings.push("integerValue: " + this.integerValue);
      }
    });

    IntValue.format = function(intValue, options) {
      if (!intValue) {
        return (options && (options.na || options.na === "")) ? options.na : 'N/A';
      }
      else {
        var opt = options ? lang.clone(options) : {};
        delete opt.places;
        opt.fractional = false;
        return number.format(intValue.integerValue, opt);
      }
    };

    IntValue.parse = function(str, options) {
      if (!str || str === (options && options.na ? options.na : 'N/A')) {
        return null;
      }
      else {
        // with options.places, we need to supply str with this exact decimal places!!! 0's aren't added, number isn't truncated
        var opt = options ? lang.clone(options) : {};
        delete opt.places;
        opt.fractional = false;
        var i = number.parse(str, opt);
        return new IntValue({integerValue: i});
      }
    };

    IntValue.mid = module.id;

    return IntValue;
  }
);
