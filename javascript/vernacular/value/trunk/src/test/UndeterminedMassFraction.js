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

define(["../UndeterminedMassFraction", "./MassFraction"],
  function(UndeterminedMassFraction, testGeneratorMassFraction) {

    function testUndeterminedMassFraction(Constructor, kwargs1, kwargs2) {
      testGeneratorMassFraction(Constructor, kwargs1, kwargs2);
    }

    testGeneratorMassFraction(
      UndeterminedMassFraction,
      {
        scalarValue: 0.00123,
        unit: "0..1"
      },
      {
        scalarValue: 456.789,
        unit: "‰"
      }
    );

    return testUndeterminedMassFraction;
  }
);
