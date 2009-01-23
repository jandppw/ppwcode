dojo.provide("org.ppwcode.dojo.dijit.layout.TitledContentPane");
dojo.experimental("org.ppwcode.dojo.dijit.layout.TitledContentPane");

dojo.require("dojox.layout.ContentPane");
dojo.require("dojox.fx.Shadow");
dojo.require("dijit._Templated");
dojo.require("dijit._Widget");

dojo.declare("org.ppwcode.dojo.dijit.layout.TitledContentPane",
	[ dojox.layout.ContentPane, dijit._Templated ],
	{
	// summary:
	//		A non-modal Floating window.
	//
	// description:
	// 		Makes a dijit.ContentPane float and draggable by it's title [similar to TitlePane]
	// 		and over-rides onClick to onDblClick for wipeIn/Out of containerNode
	// 		provides minimize(dock) / show() and hide() methods, and resize [almost] 

	// title: String
	//		Title to use in the header
	title: "",

	/*=====
	// iconSrc: String
	//		[not implemented yet] will be either icon in titlepane to left
	//		of Title, and/or icon show when docked in a fisheye-like dock
	//		or maybe dockIcon would be better?
	iconSrc: null,
	=====*/

	'class': "",
	// contentClass: String
	// 		The className to give to the inner node which has the content
	contentClass: "dojoxTitledPaneContent",
	
	paddingSize: 10,
	margin: [],
	showShadow: false,

	// privates:
	_currentState: null,
	_shadow: null,

	templateString: null,
	templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/layout/templates/TitledContentPane.html"),
	
	postCreate: function(){
	
		this.setTitle(this.title);
		if (this.showShadow) {
			this._shadow = new dojox.fx.Shadow({node:this.domNode});
			this.setMargin(this._shadow.shadowThickness + "px");
			this._shadow.startup();
		} else if (this.margin.length > 0) {
			this.setMargin(this.getMarginString(this.margin));
		}

		this.inherited(arguments);
	},
	
	//Returns String
	getMarginString: function(/*Object*/object) {
		console.log(object.length);
		if (object.length == 1) {
			return object[0] + "px";
		} else if (object.length == 4) {
			return object[0] + "px " + object[1] + "px " + object[2] + "px " + object[3] + "px";
		} else {
			throw new Error("Error while setting the margin!!\nExamples of how to use the margin tag:\n-With 4 numbers: margin=10,0,10,5\n-With 1 number: margin=10");
		}
	},
	
	startup: function(){
		if(this._started){ return; }

		// wire up the tablist and its tabs
		this.inherited(arguments);

		if(dojo.isSafari){
			// sometimes safari 3.0.3 miscalculates the height of the tab labels, see #4058
			setTimeout(dojo.hitch(this, "layout"), 0);
		}
		
		if(dojo.isIE){
			this.canvas.style.overflow = "auto";
		} else {
			this.containerNode.style.overflow = "auto";
		}		
		this.resize(dojo.coords(this.domNode));
		this._started = true;
	},

	setTitle: function(/* String */ title){
		// summary: Update the Title bar with a new string
		this.titleNode.innerHTML = title; 
		this.title = title; 
	},
	
	calcSize: function(/*DomNode|String*/dn) {
		return dojo.contentBox(dn);
	},
	
	setMargin: function(/*String*/margin) {
		var dns = this.domNode.style;
		dns.margin = margin;
		//dns.margin = this._shadow.shadowThickness + "px";
	},
		
	//
	resize: function(/*Object*/dim){
		// summary: Size the FloatingPane and place accordingly
		if (!dim){
			dim = this.calcSize(this.domNode);
		}
		if (dojo.isIE) {
			var coord = dojo.coords(this.domNode);
			dim.h = coord.h;
		}
	
		//Adding padding to contentPane
		var dns = this.canvas.style;
		dns.margin = this.paddingSize+"px";
		
		// Now resize canvas
		var factor = 2;
		if(dojo.isIE){
			factor = 4;
		}
		var mbCanvas = { l: 0, t: 0, w: dim.w - (this.paddingSize * factor), h: (dim.h - this.focusNode.offsetHeight) - (this.paddingSize * factor)};
		
		
		// If the single child can resize, forward resize event to it so it can
		// fit itself properly into the content area
		this._checkIfSingleChild();
		if(this._singleChild && this._singleChild.resize){
			this._singleChild.resize(mbCanvas);
		}

		if (this._shadow) {
			this._shadow.resize();
		}
	},	
	
	layout: function(){
		// Summary: Configure the content pane to take up all the space except for where the tabs are
		//if(!this.doLayout){ return; }

		// position and size the titles and the container node
		var titleAlign = this.tabPosition.replace(/-h/,"");
		var children = [
			{ domNode: this.titleNode.domNode, layoutAlign: titleAlign },
			{ domNode: this.containerNode, layoutAlign: "client" }
		];
		dijit.layout.layoutChildren(this.domNode, this._contentBox, children);

		// Compute size to make each of my children.
		// children[1] is the margin-box size of this.containerNode, set by layoutChildren() call above
		this._containerContentBox = dijit.layout.marginBox2contentBox(this.containerNode, children[1]);

		if(this.selectedChildWidget){
			this._showChild(this.selectedChildWidget);
			if(this.doLayout && this.selectedChildWidget.resize){
				this.selectedChildWidget.resize(this._containerContentBox);
			}
		}
	},	
	
	destroy: function(){
		this.inherited(arguments);
	}
});