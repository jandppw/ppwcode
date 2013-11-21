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

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value"],
  function(declare, Value) {

      return declare([Value], {
        // summary:
        //   Mixin that adds the possibility for a value to return a qualifier.
        //   This is used to identify properties in a multiple property store.

        _c_invar: [
        ],

        getQualifier: function() {
          this._c_ABSTRACT();
          return "dummy"; // return String
        },

        equals: function(/*_QualifiedValue*/ other) {
          return this.inherited(arguments) && (this.getQualifier() === other.getQualifier());
        }

      });
    }
);
