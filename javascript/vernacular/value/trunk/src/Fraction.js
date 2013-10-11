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

define(["dojo/_base/declare", "./ScalarQuantitativeValue",
        "ppwcode-util-collections/ArraySet", "module"],
  function(declare, ScalarQuantitativeValue,
           Set, module) {

    // The value of the property is the power of 10
    var unitFactor = {
       "ppm":  -6, // parts per million
         "‰":  -3, // promille
         "%":  -2, // percent
      "0..1":   0  // fraction without a unit
    };

    var Fraction = declare([ScalarQuantitativeValue], {

      _c_invar: [
        // NOP
      ],

      getScalarValueAs: function(/*String*/ unit) {
        // summary:
        //   Return the scalarValue in the given unit.
        this._c_pre(function() {return unit;});
        this._c_pre(function() {return this.getSupportedUnits().contains(unit)});

        // factor is power-of-10 of (unitFactor[this.unit] - unitFactor[unit])
        var factor = unitFactor[this.unit] - unitFactor[unit];
        return this.scalarValue * Math.pow(10, factor);
      }

    });

    Fraction.format = ScalarQuantitativeValue.format;
    Fraction.parse = function(FractionConstructor, str, options) {
      // we also support "pm" for promille, instead of "‰" for convenience
      var rStr = str.replace(/[^p]pm/, "‰");
      return ScalarQuantitativeValue.parse("0..1", FractionConstructor, rStr, options);
    };

    Fraction.unitFactor = unitFactor;
    Fraction.mid = module.id;
    Fraction.extraSupportedUnits = new Set({elementType: "string", data: Object.keys(unitFactor)});

    return Fraction;
  }

);
