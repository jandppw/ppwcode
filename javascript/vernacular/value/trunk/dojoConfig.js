/*
  Configuration of the application.
  This file contains the development configuration.
  The build process replaces this file with another version for a specific release
 */

// dojo config

var dojoConfig = {
  basePath: "./lib/dojo/dojo",
  async: true,
  parseOnLoad: false,
  isDebug: true,
  has: {
    "dojo-firebug": false,
    "dojo-debug-messages": true,
    "mvc-bindings-log-api": true,
    "ppwcode-contracts-precondition": true
  },
  packages: [
    // dojo, dgrid, dojox are siblings
    {name: "dijit", location: "../dijit"},
    {name: "dojox", location: "../dojox"},
    {name: "ppwcode-util-contracts", location: "../../ppwcode/util/contracts"}
  ]
};
