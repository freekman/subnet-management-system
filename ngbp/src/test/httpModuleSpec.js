/**
 * Created by clouway on 15-6-18.
 */
describe("httpModule", function () {

  var httpRequest, $httpBackend;

  beforeEach(module("httpModule"));

  beforeEach(inject(function ($injector) {
    httpRequest = $injector.get("httpRequest");
    $httpBackend = $injector.get('$httpBackend');
    $httpBackend.expectGET("/register").respond("Success!")
  }));
  it("should make a httpRequest ", function () {
    httpRequest.send("Get", "/register", "").then(function (res) {
      expect(res).toBe("Success!");
    });
    $httpBackend.flush();
  });
});