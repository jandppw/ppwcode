define(["dojo/_base/declare"], function(dojoDeclare) {

  function _crackParameters(className, superclass, props) {
    // copied from dojo/_base/declare.js - declare - first 6 lines of code

    // summary:
    //    Returns an object with properties "className", "superclass" and "props",
    //    taking into account that in the arguments className is optional; that
    //    superclass may be null, a (constructor) Function, or an Array of
    //    (constructor) Functions; that props is an optional object.
    // returns: Object
    // description:
    //    When there is no className, the first argument is intended to be
    //    superclass, and the second argument is intended to be props.
    //    The resulting object is then
    //    {"className" : "", "superclass" : arguments[0], "props" : arguments[1]}.
    //    When there is a className, the resulting object is
    //    {"className" : arguments[0], "superclass" : arguments[1], "props" : arguments[2]}.
    if(typeof className != "string"){
      props = superclass;
      superclass = className;
      className = "";
    }
    props = props || {};

    return {"className" : className, "superclass" : superclass, "props" : props};
  }

  var ppwcodeDeclare = function(className, superclass, props) {
    // summary:
    //		Create a feature-rich constructor from compact notation.
    // className: String?
    //		The optional name of the constructor (loosely, a "class")
    //		stored in the "declaredClass" property in the created prototype.
    //		It will be used as a global name for a created constructor.
    // superclass: Function|Function[]
    //		May be null, a Function, or an Array of Functions. This argument
    //		specifies a list of bases (the left-most one is the most deepest
    //		base).
    // props: Object
    //		An object whose properties are copied to the created prototype.
    //		Add an instance-initialization function by making it a property
    //		named "constructor".
    // returns: dojo/_base/declare.__DeclareCreatedObject
    //		New constructor function.
    // description:
    //		Create a constructor using a compact notation for inheritance and
    //		prototype extension.
    //
    //		Mixin ancestors provide a type of multiple inheritance.
    //		Prototypes of mixin ancestors are copied to the new class:
    //		changes to mixin prototypes will not affect classes to which
    //		they have been mixed in.
    //
    //		Ancestors can be compound classes created by this version of
    //		declare(). In complex cases all base classes are going to be
    //		linearized according to C3 MRO algorithm
    //		(see http://www.python.org/download/releases/2.3/mro/ for more
    //		details).
    //
    //		"className" is cached in "declaredClass" property of the new class,
    //		if it was supplied. The immediate super class will be cached in
    //		"superclass" property of the new class.
    //
    //		Methods in "props" will be copied and modified: "nom" property
    //		(the declared name of the method) will be added to all copied
    //		functions to help identify them for the internal machinery. Be
    //		very careful, while reusing methods: if you use the same
    //		function under different names, it can produce errors in some
    //		cases.
    //
    //		It is possible to use constructors created "manually" (without
    //		declare()) as bases. They will be called as usual during the
    //		creation of an instance, their methods will be chained, and even
    //		called by "this.inherited()".
    //
    //		Special property "-chains-" governs how to chain methods. It is
    //		a dictionary, which uses method names as keys, and hint strings
    //		as values. If a hint string is "after", this method will be
    //		called after methods of its base classes. If a hint string is
    //		"before", this method will be called before methods of its base
    //		classes.
    //
    //		If "constructor" is not mentioned in "-chains-" property, it will
    //		be chained using the legacy mode: using "after" chaining,
    //		calling preamble() method before each constructor, if available,
    //		and calling postscript() after all constructors were executed.
    //		If the hint is "after", it is chained as a regular method, but
    //		postscript() will be called after the chain of constructors.
    //		"constructor" cannot be chained "before", but it allows
    //		a special hint string: "manual", which means that constructors
    //		are not going to be chained in any way, and programmer will call
    //		them manually using this.inherited(). In the latter case
    //		postscript() will be called after the construction.
    //
    //		All chaining hints are "inherited" from base classes and
    //		potentially can be overridden. Be very careful when overriding
    //		hints! Make sure that all chained methods can work in a proposed
    //		manner of chaining.
    //
    //		Once a method was chained, it is impossible to unchain it. The
    //		only exception is "constructor". You don't need to define a
    //		method in order to supply a chaining hint.
    //
    //		If a method is chained, it cannot use this.inherited() because
    //		all other methods in the hierarchy will be called automatically.
    //
    //		Usually constructors and initializers of any kind are chained
    //		using "after" and destructors of any kind are chained as
    //		"before". Note that chaining assumes that chained methods do not
    //		return any value: any returned value will be discarded.
    //
    // example:
    //	|	declare("my.classes.bar", my.classes.foo, {
    //	|		// properties to be added to the class prototype
    //	|		someValue: 2,
    //	|		// initialization function
    //	|		constructor: function(){
    //	|			this.myComplicatedObject = new ReallyComplicatedObject();
    //	|		},
    //	|		// other functions
    //	|		someMethod: function(){
    //	|			doStuff();
    //	|		}
    //	|	});
    //
    // example:
    //	|	var MyBase = declare(null, {
    //	|		// constructor, properties, and methods go here
    //	|		// ...
    //	|	});
    //	|	var MyClass1 = declare(MyBase, {
    //	|		// constructor, properties, and methods go here
    //	|		// ...
    //	|	});
    //	|	var MyClass2 = declare(MyBase, {
    //	|		// constructor, properties, and methods go here
    //	|		// ...
    //	|	});
    //	|	var MyDiamond = declare([MyClass1, MyClass2], {
    //	|		// constructor, properties, and methods go here
    //	|		// ...
    //	|	});
    //
    // example:
    //	|	var F = function(){ console.log("raw constructor"); };
    //	|	F.prototype.method = function(){
    //	|		console.log("raw method");
    //	|	};
    //	|	var A = declare(F, {
    //	|		constructor: function(){
    //	|			console.log("A.constructor");
    //	|		},
    //	|		method: function(){
    //	|			console.log("before calling F.method...");
    //	|			this.inherited(arguments);
    //	|			console.log("...back in A");
    //	|		}
    //	|	});
    //	|	new A().method();
    //	|	// will print:
    //	|	// raw constructor
    //	|	// A.constructor
    //	|	// before calling F.method...
    //	|	// raw method
    //	|	// ...back in A
    //
    // example:
    //	|	var A = declare(null, {
    //	|		"-chains-": {
    //	|			destroy: "before"
    //	|		}
    //	|	});
    //	|	var B = declare(A, {
    //	|		constructor: function(){
    //	|			console.log("B.constructor");
    //	|		},
    //	|		destroy: function(){
    //	|			console.log("B.destroy");
    //	|		}
    //	|	});
    //	|	var C = declare(B, {
    //	|		constructor: function(){
    //	|			console.log("C.constructor");
    //	|		},
    //	|		destroy: function(){
    //	|			console.log("C.destroy");
    //	|		}
    //	|	});
    //	|	new C().destroy();
    //	|	// prints:
    //	|	// B.constructor
    //	|	// C.constructor
    //	|	// C.destroy
    //	|	// B.destroy
    //
    // example:
    //	|	var A = declare(null, {
    //	|		"-chains-": {
    //	|			constructor: "manual"
    //	|		}
    //	|	});
    //	|	var B = declare(A, {
    //	|		constructor: function(){
    //	|			// ...
    //	|			// call the base constructor with new parameters
    //	|			this.inherited(arguments, [1, 2, 3]);
    //	|			// ...
    //	|		}
    //	|	});
    //
    // example:
    //	|	var A = declare(null, {
    //	|		"-chains-": {
    //	|			m1: "before"
    //	|		},
    //	|		m1: function(){
    //	|			console.log("A.m1");
    //	|		},
    //	|		m2: function(){
    //	|			console.log("A.m2");
    //	|		}
    //	|	});
    //	|	var B = declare(A, {
    //	|		"-chains-": {
    //	|			m2: "after"
    //	|		},
    //	|		m1: function(){
    //	|			console.log("B.m1");
    //	|		},
    //	|		m2: function(){
    //	|			console.log("B.m2");
    //	|		}
    //	|	});
    //	|	var x = new B();
    //	|	x.m1();
    //	|	// prints:
    //	|	// B.m1
    //	|	// A.m1
    //	|	x.m2();
    //	|	// prints:
    //	|	// A.m2
    //	|	// B.m2


    var trueArgs= _crackParameters(className, superclass, props);
    return dojoDeclare(trueArgs.className, trueArgs.superclass, trueArgs.props);
  }

  return ppwcodeDeclare;

});
