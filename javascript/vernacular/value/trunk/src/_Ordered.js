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

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value", "module"],
  function(declare, Value, module) {

      var _Ordered = declare([Value], {
        // summary:
        //   Mixin for values that have an order-property. This is used in collections.
        //   The order is a positive number or 0. In a collection, the order's of the elements
        //   should be unique. They do not have to be in sequence at all times, but can be normalized
        //   to be in sequence.

        _c_invar: [
          function() {return this._c_prop_mandatory("order");},
          function() {return this._c_prop_int("order");}
        ],

        // order: Number
        order: null,

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return this._c_prop_mandatory(props, "order")});
          this._c_pre(function() {return this._c_prop_int(props, "order")});

          this.order = props.order;
        },

        compare: function(/*ScalarQuantitativeValue*/ other) {
          // summary:
          //   Compare uses the order. If the order is the same, we call the super method to decide order.
          //   Normally this cannot happen in a meaningful collection, since a meaningful collection should
          //   not contain duplicate orders.
          //   Note that this makes it necessary to have _Ordered always _first_ in multiple inheritance cases,
          //   because this implementation needs to be called before any other implementations.

          this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(_Ordered));});

          if (!other || this.order > other.order) {
            return +1;
          }
          if (this.order < other.order) {
            return -1;
          }
          return this.inherited(arguments); // to make the order complete
        },

        _extendJsonObject: function(/*Object*/ json) {
          json.order = this.order;
        },

        _stateToString: function(/*Array of String*/ toStrings) {
          toStrings.push("order: " + this.order);
        }

      });

      _Ordered.mid = module.id;
      return _Ordered;
    }
);
