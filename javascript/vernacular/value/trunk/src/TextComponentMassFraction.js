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

define(["dojo/_base/declare", "./MassFraction", "./_QualifiedValue",
        "ppwcode-util-oddsAndEnds/js", "./UndeterminedMassFraction", "dojo/_base/lang", "module"],
  function(declare, MassFraction, _QualifiedValue,
           js, UndeterminedMassFraction, lang, module) {

      var TextComponentMassFraction = declare([MassFraction, _QualifiedValue], {

        _c_invar: [
          function() {return this._c_prop_mandatory("componentName");},
          function() {return this._c_prop_string("componentName");},
          function() {return this._c_prop_mandatory("order");},
          function() {return this._c_prop_number("order");}
        ],

        // order: Number
        order: null,

        // componentName: String
        //   Textual representation of the component.
        componentName: null,

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return props /* exists and not null */;});
          this._c_pre(function() {return js.typeOf(props) === "object";});
          this._c_pre(function() {return this._c_prop_mandatory(props, "scalarValue");});
          this._c_pre(function() {return this._c_prop_number(props, "scalarValue");});
          this._c_pre(function() {return this._c_prop_mandatory(props, "unit");});
          this._c_pre(function() {return this._c_prop_string(props, "unit");});
          this._c_pre(function() {return this.getSupportedUnits().contains(props.unit);});
          this._c_pre(function() {return this._c_prop_mandatory(props, "componentName");});
          this._c_pre(function() {return this._c_prop_string(props, "componentName");});
          this._c_pre(function() {return this._c_prop_mandatory(props, "order");});
          this._c_pre(function() {return this._c_prop_number(props, "order");});

          this.componentName = props.componentName;
          this.order = props.order;
        },

        equals: function(/*ScalarQuantitativeValue*/ other) {
          return this.inherited(arguments) && this.componentName === other.componentName;
          // we don't take the order into account
        },

        getQualifier: function() {
          return this.componentName;
        },

        add: function(/*Fraction*/ other) {
          var sum = this.scalarValue + other.getScalarValueAs(this.unit);
          if (this.component = other.component) {
            return new (this.constructor)({
              scalarValue: sum,
              unit: this.unit,
              componentName: this.componentName,
              order: Math.min(this.order, other.order)
            });
          }
          else {
            return new UndeterminedMassFraction({scalarValue: sum, unit: this.unit});
          }
        },

        _extendJsonObject: function(/*Object*/ json) {
          json.componentName = this.componentName;
          json.order = this.order;
        },

        _stateToString: function(/*Array of String*/ toStrings) {
          toStrings.push("componentName: " + this.componentName);
          toStrings.push("order: " + this.order);
        }

      });

      TextComponentMassFraction.format = MassFraction.format;
      TextComponentMassFraction.parse = lang.partial(MassFraction.parse, TextComponentMassFraction);
    // MUDO componentName, order

      TextComponentMassFraction.mid = module.id;

      return TextComponentMassFraction;
    }
);
