/*
 Copyright 2013 - $Date $ by PeopleWare n.v.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

define(["dojo/main", "util/doh/main", "dojo/_base/window", "require"],

  function(dojo, doh, window, require) {

    var secrets = ['secrets'];

    doh.register("WeakMapDemo", [

      function testLoad() {
        var deferred = new doh.Deferred();
        require(["harmony-collections/harmony-collections"],
          function() {
            var WeakMap = window.global.WeakMap;
            try {
              doh.t(WeakMap);
              doh.is(["constructor", "delete", "get", "has", "set", "toString"],
                Object.getOwnPropertyNames(WeakMap.prototype).sort());
              doh.is("WeakMap", WeakMap.name);
              doh.is("function WeakMap() { [native code] }", WeakMap + "");
              doh.is("[object WeakMap]", WeakMap.prototype+'');
              deferred.callback(WeakMap);
            }
            catch (err) {
              deferred.errback(err);
            }
          }
        );
        return deferred;
      },

      function testCreate() {
        var deferred = new doh.Deferred();
        require(["harmony-collections/harmony-collections"],
          function() {
            var WeakMap = window.global.WeakMap;
            try {
              var subject = new WeakMap();
              doh.t(subject);
              doh.t(subject instanceof WeakMap);
              doh.is(WeakMap.prototype, Object.getPrototypeOf(subject));
              doh.is([], Object.getOwnPropertyNames(subject));
              deferred.callback(WeakMap);
            }
            catch (err) {
              deferred.errback(err);
            }
          }
        );
        return deferred;
      },

      function testGet1() {
        var deferred = new doh.Deferred();
        require(["harmony-collections/harmony-collections"],
          function() {
            var WeakMap = window.global.WeakMap;
            try {
              var subject = new WeakMap();
              var result = subject.get(WeakMap);
              doh.f(result);
              doh.t(typeof result === "undefined");
              deferred.callback(WeakMap);
            }
            catch (err) {
              deferred.errback(err);
            }
          }
        );
        return deferred;
      },

      function testSet() {
        var deferred = new doh.Deferred();
        require(["harmony-collections/harmony-collections"],
          function() {
            var WeakMap = window.global.WeakMap;
            try {
              var subject = new WeakMap();
              var key = {};
              var result = subject.set(WeakMap, secrets);
              doh.f(result);
              doh.t(typeof result === "undefined");
              deferred.callback(WeakMap);
            }
            catch (err) {
              deferred.errback(err);
            }
          }
        );
        return deferred;
      }

//              t.same(map.get(WM), undefined, 'retreiving non-existant key returns undefined');
//              t.same(map.set(WM, secrets), undefined, 'set works and returns undefined');
//              t.same(map.get(WM), secrets, 'retreiving works');
//              t.same(map.set(WM, 'overwrite'), undefined, 'primitive value set works');
//              t.same(map.get(WM), 'overwrite', 'overwriting works');
//              t.same(map.has(WM), true, 'has returns true');
//              t.same(map.delete(WM), true, 'delete returns true');
//              t.same(map.has(WM), false, 'has returns false');
//              t.same(map.get(WM), undefined, 'retreiving deleted item returns undefined');

    ]);

  }
);
