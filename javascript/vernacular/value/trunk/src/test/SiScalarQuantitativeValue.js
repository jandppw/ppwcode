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

define(["contracts/doh",
        "../SiScalarQuantitativeValue", "./ScalarQuantitativeValue",
        "dojo/_base/declare", "dojo/_base/lang"],
  function(doh,
           SiScalarQuantitativeValue, testGeneratorScalarQuantitativeValue,
           declare, lang) {

    var SiSqvMock = declare([SiScalarQuantitativeValue], {});
    SiSqvMock.mid = "pictoperfect-viewmodel/value/SiScalarQuantitativeValue";
    SiSqvMock.baseUnit = "-BASE UNIT";

    function testGeneratorSiScalarQuantitativeValue(Constructor, kwargs1, kwargs2, renameds) {

      testGeneratorScalarQuantitativeValue(Constructor, kwargs1, kwargs2, renameds);

      doh.register(Constructor.mid, [

        function testGetBaseUnit() {
          var result = Constructor.prototype.getBaseUnit();

          // post
          doh.t(result && true); // make it a boolean; doh evals strings
        }

      ]);

      var units = Constructor.prototype.getSupportedUnits();
      units.forEach(function(unit) {
        doh.register(Constructor.mid, [

          {
            name: "test getUnitPrefix of this (" + unit + ")",
            setUp: function() {
              this.instrumented =  lang.clone(kwargs1);
              this.instrumented.unit = unit;
              this.subject = new Constructor(this.instrumented);
            },
            runTest: function() {
              var result = this.subject.getUnitPrefix();

              doh.invars(this.subject);
              if (unit.indexOf(this.subject.getBaseUnit()) >= 0) {
                doh.t(SiScalarQuantitativeValue.siPrefixes.contains(result));
                doh.is(unit, result + this.subject.getBaseUnit());
              }
              else {
                doh.is(null, result);
              }
            },
            tearDown: function() {
              delete this.instrumented;
              delete this.subject;
            }
          },

          {
            name: "test getUnitPrefix of " + unit,
            setUp: function() {
              this.subject = new Constructor(kwargs1);
            },
            runTest: function() {
              var result = this.subject.getUnitPrefix(unit);

              doh.invars(this.subject);
              // post
              if (unit.indexOf(this.subject.getBaseUnit()) >= 0) {
                doh.t(SiScalarQuantitativeValue.siPrefixes.contains(result));
                doh.is(unit, result + this.subject.getBaseUnit());
              }
              else {
                doh.is(null, result);
              }
            },
            tearDown: function() {
              delete this.subject;
            }
          }

        ]);
      });

    }

    testGeneratorSiScalarQuantitativeValue(
      SiSqvMock,
      {
        scalarValue: 123,
        unit: "µ-BASE UNIT"
      },
      {
        scalarValue: 456.789,
        unit: "-BASE UNIT"
      }
    );

    return testGeneratorSiScalarQuantitativeValue;
  }
);
