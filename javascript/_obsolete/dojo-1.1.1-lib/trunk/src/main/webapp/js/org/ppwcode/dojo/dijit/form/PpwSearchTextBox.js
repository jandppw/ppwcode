dojo.provide("org.ppwcode.dojo.dijit.form.PpwSearchTextBox");

dojo.require("dijit.form.TextBox");

dojo.declare(
	"org.ppwcode.dojo.dijit.form.PpwSearchTextBox",
	dijit.form.TextBox,
	{
		//summary:
		//    PpwSearchTextBox is a text box that holds off on sending keyup
		//    events until the text box contains a number of characters and
		//    until a defined amount of milliseconds have passed.  The main
		//    use case for using this text box is when an application wants
		//    to contact the server (e.g. XHR communication) when a user
		//    changes the contents of the text box.  An application typically
		//    does not want a request reply cycle on each keystroke.
		//description:
		//    The PpwSearchTextBox is a text box that functions like a normal
		//    text box, except that it delays firing the DOM keyup event, or
		//    does not send the keyup event at all.  Behavior is as follows:
		//    DOM events are not sent if the amount of characters in the text
		//    box is smaller than a defined amount (minChars).  If the amount
		//    of characters in the text box is larger than that amount, the 
		//    widget holds off on sending the event for an amount of time
		//    (defined in triggerTimeout).  If a new keystroke event occurs
		//    within that time, the keyup event for the previous keystroke is
		//    thrown away.  The delay for sending the event is reset.
		//    The widget also filters certain keys.  The goal is to only send
		//    events on keystrokes that alter the text box; this excludes
		//    navigation keys (up, down, home, end, ...), Function keys,
		//    modifier keys, etc.
		//  
		
		templateString:"<input class=\"dijit dijitReset dijitLeft\" dojoAttachPoint='textbox,focusNode' name=\"${name}\"\n\tdojoAttachEvent='onmouseenter:_onMouse,onmouseleave:_onMouse,onfocus:_onMouse,onblur:_onMouse,onkeypress:_onKeyPress,onkeyup:_onkeyup'\n\tautocomplete=\"off\" type=\"${type}\"\n\t/>\n",
		
		minChars: 3,
		triggerTimeout: 300,
		
		_nonEventKeyCodes: [
		            		dojo.keys.SHIFT,
		            		dojo.keys.CTRL,
		            		dojo.keys.ALT,
		            		dojo.keys.PAUSE,
		            		dojo.keys.CAPS_LOCK,
		            		dojo.keys.ESCAPE,
		            		dojo.keys.SPACE,
		            		dojo.keys.PAGE_UP,
		            		dojo.keys.PAGE_DOWN,
		            		dojo.keys.END,
		            		dojo.keys.HOME,
		            		dojo.keys.LEFT_ARROW,
		            		dojo.keys.UP_ARROW,
		            		dojo.keys.RIGHT_ARROW,
		            		dojo.keys.DOWN_ARROW,
		            		dojo.keys.INSERT,
		            		dojo.keys.DELETE,
		            		dojo.keys.HELP,
		            		dojo.keys.LEFT_WINDOW,
		            		dojo.keys.RIGHT_WINDOW,
		            		dojo.keys.SELECT,
		            		dojo.keys.F1,
		            		dojo.keys.F2,
		            		dojo.keys.F3,
		            		dojo.keys.F4,
		            		dojo.keys.F5,
		            		dojo.keys.F6,
		            		dojo.keys.F7,
		            		dojo.keys.F8,
		            		dojo.keys.F9,
		            		dojo.keys.F10,
		            		dojo.keys.F11,
		            		dojo.keys.F12,
		            		dojo.keys.F13,
		            		dojo.keys.F14,
		            		dojo.keys.F15,
		            		dojo.keys.NUM_LOCK,
		            		dojo.keys.SCROLL_LOCK
		            		],
		            		
		_nonEventKeyMap: {instantiated: false},
		
		setMinChars: function(/*int*/amount) {
			//summary:
			//    Set the amount of characters that must by minimally typed
			//    the widget starts firing events.
			//description:
			//    Set the amount of characters that must by minimally typed
			//    the widget starts firing events.  This property can also
			//    be set with the "minChars" attribute in the tag that
			//    defines this widget.
			//amount:
			//    the amount of characters that must be typed
			this.minChars = amount;
		},
		
		setTriggerTimeout: function(/*int*/amount) {
			//summary:
			//    Set the amount of milliseconds that must pass before
			//    an event is triggered after a valid key has been pressed.
			//description:
			//    Set the amount of milliseconds that must pass before
			//    an event is triggered after a valid key has been pressed.
			//    This property can also be set with the triggerTimeout
			//    attribute in the tag that defines this widget.
			//amount:
			//    The timeout in milliseconds.
			this.triggerTimeout = amount;
		},
		
		constructor: function() {
			if (!this._nonEventKeyMap.instantiated) {
				//create the object based on the array in the prototype
				for (var i = 0; i < this._nonEventKeyCodes.length; i++) {
					this._nonEventKeyMap[this._nonEventKeyCodes[i]] = true;
				}
				this._nonEventKeyMap.instantiated = true;
			}
		},
		
		_timeoutId: null,
		
		_onkeyup: function(event) {
			//console.log(event.keyCode);
			//console.log("PpwSearchTextBox: value.length is " + this.getValue().length);
			if (this._nonEventKeyMap[event.keyCode] !== true) { 
				//console.log("PpwSearchTextBox: valid event key pressed");
				//if a 'valid' key is pressed, clear a running timer if there is one
				//also do this when there are less than this.minChars in the textbox
				if (this._timeoutId != null) {
					//console.log("PpwSearchTextBox: timeout " + this._timeoutId + " canceled");
					clearTimeout(this._timeoutId);
					this._timeoutId = null;
				}
				if (event.keyCode == dojo.keys.ENTER) {
					//if the key is ENTER, proceed always and immediately
					this._onkeyupProceed(event);
				} else if (this.getValue().length >= this.minChars) {
					//if there are enough characters in the box, schedule a timer that will
					//forward this event
					//console.log("PpwSearchTextBox: length constraint satisfied");
					this._timeoutId = setTimeout(dojo.hitch(this, this._onkeyupProceed, event), this.triggerTimeout);
					//console.log("PpwSearchTextBox: new timeout (" + this._timeoutId + ") set: " + this.triggerTimeout);
				} else {
					dojo.stopEvent(event);
				}
			} else {
				dojo.stopEvent(event);
			}
		},
		
		_onkeyupProceed: function(event) {
			//console.log("PpwSearchTextBox: event call proceeding " + this._timeoutId);
			this._timeoutId = null;
			this.onkeyup(event);
		}
	}
);
