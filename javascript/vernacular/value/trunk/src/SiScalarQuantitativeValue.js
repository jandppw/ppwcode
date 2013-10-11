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
        "ppwcode-util-collections/ArraySet", "ppwcode-util-oddsAndEnds/js"],
    function(declare, ScalarQuantitativeValue,
             Set, js) {
      // Adds support for the SI-system prefixes
      // http://en.wikipedia.org/wiki/International_System_of_Units#Prefixes

      var SiScalarQuantitativeValue = declare([ScalarQuantitativeValue], {

        _c_invar: [
          function() {return js.typeOf(this.getBaseUnit()) === "string";},
          function() {return this.getUnitPrefix() + this.getBaseUnit() === this.unit;}
        ],

        getBaseUnit: function() {
          // summary:
          //   The base SI unit for this SI value type.
          //   Concrete subclasses may define the base unit in a Constructor property "baseUnit".
          //   Otherwise, the base unit of the super class is used.

          if (this.constructor.baseUnit) {
            return this.constructor.baseUnit;
          }
          var parents = this.constructor._meta.parents;
          var baseUnit = parents.reduce(
            function(acc, p) {
              if (!p.prototype.isInstanceOf(SiScalarQuantitativeValue)) {
                return acc;
              }
              var pBaseUnit = p.prototype.getBaseUnit();
              if (acc && pBaseUnit && acc !== pBaseUnit) {
                throw "ERROR: conflicting baseUnits (" + acc + " and " + pBaseUnit + ")";
              }
              // at least one is null
              return acc || pBaseUnit;
            },
            null
          );
          return baseUnit;
        },

        getUnitPrefix: function(unit) {
          // summary:
          //   Returns the SI prefix of unit, or the SI prefix of this.unit
          //   if no unit is given.
          //   If the unit is not a variant of the base unit, returns null.
          this._c_pre(function() {return !unit || this.getSupportedUnits().contains(unit);});

          if (! this.constructor.prefixPattern) {
            this.constructor.prefixPattern = new RegExp("(.*)" + this.getBaseUnit());
          }
          var theUnit = unit || this.unit;
          var match = theUnit.match(this.constructor.prefixPattern);
          return match === null ? null : match[1];
        },

        getExtraSupportedUnits: function() {
          // summary:
          //   The extra units this class adds to the supported units
          //   defined in superclasses.
          // description:
          //   Returns the base unit with all defined SI prefixes.
          //   this.constructor.extraSupportedUnits is created lazily.
          //   Also, there might be non-SI-supported units. These are mentioned in a
          //   constructor property `nonSiExtraSupportedUnits` optionally.

          if ((!this.constructor.extraSupportedUnits) && this.getBaseUnit()) {
            this.constructor.extraSupportedUnits = SiScalarQuantitativeValue.siPrefixes.map(
              function(prefix) {
                return prefix + this.getBaseUnit();
              },
              this
            );
            if (this.constructor.nonSiExtraSupportedUnits) {
              this.constructor.extraSupportedUnits.addAll(this.constructor.nonSiExtraSupportedUnits);
            }
          }
          return this.inherited(arguments);
        },

        getScalarValueAs: function(/*String*/ unit) {
          // summary:
          //   Return the scalarValue in the given unit.
          // description:
          //   The prefix is the first letter of the unit.
          this._c_pre(function() {return unit;});
          this._c_pre(function() {return this.getSupportedUnits().contains(unit)});

          //            unit
          // this.unit    µg   mg    g   kg   Mg
          //        µg     0   -3   -6   -9  -12
          //        mg     3    0   -3   -6   -9
          //         g     6    3    0   -3   -6
          //        kg     9    6    3    0   -6
          //        Mg    12    9    6    3    0
          // factor is power-of-10 of (siFactor[this.unit] - siFactor[unit])

          var thisPrefix = this.getUnitPrefix();
          var unitPrefix = this.getUnitPrefix(unit);
          var factor = SiScalarQuantitativeValue.siFactor[thisPrefix] - SiScalarQuantitativeValue.siFactor[unitPrefix];
          return this.scalarValue * Math.pow(10, factor);
        }

      });

      // The value of the property is the power of 10
      SiScalarQuantitativeValue.siFactor = {
        // factors commented out because they will not be used
//        "y": -24, // yocto-
//        "z": -21, // zepto-
//        "a": -18, // atto-
//        "f": -15, // femto-
        "p": -12, // pico-
        "n":  -9, // nano-
        "µ":  -6, // micro-
        "m":  -3, // milli-
        "":   0, // BASE -- really ok: the empty string as property name
        "k":   3, // kilo
        "M":   6, // Mega-
        "G":   9, // Giga-
        "T":  12 // Terra-
//        "P":  15, // Peta-
//        "E":  18, // Exa-
//        "Z":  21, // Zetta-
//        "Y":  24  // Yotta-
      };
      // SiScalarQuantitativeValue.baseUnit is explicitly not set; subclasses should set it, however
      // Constructor.prefixPattern is set lazily on subclass constructors
      SiScalarQuantitativeValue.siPrefixes = new Set({
        elementType: "string",
        data: Object.keys(SiScalarQuantitativeValue.siFactor)
      });

      SiScalarQuantitativeValue.format = ScalarQuantitativeValue.format;
      SiScalarQuantitativeValue.parse = ScalarQuantitativeValue.parse;

      // no peristenceType: does not exist on the server

      return SiScalarQuantitativeValue;
    }
);
