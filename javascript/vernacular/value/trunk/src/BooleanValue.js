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

define(["dojo/_base/declare", "./EnumerationValue", "module"],
    function(declare, EnumerationValue, module) {

      var BooleanValue = declare([EnumerationValue], {

        _c_invar: [
          function() {return this.enumValue === "T" || this.enumValue === "F";}
        ],

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return props.enumValue === "T" || props.enumValue === "F";});
        },

        compare: function(/*EnumerationValue*/ other) {
          this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(EnumerationValue));});

          if (!other) {
            return +1;
          }
          if (this.enumValue === other.enumValue) {
            return 0;
          }
          if (this.enumValue === "F") {
            return -1;
          }
          return +1;
        },

        toBoolean: function() {
          return this.enumValue === "T";
        }

      });

      BooleanValue.format = function(/*BooleanValue*/ bv) {
        return bv ? (bv.toBoolean() ? "true" : "false") : null;
      };

      BooleanValue.parse = function(/*String*/ str) {
        return str ? (str === "true" ? new BooleanValue({enumValue: "T"}) : new BooleanValue({enumValue: "F"})) : null;
      };

      BooleanValue.mid = module.id;

      return BooleanValue;
    }
);
