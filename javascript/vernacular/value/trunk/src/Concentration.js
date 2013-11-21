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

define(["dojo/_base/declare", "./SiScalarQuantitativeValue", "ppwcode-util-collections/ArraySet",
        "./Volume", "./Mass",
        "dojo/_base/lang", "module"],
  function(declare, SiScalarQuantitativeValue, Set,
           Volume, Mass,
           lang, module) {

    var kgPerL = "kg/l"; // === 10^3 g / 10^-3 m³ === 10^6 g/m³ === Mg/m³
    var gPerL = "g/l"; // === 1 g / 10^-3 m³ === 10^3 g/m³ === kg/m³
    var mgPerL = "mg/l"; // === 10^-3 g / 10^-3 m³ === g/m³

    var Concentration = declare([SiScalarQuantitativeValue], {

      _c_invar: [
        // NOP
      ],

      getScalarValueAs: function(/*String*/ unit) {
        if (unit === this.unit) {
          return this.scalarValue;
        }
        if (unit === kgPerL) {
          return this.getScalarValueAs("Mg/m³");
        }
        if (unit === gPerL) {
          return this.getScalarValueAs("kg/m³");
        }
        if (unit === mgPerL) {
          return this.getScalarValueAs("g/m³");
        }
        // assert: unit is an SI variant of base unit
        if (this.unit === kgPerL || this.unit === gPerL || this.unit === mgPerL) {
          var unitPrefix = this.getUnitPrefix(unit);
          var thisSiFactor;
          switch (this.unit) {
            case kgPerL:
              thisSiFactor = SiScalarQuantitativeValue.siFactor["M"];
              break;
            case gPerL:
              thisSiFactor = SiScalarQuantitativeValue.siFactor["k"];
              break;
            case mgPerL:
              thisSiFactor = SiScalarQuantitativeValue.siFactor[""];
              break;
            default:
              throw "ERROR: " + this.unit + " is not a supported unit";
          }
          var factor = thisSiFactor - SiScalarQuantitativeValue.siFactor[unitPrefix];
          return this.scalarValue * Math.pow(10, factor);
        }
        // all the rest
        return this.inherited(arguments);
      },

      volumeToMass: function(/*Volume*/ vol) {
        this._c_pre(function() {return !vol || (vol.isInstanceOf && vol.isInstanceOf(Volume));});

        if (!vol) {
          return null;
        }
        // IDEA interpret complex unit; for now, we will return kg
        var concentrationAsKgPerL = this.getScalarValueAs(kgPerL);
        var volumeAsL = vol.getScalarValueAs("l");
        var d = concentrationAsKgPerL * volumeAsL;
        return new Mass({scalarValue: d, unit: "kg"}); // return Mass
      },

      massToVolume: function(/*Mass*/ mass) {
        this._c_pre(function() {return !mass || (mass.isInstanceOf && mass.isInstanceOf(Mass));});

        if (!mass) {
          return null;
        }
        // IDEA interpret complex unit; for now, we will return l
        var massAsKg = mass.getScalarValueAs("kg");
        var concentrationAsKgPerL = this.getScalarValueAs(kgPerL);
        var d = massAsKg / concentrationAsKgPerL;
        return new Volume({scalarValue: d, unit: "l"}); // return Volume
      }

    });

    Concentration.format = SiScalarQuantitativeValue.format;
    Concentration.parse = lang.partial(SiScalarQuantitativeValue.parse, kgPerL, Concentration);

    Concentration.baseUnit = "g/m³";
    Concentration.nonSiExtraSupportedUnits = new Set({elementType: "string", data: [kgPerL, gPerL, mgPerL]});
    Concentration.mid = module.id;

    return Concentration;
  }
);
