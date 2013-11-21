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
        "../ScalarQuantitativeValue", "ppwcode-vernacular-semantics/test/Value",
        "dojo/_base/declare", "collections/ArraySet", "oddsAndEnds/js", "dojo/_base/lang"],
  function(doh,
           ScalarQuantitativeValue, testGeneratorValue,
           declare, Set, js, lang) {

    var mockUnit = "mockUnit";

    var SqvMock = declare([ScalarQuantitativeValue], {});
    SqvMock.extraSupportedUnits = new Set({elementType: "string", data: [mockUnit]});
    SqvMock.mid = ScalarQuantitativeValue.mid;

    function testGeneratorScalarQuantitativeValue(Constructor, kwargs1, kwargs2, renameds) {
      if (!Constructor) {
        throw "CANNOT CREATE TESTS: no value type constructor.";
      }
      if (!Constructor.mid) {
        throw "CANNOT CREATE TESTS: value type constructor has no mid"
      }

      testGeneratorValue(Constructor, kwargs1, kwargs2, renameds);

      doh.register(Constructor.mid, [
        function testGetExtraSupportedUnits() {
          var result = Constructor.prototype.getExtraSupportedUnits();

          // post
          doh.t(!result || result.isInstanceOf && result.isInstanceOf(Set));
          doh.t(!Constructor.extraSupportedUnits || result.containsAll(Constructor.extraSupportedUnits));
        },

        function testGetSupportedUnits() {
          var result = Constructor.prototype.getSupportedUnits();

          // post
          doh.t(result);
          doh.t(result.isInstanceOf && result.isInstanceOf(Set));
          doh.t(result.getSize() > 0);
          Constructor._meta.parents.forEach(function(parent) {
            if (parent.prototype.isInstanceOf(ScalarQuantitativeValue)) {
              doh.t(result.containsAll(parent.prototype.getSupportedUnits()));
            }
          });
          if (Constructor.extraSupportedUnits) {
            doh.t(result.containsAll(Constructor.extraSupportedUnits));
          }
        }
      ]);

      var units = Constructor.prototype.getSupportedUnits();
      units.forEach(function(unit1) {
        units.forEach(function(unit2) {
          doh.register(Constructor.mid, [
            {
              name: "test getScalarValueAs " + unit1 + " --> " + unit2,
              setUp: function() {
                this.instrumented =  lang.clone(kwargs1);
                this.instrumented.unit = unit1;
                this.subject = new Constructor(this.instrumented);
              },
              runTest: function() {
                var result = this.subject.getScalarValueAs(unit2);

                doh.invars(this.subject);
                // post
                doh.is("number", js.typeOf(result));
                if (unit1 === unit2) {
                  doh.is(this.subject.scalarValue, result);
                }
                console.log(result);
              },
              tearDown: function() {
                delete this.instrumented;
                delete this.subject;
              }
            }
          ]);
        });
      });

      doh.register(Constructor.mid, [

        {
          name: "test add 1",
          setUp: function() {
            this.subject = new Constructor(kwargs1);
          },
          runTest: function() {
            var result = this.subject.add(this.subject);

            doh.invars(this.subject);
            // post
            doh.is(kwargs1.unit, result.unit);
            doh.is(2*kwargs1.scalarValue, result.scalarValue);
          },
          tearDown: function() {
            delete this.subject;
          }
        },

        {
          name: "test add 2",
          setUp: function() {
            this.subject1 = new Constructor(kwargs1);
            this.subject2 = new Constructor(kwargs2);
          },
          runTest: function() {
            var result = this.subject1.add(this.subject2);

            doh.invars(this.subject1);
            doh.invars(this.subject2);
            // post
            doh.is(kwargs1.unit, result.unit);
            doh.is(kwargs1.scalarValue + this.subject2.getScalarValueAs(kwargs1.unit), result.scalarValue);
          },
          tearDown: function() {
            delete this.subject1;
            delete this.subject2;
          }
        }

      ]);

    }

    testGeneratorScalarQuantitativeValue(
      SqvMock,
      {
        scalarValue: 123,
        unit: "mockUnit"
      },
      {
        scalarValue: 456.789,
        unit: "mockUnit"
      }
    );

    return testGeneratorScalarQuantitativeValue;
  }
);
