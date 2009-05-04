dependencies = {
	layers: [
		{
			name: "ppwfacebookdemo.js",
			dependencies: [
				"app.src"
			]
		}
	],

	prefixes: [
//dojo is in target/webResources/js
		[ "dijit", "../dijit" ],
		[ "dojox", "../dojox" ],
//org and app prefixes are in the source directory
		[ "ppwcode", "../../../../../src/main/webapp/js/ppwcode" ],
		[ "app", "../../../../../src/main/webapp/js/app" ]
	]
}
