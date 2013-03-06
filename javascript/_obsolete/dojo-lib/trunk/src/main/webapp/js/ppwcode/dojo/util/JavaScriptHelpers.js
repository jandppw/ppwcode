dojo.provide("ppwcode.dojo.util.JavaScriptHelpers");

ppwcode.dojo.util.JavaScriptHelpers.getConstructorFunctionName = function(/*Function*/func) {
	var result = func.toString();
	result = dojo.trim(result.substr(0, result.indexOf("()")).substr("function".length));
	if (result == "") {
		//in case of DWR 3, we use the $dwrClassName property.  In DWR 3
		//all contructor functions are anonymous
		if (func.$dwrClassName) {
			result = func.$dwrClassName;
		} else {
			//worst case scenario:  a print of the function :/
			result = func.toString();
		}
	}
	return result;
}