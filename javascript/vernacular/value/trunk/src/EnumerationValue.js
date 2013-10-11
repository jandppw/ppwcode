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

define(["dojo/_base/declare", "./_QualifiedValue", "module"],
    function(declare, _QualifiedValue, module) {

      var EnumerationValue = declare([_QualifiedValue], {
        // summary:
        //   Enumeration values can be used in multiple properties. The qualifier is
        //   the enumValue itself in that case.

        _c_invar: [
          function() {return this._c_prop_mandatory("enumValue");},
          function() {return this._c_prop_string("enumValue");}
        ],

        // enumValue: String
        //   The string that is the programmatic representation of this enum value.
        //   Also used in nls files.
        enumValue: null,

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return props;});
          this._c_pre(function() {return this._c_prop_mandatory(props, "enumValue");});
          this._c_pre(function() {return this._c_prop_string(props, "enumValue");});

          this.enumValue = props.enumValue;
        },

        compare: function(/*EnumerationValue*/ other) {
          this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(EnumerationValue));});

          if (!other) {
            return +1;
          }
          return this.enumValue < other.enumValue ? -1 : (this.enumValue === other.enumValue ? 0 : +1);
        },

        equals: function(/*EnumerationValue*/ other) {
          return this.inherited(arguments) && (this.enumValue === other.enumValue);
        },

        getQualifier: function() {
          return this.enumValue;
        },

        getValue: function() { // IDEA probably needs to be replaced with a formatter
          return this.enumValue;
        },

        _extendJsonObject: function(/*Object*/ json) {
          json.enumValue = this.enumValue;
        },

        _stateToString: function(/*Array of String*/ toStrings) {
          toStrings.push("enumValue: " + this.enumValue);
        }

      });

      EnumerationValue.format = function(eValue, options) {
        if (!eValue) {
          return (options && (options.na || options.na === "")) ? options.na : 'N/A';
        }
        return eValue.enumValue;
      };

      EnumerationValue.parse = function(EnumValueConstructor, str, options) {
        if (!str || str === "N/A") {
          return null;
        }
        return new EnumValueConstructor({enumValue: str});
      };

      EnumerationValue.mid = module.id;

      return EnumerationValue;
    }
);
