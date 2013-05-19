/*
Copyright 2012 - $Date $ by PeopleWare n.v.

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

define(["dojo/_base/declare", "ppwcode/contracts/_Mixin"],
    function(declare, _ContractMixin) {

      var SemanticException = declare([_ContractMixin], {
        // summary:
        //   Supertype for exceptions related to semantics: the nominal effect of a method could
        //   not be reached, because doing so under the given circumstances would violate semantics
        //   (often type invariants).
        // description:
        //   `key` is a string, that is used to lookup i18n messages to show to the user when
        //    this exception occurs. Most often, it will be all caps.
        //    i18n messages can use all information found in the particular `SemanticException`

        invars: [
          function() {return this._c_prop_string("key");},
          function() {return this.cause || this.cause == null;} // TODO not undefined
        ],

        // key: String?
        //    `key` is a string, that is used to lookup i18n messages to show to the user when
        //    this exception occurs.
        // description:
        //    Most often, it will be all caps.
        //    i18n messages can use all information found in the particular `SemanticException`
        key: null,

        // cause: Object?
        //    The underlying cause of this exception, if any.
        cause: null,

        constructor: function(kwargs) {
          if (kwargs) {
            this.key = kwargs.key;
            this.cause = kwargs.cause;
          }
        },

        postCreate: function() {
          Object.freeze(this);
        },

        like: function(/*SemanticException*/ other) {
          if (!other || ! other.key || ! other.cause) {
            return false;
          }
          else if (other === this) {
            return true
          }
          else {
            return other.key === this.key && other.cause === this.cause;
          }
        }

        // TODO need JSON and toString

      });

      return SemanticException;
    }
);
