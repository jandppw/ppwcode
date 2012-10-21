define(["dojo/main", "util/doh/main", "contracts/contracts"],
  function(dojo, doh, c) {

    var booleanValue = true;
    var stringValue1 = "A property value";
    var stringValue2 = "Another property value";
    var numberValue1 = 3.14;
    var numberValue2 = Math.sqrt(2);
    var arrayValue = ["string value", -88284.994, null, [4, 5, 9], { propInObjectInArray: true}, function() { return true; }];
    var dateValue = new Date();
    var objectValue = { propInObjectinObject: 8 };
    var functionValue = function() {
      // object must have a property "stringProperty"
      return this.stringProperty;
    };
    var toStringMethod = function() {
      // object must have a method "functionProperty"
      return this.functionProperty();
    };
    var constructor = function() {
      this.stringProperty = stringValue2;
      this.numberProperty =  numberValue2;
    };

    function testResultInstanceProperty(resultInstance, propertyName, expectedValuePrototype, expectedValueInstance) {
      var resultPrototype = Object.getPrototypeOf(resultInstance);
      doh.t(resultPrototype.hasOwnProperty(propertyName));
      doh.is(expectedValuePrototype, resultPrototype[propertyName]);
      doh.is(expectedValueInstance, resultInstance[propertyName]);
    }

    function functionIsResultingFunctionFromContractMethod(fcmCandidate) {
      doh.isNot(null, fcmCandidate);
      doh.t(fcmCandidate.hasOwnProperty("pre"));
      doh.t(fcmCandidate.hasOwnProperty("impl"));
      doh.t(fcmCandidate.hasOwnProperty("post"));
      doh.is(fcmCandidate, fcmCandidate.impl);
    }

    doh.register("be/ppwcode/util/contracts/I/declare", [

      function testDoh() {
        console.log("test ran");
      },

      function testSimpleDeclare() {
        console.log("simple");
        var Result = subjectDeclare(null, {
          nullProperty : null,
          booleanProperty : booleanValue,
          stringProperty : stringValue1,
          numberProperty : numberValue1,
          arrayProperty : arrayValue,
          dateProperty : dateValue,
          objectProperty : objectValue,
          functionProperty : functionValue,
          toString: toStringMethod,
          constructor: constructor
        });
        var resultInstance = new Result();
        doh.is(Result, resultInstance.constructor);
//        doh.is(Object.getPrototypeOf(Result), Object.getPrototypeOf(resultInstance));
        testResultInstanceProperty(resultInstance, "nullProperty", null, null);
        testResultInstanceProperty(resultInstance, "booleanProperty", booleanValue, booleanValue);
        testResultInstanceProperty(resultInstance, "stringProperty", stringValue1, stringValue2);
        testResultInstanceProperty(resultInstance, "numberProperty", numberValue1, numberValue2);
        testResultInstanceProperty(resultInstance, "arrayProperty", arrayValue, arrayValue);
        testResultInstanceProperty(resultInstance, "dateProperty", dateValue, dateValue);
        testResultInstanceProperty(resultInstance, "objectProperty", objectValue, objectValue);
        testResultInstanceProperty(resultInstance, "functionProperty", functionValue, functionValue);
        testResultInstanceProperty(resultInstance, "toString", toStringMethod, toStringMethod);
        var resultPrototype = Object.getPrototypeOf(resultInstance);
        doh.t(resultPrototype.hasOwnProperty("constructor"));
        doh.is(resultInstance.constructor, resultPrototype.constructor)
      },

      function testContractDeclare() {
        console.log("contract");
        var Result = subjectDeclare(null, {
          invariants : [],
          nullProperty : null,
          booleanProperty : booleanValue,
          stringProperty : stringValue1,
          numberProperty : numberValue1,
          arrayProperty : arrayValue,
          dateProperty : dateValue,
          objectProperty : objectValue,
          functionProperty : functionValue,
          toString : toStringMethod,
          constructor : constructor
//          constructor : {
//            pre  : [],
//            impl : constructor,
//            post : []
//          },
          oneMoreMethod : function() {
            _c_pre(function() { return true; });
            _c_post(function(result) { return result === this.stringProperty; });
            _c_excp(function(e) { return e != null; });

            return this.stringProperty;
          }
        });
        var resultInstance = new Result();
        doh.is(Result, resultInstance.constructor);
// TODO why does this fail? doh.is(Object.getPrototypeOf(Result), Object.getPrototypeOf(resultInstance));
        testResultInstanceProperty(resultInstance, "nullProperty", null, null);
        testResultInstanceProperty(resultInstance, "booleanProperty", booleanValue, booleanValue);
        testResultInstanceProperty(resultInstance, "stringProperty", stringValue1, stringValue2);
        testResultInstanceProperty(resultInstance, "numberProperty", numberValue1, numberValue2);
        testResultInstanceProperty(resultInstance, "arrayProperty", arrayValue, arrayValue);
        testResultInstanceProperty(resultInstance, "dateProperty", dateValue, dateValue);
        testResultInstanceProperty(resultInstance, "objectProperty", objectValue, objectValue);
        testResultInstanceProperty(resultInstance, "functionProperty", functionValue, functionValue);
        testResultInstanceProperty(resultInstance, "toString", toStringMethod, toStringMethod);
        var resultPrototype = Object.getPrototypeOf(resultInstance);
//        doh.t(resultPrototype.hasOwnProperty("constructor"));
//        doh.is(resultInstance.constructor, resultPrototype.constructor);
//        testResultInstanceProperty(resultInstance, "oneMoreMethod", functionValue, functionValue);
// MUDO error        functionIsResultingFunctionFromContractMethod(resultInstance.constructor);
//        functionIsResultingFunctionFromContractMethod(resultInstance.oneMoreMethod);
      }

    ]);

  }
);
