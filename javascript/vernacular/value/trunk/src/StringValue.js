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

define(["dojo/_base/declare", "ppwcode-vernacular-semantics/Value", "ppwcode-util-oddsAndEnds/xml", "module"],
  function(declare, Value, xml, module) {

    var StringValue = declare([Value], {

      _c_invar: [
        function() {return this._c_prop_mandatory("text");},
        function() {return this._c_prop_string("text");}
      ],

      // text: String
      //   The text of the value.
      text: null,

      constructor: function(/*Object*/ props) {
        this._c_pre(function() {return props;});
        this._c_pre(function() {return this._c_prop_mandatory(props, "text");});
        this._c_pre(function() {return this._c_prop_string(props, "text");});

        this.text = props.text;
      },

      compare: function(/*StringValue*/ other) {
        this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(StringValue));});

        if (!other) {
          return +1;
        }
        return this.text < other.text ? -1 : (this.text === other.text ? 0 : +1);
      },

      equals: function(/*StringValue*/ other) {
        return this.inherited(arguments) && (this.text === other.text);
      },

      getValue: function() {
        return this.text;
      },

      _extendJsonObject: function(/*Object*/ json) {
        json.text = this.text;
      },

      _stateToString: function(/*Array of String*/ toStrings) {
        toStrings.push("text: " + this.text);
      }

    });

    StringValue.format = function(sValue, options) {
      if (!sValue) {
        return (options && (options.na || options.na === "")) ? options.na : 'N/A';
      }
      // don't escape XML by default; only do if options.escapeXml is explicity true (it isn't used in text boxes)
      if (options && options.escapeXml) {
        return xml.escape(sValue.text);
      }
      else {
        return sValue.text;
      }
    };

    StringValue.parse = function(str, options) {
      if (!str || str === (options && options.na ? options.na : 'N/A')) {
        return null;
      }
      return new StringValue({text: str});
    };

    StringValue.mid = module.id;

    return StringValue;
  }
);
