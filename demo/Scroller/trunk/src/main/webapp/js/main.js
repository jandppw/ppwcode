(function() {

function scroll_initialize() {
	var nextnode = dojo.byId("next");
	var previousnode = dojo.byId("prev");
	var dropdownbox = dijit.byId("easingSelector");
	var easing = dojo.fx.easing.linear;
	var duration = 350;
	var position = 0;

	dojo.connect(dropdownbox, "onChange", function() {
		switch (dropdownbox.attr('value')) {
		case "linear":
				easing = dojo.fx.easing.linear;
				duration = 350;
				break;
		case "slowing":
				easing = dojo.fx.easing.quintOut;
				duration = 1000;
				break;
		case "overshoot":
				easing = dojo.fx.easing.backOut;
				duration = 900;
				break;
		case "bouncing":
				easing = dojo.fx.easing.bounceOut;
				duration = 1300;
				break;
		}
	});
	
	dojo.connect(nextnode, "onclick", function() {
		if (position != -1260) {
			position -= 630;
			dojo.animateProperty(
					{node: "slides", 
					 properties: {left: position},
					 easing: easing,
					 duration: duration}
			).play();
		} 
	});
	
	dojo.connect(previousnode, "onclick", function() {
		if (position != 0) {
			position += 630;
			dojo.animateProperty(
					{node: "slides",
					 properties: {left: position},
					 easing: easing,
					 duration: duration}
			).play();
		} 
	});
}

window['djConfig'] = {
		afterOnLoad: true,
		require: ['dojo.fx.easing', 'dojox.form.DropDownSelect'],
		addOnLoad: scroll_initialize,
		parseOnLoad: true
};

})();