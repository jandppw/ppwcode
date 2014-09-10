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

define(["dojo/_base/declare", "./SiScalarQuantitativeValue", "ppwcode-util-collections/ArraySet", "dojo/_base/lang", "module"],
  function(declare, SiScalarQuantitativeValue, Set, lang, module) {

    var TPM = "TPM";

    var Frequency = declare([SiScalarQuantitativeValue], {

      _c_invar: [
        // NOP
      ],

      getScalarValueAs: function(/*String*/ unit) {
        if (unit === TPM) {
          if (this.unit === TPM) {
            return this.scalarValue;
          }
          else {
            return this.getScalarValueAs("Hz") * 60;
          }
        }
        else if (this.unit === TPM) { // and unit is not
          var unitPrefix = this.getUnitPrefix(unit);
          var thisAsHz = this.scalarValue / 60;
          var factor = -SiScalarQuantitativeValue.siFactor[unitPrefix];
          return thisAsHz * Math.pow(10, factor);
        }
        else {
          return this.inherited(arguments);
        }
      }

    });

    Frequency.format = SiScalarQuantitativeValue.format;
    Frequency.parse = lang.partial(SiScalarQuantitativeValue.parse, TPM, Frequency);

    Frequency.baseUnit = "Hz";
    Frequency.nonSiExtraSupportedUnits = new Set({elementType: "string", data: [TPM]});
    Frequency.mid = module.id;

    return Frequency;
  }
);
