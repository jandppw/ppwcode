dojo.provide("org.ppwcode.dojo.util.JavaScriptHelpers");

org.ppwcode.dojo.util.JavaScriptHelpers.getFunctionName = function(/*Function*/func) {
	var result = null;
	if (func.$dwrClassName) {
		result = func.$dwrClassName;
	} else {
		var s = func.toString();
		result = s.substr(0, s.indexOf("()")).substr("function ".length);
	}
	return result;
}