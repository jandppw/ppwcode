define(["dojo/main", "util/doh", "be/ppwcode/util/contracts/I/declare"],
  function(dojo, doh, subjectDeclare) {

    doh.register("be/ppwcode/util/contracts/I/declare", [

      function testConstructor() {
        var idStub = "ID stub";
        var subject = new Loggable({id: idStub});
        doh.invars(subject);
        // post
        doh.is(idStub, subject.id);
      },

      function testToString() {
        var subject = new Loggable({id: "test"});
        var result = subject.toString();
        doh.isNot(null, result);
        doh.t(typeof result === "string");
        doh.isNot("", result);
        console.log(result);
      }

    ]);

  }
);
