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

define(["dojo/_base/declare", "./Fraction", "ppwcode-util-collections/ArraySet", "module"],
  function(declare, Fraction, Set, module) {

    var mgperkg = "mg/kg";

    var MassFraction = declare([Fraction], {

      _c_invar: [
        // NOP
      ],

      getScalarValueAs: function(/*String*/ unit) {
        if (unit === mgperkg) {
          if (this.unit === mgperkg) {
            return this.scalarValue;
          }
          else {
            return this.getScalarValueAs("ppm");
          }
        }
        else if (this.unit === mgperkg) { // and unit is not
          var factor = -6 - Fraction.unitFactor[unit];
          return this.scalarValue * Math.pow(10, factor);
        }
        else {
          return this.inherited(arguments);
        }
      }

    });

    MassFraction.format = Fraction.format;
    MassFraction.parse = Fraction.parse;

    MassFraction.mid = module.id;
    MassFraction.extraSupportedUnits = new Set({elementType: "string", data: [mgperkg]});

    return MassFraction;
  }
);
