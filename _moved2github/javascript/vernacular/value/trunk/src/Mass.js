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

define(["dojo/_base/declare", "./SiScalarQuantitativeValue", "dojo/_base/lang", "module"],
    function(declare, SiScalarQuantitativeValue, lang, module) {

      var Mass = declare([SiScalarQuantitativeValue], {

        _c_invar: [
          // NOP
        ],

        times: function(/*Fraction*/ fraction) {
          var d = this.scalarValue * fraction.getScalarValueAs("0..1");
          return new Mass({scalarValue: d, unit: this.unit})
        }

      });

      Mass.format = SiScalarQuantitativeValue.format;
      Mass.parse = lang.partial(SiScalarQuantitativeValue.parse, "g", Mass);

      Mass.baseUnit = "g";
      Mass.mid = module.id;

      return Mass;
    }
);
