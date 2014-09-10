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

define(["dojo/_base/declare", "./ScalarQuantitativeValue", "ppwcode-util-collections/ArraySet", "module"],
  function(declare, ScalarQuantitativeValue, Set, module) {

    var degreesCelsius = "°C";

    var ThermodynamicTemperature = declare([ScalarQuantitativeValue], {

      _c_invar: [
        // NOP
      ]

    });

    ThermodynamicTemperature.format = ScalarQuantitativeValue.format;
    ThermodynamicTemperature.parse = function(str, options) {
      // we also support "C" without "°" for convenience
      var rStr = str.replace("C", degreesCelsius);
      return ScalarQuantitativeValue.parse(degreesCelsius, ThermodynamicTemperature, rStr, options);
    };

    ThermodynamicTemperature._supportedUnits = new Set({elementType: "string", data: [degreesCelsius]});
    ThermodynamicTemperature.mid = module.id;

    return ThermodynamicTemperature;
  }
);
