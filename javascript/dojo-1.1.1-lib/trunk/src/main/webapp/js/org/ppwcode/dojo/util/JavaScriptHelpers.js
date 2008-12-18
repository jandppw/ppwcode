dojo.provide("org.ppwcode.dojo.util.JavaScriptHelpers");

org.ppwcode.dojo.util.JavaScriptHelpers.getFunctionName = function(/*Function*/func) {
	var s = func.toString();
	s = s.substr(0, s.indexOf("()")).substr("function ".length);
	return s;
}