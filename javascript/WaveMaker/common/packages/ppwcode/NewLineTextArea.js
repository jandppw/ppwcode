/*
Copyright 2009 - $Date: $ by PeopleWare n.v..

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

dojo.provide("common.packages.ppwcode.NewLineTextArea");

dojo.require("wm.base.widget.Editor");
dojo.require("wm.base.widget.Editors.Text");

wm.editors.push("NewLineTextArea");

dojo.declare("wm._NewLineTextAreaEditor", wm._TextAreaEditor, {
	autoScroll: true,
	_getReadonlyValue: function() {
		var t = this.inherited(arguments);
		var rex = new RegExp("[\\n\\r]", "gi");
		return t.replace(rex, "<br>");
	},
	setReadonly: function(readonly) {
		this.autoScroll = readonly;
		this.inherited(arguments);
	}
});

dojo.declare("common.packages.ppwcode.NewLineTextAreaEditor", wm.Editor, {
	display: "NewLineTextArea"
});
