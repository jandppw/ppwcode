define(["dojo/main", "util/doh/main", "contracts/declare"],
  function(dojo, doh, subjectDeclare) {

    var propertyValue1 = "A property value";
    var propertyValue2 = "Another property value";
    var method = function() {
      // object must have a property "aProperty"
      return this.aProperty;
    };
    var toStringMethod = function() {
      // object must have a method "aMethod"
      return this.aMethod();
    };
    var constructor = function() {
      this.aProperty = propertyValue2;
    };

    doh.register("be/ppwcode/util/contracts/I/declare", [

      function testDoh() {
        console.log("test ran");
      },

      function testSimpleDeclare() {
        var Result = subjectDeclare(null, {
          aProperty : propertyValue1,
          aMethod : method,
          toString: toStringMethod,
          constructor: constructor
        });
        var resultInstance = new Result();
//        var propName;
//        console.log("properties of resultInstance START");
//        for (propName in resultInstance) {
//          console.log(propName + ": " + resultInstance[propName]);
//        }
//        console.log("properties of resultInstance DONE");
//        console.log("properties of resultInstance.prototype START");
//        for (propName in resultInstance.__proto__) {
//          console.log(propName + ": " + resultInstance[propName]);
//        }
//        console.log("properties of resultInstance.prototype DONE");
        doh.t(resultInstance.__proto__.hasOwnProperty("aProperty"));
        doh.is(propertyValue1, resultInstance.__proto__.aProperty);
        doh.is(propertyValue2, resultInstance.aProperty);
        doh.t(resultInstance.__proto__.hasOwnProperty("aMethod"));
        doh.is(method, resultInstance.aMethod);
        doh.is(propertyValue1, resultInstance.__proto__.aMethod());
        doh.is(propertyValue2, resultInstance.aMethod());
        doh.t(resultInstance.__proto__.hasOwnProperty("toString"));
        doh.is(toStringMethod, resultInstance.toString);
        doh.is(propertyValue1, resultInstance.__proto__.toString());
        doh.is(propertyValue2, resultInstance.toString());
      }

    ]);

  }
);
