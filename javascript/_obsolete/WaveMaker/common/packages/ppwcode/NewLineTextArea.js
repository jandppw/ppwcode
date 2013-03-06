dojo.provide("common.packages.ppwcode.NewLineTextArea");

dojo.require("wm.base.widget.Editor");
dojo.require("wm.base.widget.Editors.Text");

wm.editors.push("NewLineTextArea");

dojo.declare("wm._NewLineTextAreaEditor", wm._TextAreaEditor, {
	autoScroll: false,
	_getReadonlyValue: function() {
		console.log("_getReadonlyValue");
		var t = this.inherited(arguments);
		console.log("\t" + t);
		var rex = new RegExp("[\\n\\r]", "gi");
		return t.replace(rex, "<br>");
	},
	setReadonly: function(readonly) {
		console.log("setReadonly");
		this.autoScroll = readonly;
		this.inherited(arguments);
	},
        sizeEditor: function() {
	// sizeEditor is an exact copy of wm._TextAreaEditor with one modification:
	// the width is reset to zero if it appears no be negative.  A negative width
	// is returned from getContentBounds in IE8 when autoScroll is turned on. The
	// DOM model seems to have a problem with a negative width.
                if (this._cupdating)
                        return;
                var e = this.editor;
                if (e) {
                        var
                                bounds = this.getContentBounds(),
                                // note, subtract 2 from bounds for dijit editor border/margin
                                height = bounds.h ? bounds.h - 2 + "px" : "",
                                width = bounds.w  ? bounds.w - 4 + "px" : "",
                                d = e && e.domNode,
                                s = d.style,
                                fc = d && d.firstChild;

			// Keep IE(8) happy
                        if (parseInt(width) < 0) width = "";

                        if (!this.editorBorder) s.border = 0;
                        s.backgroundColor = this.editorBorder ? "" : "transparent";
                        s.backgroundImage = this.editorBorder ? "" : "none";
                        s.width = width;
                        if (height) {
                                if (fc) {
                                        dojo.forEach(fc.childNodes, function(c) {
                                                if (c.style)
                                                {
                                                        c.style.height = height;
                                                }
                                        });
                                }
                                if (e.focusNode && e.focusNode.style)
                                        e.focusNode.style.height = height;
                        }
                }
        }
});

dojo.declare("common.packages.ppwcode.NewLineTextAreaEditor", wm.Editor, {
	display: "NewLineTextArea"
});
