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

define(["dojo/_base/declare", "./_QualifiedValue", "ppwcode-util-oddsAndEnds/xml",
        "ppwcode-vernacular-semantics/ParseException", "module"],
  function(declare, _QualifiedValue, xml,
           ParseException, module) {

      var LocalizedStringValue = declare([_QualifiedValue], {

        _c_invar: [
          function() {return this._c_prop_mandatory_string("language");},
          // TODO limit to acceptable ISO codes
          function() {return this._c_prop_mandatory_string("localizedText");}
        ],

        // language: String
        language: null,

        // localizedText: String
        localizedText: null,

        constructor: function(/*Object*/ props) {
          this._c_pre(function() {return (props !== null);});
          this._c_pre(function() {return this._c_prop_mandatory(props, "language");});
          this._c_pre(function() {return this._c_prop_string(props, "language");});
          // TODO limit to acceptable ISO codes
          this._c_pre(function() {return this._c_prop_mandatory(props, "localizedText");});
          this._c_pre(function() {return this._c_prop_string(props, "localizedText");});

          this.language = props.language;
          this.localizedText = props.localizedText;
        },

        compare: function(/*LocalizedStringValue*/ other) {
          this._c_pre(function() {return !other || (other.isInstanceOf && other.isInstanceOf(LocalizedStringValue));});

          if (!other) {
            return +1;
          }
          return this.language < other.language ? -1 :
                   (this.language > other.language ? +1 :
                     (this.localizedText < other.localizedText ? -1 :
                       (this.localizedText > other.localizedText ? +1 :
                         0)));
        },

        equals: function(/*be.ecachim.pictoperfect.ui.viewmodel.value.LocalizedStringValue*/ other) {
          return this.inherited(arguments) &&
            (this.language === other.language) && (this.localizedText === other.localizedText);
        },

        getValue: function() {
          return this.localizedText;
        },

        getQualifier: function() {
          return this.language;
        },

        _extendJsonObject: function(/*Object*/ json) {
          json.language = this.language;
          json.localizedText = this.localizedText;
        },

        _stateToString: function(/*Array of String*/ toStrings) {
          toStrings.push("language: " + this.language);
          toStrings.push("localizedText: " + this.localizedText);
        }

      });

      LocalizedStringValue.format = function(lsValue, options) {
        if (!lsValue) {
          return (options && (options.na || options.na === "")) ? options.na : 'N/A';
        }
        // don't escape XML by default; only do if options.escapeXml is explicity true (it isn't used in text boxes)
        if (options && options.escapeXml) {
          return xml.escape(lsValue.localizedText);
        }
        else {
          return lsValue.localizedText;
        }
      };

      LocalizedStringValue.parse = function(str, options) {
        if (!str || str === (options && options.na ? options.na : 'N/A')) {
          return null;
        }
        if (!(options && options.locale)) {
          throw new ParseException({targetType: LocalizedStringValue, str: str, options: options, key: "NO_LOCALE"});
        }
        return new LocalizedStringValue({language: options.locale, localizedText: str});
      };

      LocalizedStringValue.mid = module.id;

      return LocalizedStringValue;
    }
);
