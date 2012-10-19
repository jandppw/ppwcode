define(["dojo/main", "util/doh/main", "contracts/declare"],
  function(dojo, doh, subjectDeclare) {

    doh.register("be/ppwcode/util/contracts/I/declare", [

      function testDoh() {
        console.log("test ran");
      }

    ]);

  }
);
