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

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value",
        "ppwcode-util-collections/ArraySet", "ppwcode-util-oddsAndEnds/js", "dojo/number", "dojo/_base/lang",
        "ppwcode-vernacular-semantics/ParseException",
        "module"],
    function(declare, Value,
             Set, js, number, lang,
             ParseException,
             module) {

      var ScalarQuantitativeValue = declare([Value], {

        _c_invar: [
          function() {return this._c_prop_mandatory("scalarValue");},
          function() {return this._c_prop_number("scalarValue");},
          function() {return this._c_prop_mandatory("unit");},
          function() {return this._c_prop_string("unit");},
          function() {return this.getSupportedUnits().contains(this.unit);}
        ],

        // scalarValue: Number
        //   The scalar value of this value. Mandatory.
        scalarValue: null,

        // unit: String
        //   The string representing the unit of this value. Mandatory.
        unit: null,

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return props /* exists and not null */;});
          this._c_pre(function() {return js.typeOf(props) === "object";});
          this._c_pre(function() {return this._c_prop_mandatory(props, "scalarValue");});
          this._c_pre(function() {return this._c_prop_number(props, "scalarValue");});
          this._c_pre(function() {return this._c_prop_mandatory(props, "unit");});
          this._c_pre(function() {return this._c_prop_string(props, "unit");});
          this._c_pre(function() {return this.getSupportedUnits().contains(props.unit);});

          this.scalarValue = props.scalarValue;
          this.unit = props.unit;
        },

        getExtraSupportedUnits: function() {
          // summary:
          //   The extra units this class adds to the supported units
          //   defined in superclasses.
          //   Concrete subclasses can define the extra supported units
          //   in a Constructor property "extraSupportedUnits".
          // description:
          //   If a class defines no extra supported units,
          //   this returns undefined.

          return this.constructor.extraSupportedUnits;
        },

        getSupportedUnits: function() {
          // summary:
          //   Returns Set of all supported units.
          // description:
          //   Subtypes list the extra units they support, next to the ones their super types support,
          //   in a Set property `extraSupportedUnits` of the constructor. This function returns
          //   the union of those Sets of all super types of the instance.

          return getSupportedUnits(this.constructor);
        },

        getScalarValueAs: function(/*String*/ unit) {
          // summary:
          //   Return the scalarValue in the given unit.
          // description:
          //   Must be overridden if the class supports more then 1 unit.
          this._c_pre(function() {return unit;});
          this._c_pre(function() {return this.getSupportedUnits().contains(unit)});

          return this.scalarValue; // return Number - works only if there is only 1 unit supported
        },

        compare: function(/*ScalarQuantitativeValue*/ other) {
          this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(ScalarQuantitativeValue));});

          if (!other) {
            return +1;
          }
          var otherScalar = other.getScalarValueAs(this.unit);
          return this.scalarValue < otherScalar ? -1 : (this.scalarValue === otherScalar ? 0 : +1);
        },

        equals: function(/*ScalarQuantitativeValue*/ other) {
          return this.inherited(arguments) && (this.scalarValue === other.getScalarValueAs(this.unit));
        },

        add: function(/*ScalarQuantitativeValue*/ other) {
          // summary:
          //   Returns a new object, of the same concrete type as this, with the unit of this,
          //   and a scalarValue that represents the sum of other with this.
          //   Other must be compatible, in that it must be able to return a scalar value
          //   in the unit of this.
          //   If no other is given, we return this.
          // description:
          //   The kwargs argument offered to the constructor of this type is the result of
          //   this.toJSON, with an altered scalarValue. This means all other properties that
          //   the subclass provides are copied to the new object.
          this._c_pre(function() {return !other || other.getSupportedUnits().contains(this.unit);});

          if (!other) {
            return this;
          }
          var sum = this.scalarValue + other.getScalarValueAs(this.unit);
          var kwargs = this.toJSON();
          kwargs.scalarValue = sum;
          return new (this.constructor)(kwargs);
        },

        getValue: function() {
          return this.scalarValue + " " + this.unit;
        },

        _extendJsonObject: function(/*Object*/ json) {
          json.scalarValue = this.scalarValue;
          json.unit = this.unit;
        },

        _stateToString: function(/*Array of String*/ toStrings) {
          toStrings.push("scalarValue: " + this.scalarValue);
          toStrings.push("unit: " + this.unit);
        }
      });


      function getSupportedUnits(ScalarQuantitativeValueConstructor) {
        if (!ScalarQuantitativeValueConstructor.prototype.isInstanceOf(ScalarQuantitativeValue)) {
          throw "ERROR: precondition violation: constructor is not a subtype of ScalarQuantitativeValue";
        }

        if (!ScalarQuantitativeValueConstructor._supportedUnits) {
          var parents = ScalarQuantitativeValueConstructor._meta.parents;
          ScalarQuantitativeValueConstructor._supportedUnits = parents.reduce(
            function(acc, parent) {
              if (parent.prototype.isInstanceOf(ScalarQuantitativeValue)) {
                acc.addAll(getSupportedUnits(parent));
              }
              return acc;
            },
            ScalarQuantitativeValueConstructor.prototype.getExtraSupportedUnits() ||
              new Set({elementType: "string"})
          );
        }
        return ScalarQuantitativeValueConstructor._supportedUnits;
      }

      ScalarQuantitativeValue.format = function(sqv, options) {
        // summary:
        //   options.unit can be filled out; if not, the unit of the value is used.
        //   options.na can be filled out; this string is used to represent null or undefined `fraction`.
        //   If options.mantisseOptional is true, the output does not show a decimal point, or a mantisse,
        //   if the mantisse is 0.
        //   If options.showNoUnit is true, the unit is not shown.

        if (!sqv) {
          return (options && (options.na || options.na === "")) ? options.na : 'N/A';
        }
        else {
          var displayUnit = (options && options.unit) || sqv.unit;
          if (! sqv.getSupportedUnits().contains(displayUnit)) {
            throw "ERROR: cannot format " + sqv.toString() + " as '" + displayUnit + "' (unit not supported)";
          }
          var scalarValue = sqv.getScalarValueAs(displayUnit);
          var opt = options ? lang.clone(options) : {};
          if (opt.mantisseOptional) {
            var mantisse = scalarValue - Math.floor(scalarValue);
            if (mantisse === 0) {
              opt.places = 0;
            }
          }
          return number.format(scalarValue, opt) + (opt.showNoUnit ? "" : " " + displayUnit);
        }
      };

      ScalarQuantitativeValue.parse = function(defaultDefaultUnit, SqvConstructor, str, options) {
        // summary:
        //   Not a parse function. To get a parse function, do
        //  lang.partial(ScalarQuantitativeValue.parse, ConcreteConstructor, "defaultDefaultUnit")

        if (!(lang.isFunction(SqvConstructor) &&
              SqvConstructor.prototype &&
              SqvConstructor.prototype.isInstanceOf &&
              SqvConstructor.prototype.isInstanceOf(ScalarQuantitativeValue))) {
          throw "ERROR: SqvConstructor is not a subtype of ScalarQuantitativeValue";
        }

        if (!str || str === (options && options.na ? options.na : 'N/A')) {
          return null;
        }
        else {
          var unitStartPosition = -1;
          var unit = (options && (options.defaultUnit || options.unit)) || defaultDefaultUnit; // default
          var supportedUnits = SqvConstructor.prototype.getSupportedUnits().toArray().sort(function(one, other) { // sort: longest first
            return (other.length - one.length) || (one < other ? -1 : +1); // == is impossible: it is a set
          });
          for(var i = 0; i < supportedUnits.length; i++) {
            unitStartPosition = str.indexOf(supportedUnits[i]);
            if (unitStartPosition >= 0) {
              unit = supportedUnits[i];
              break; // because sorted from longest to shortest, first hit is it
            }
          }
          // we certainly have a unit; now we also either have a unitStartPosition >= 0, or not
          // the scalar value must come before the unit
          var strScalar = unitStartPosition >= 0 ? str.substring(0, unitStartPosition) : str;
          strScalar = strScalar.trim();
          // if there is nothing left, we take that as a null (empty, or unit without a number)
          if (strScalar === "") {
            return null;
          }
          // now, parse the number part, with i18n
          // with options.places, we need to supply str with this exact decimal places!!! 0's aren't added, number isn't truncated
          // we don't want that, so remove the places
          var opt = options ? lang.clone(options) : {};
          delete opt.places;
          var d = number.parse(strScalar, opt);
          // now d is a number, or NaN if strScalar was not a str that represented a number; that is a problem
          if (!d) {
            throw new ParseException({targetType: SqvConstructor, str: str, options: opt});
          }
          // we now have a number and a unit; we can create the Value
          return new SqvConstructor({scalarValue: d, unit: unit});
        }
      };

      ScalarQuantitativeValue._supportedUnits = new Set({elementType: "string"});
      ScalarQuantitativeValue.extraSupportedUnits = null;

      ScalarQuantitativeValue.mid = module.id;

      return ScalarQuantitativeValue;
    }
);
