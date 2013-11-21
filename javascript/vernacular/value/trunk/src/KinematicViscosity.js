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

define(["dojo/_base/declare", "./ScalarQuantitativeValue", "ppwcode-util-collections/ArraySet", "dojo/_base/lang", "module"],
  function(declare, ScalarQuantitativeValue, Set, lang, module) {

    var m2pers = "m²/s";
    var mm2pers = "mm²/s";
    var factor = 10e6;

    var KinematicViscosity = declare([ScalarQuantitativeValue], {

      _c_invar: [
        // NOP
      ],

      getScalarValueAs: function(/*String*/ unit) {
        this._c_pre(function() {return unit;});
        this._c_pre(function() {return this.getSupportedUnits().contains(unit)});

        if (unit === this.unit) {
          return this.scalarValue;
        }
        if (this.unit === m2pers) {
          // unit === mm2pers
          return this.scalarValue * factor;
        }
        // this.unit === mm2pers and unit === m2pers
        return this.scalarValue / factor;
      }

    });

    KinematicViscosity.format = ScalarQuantitativeValue.format;
    KinematicViscosity.parse = lang.partial(ScalarQuantitativeValue.parse, m2pers, KinematicViscosity);

    KinematicViscosity._supportedUnits = new Set({elementType: "string", data: [m2pers, mm2pers]});
    KinematicViscosity.mid = module.id;

    return KinematicViscosity;
  }
);
