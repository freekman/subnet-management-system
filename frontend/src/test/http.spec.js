/**
 * Created by clouway on 15-6-18.
 */
describe("http-module", function () {

  beforeEach(module("http"));

  beforeEach(module(function ($provide) {
    notification = {
      addMessage: jasmine.createSpy()
    };
    $provide.value("notification", notification);
  }));

  describe("httpRequest", function () {
    var httpRequest, $httpBackend;

    beforeEach(inject(function ($injector) {
      httpRequest = $injector.get("httpRequest");
      $httpBackend = $injector.get('$httpBackend');
    }));

    it("should make a successful httpRequest ", function () {
      var promise = httpRequest.send("Get", "/register", "");
      $httpBackend.expectGET("/register").respond("Success!");

      promise.then(function (res) {
        expect(res).toBe("Success!");
      });
      $httpBackend.flush();
    });

    it("should make a unsuccessful httpRequest ", function () {
      var promise = httpRequest.send("Get", "/register", "");
      $httpBackend.expectGET("/register").respond(400, 'Error');

      promise.then(function () {
      }, function (err) {
        expect(err).toBe("Error");

      });
      $httpBackend.flush();
    });

  });

  describe("errorInterceptor", function () {
    var httpRequest, $httpBackend, interceptor, note;

    beforeEach(inject(function ($injector, errorInterceptor, notification) {
      note = notification;
      interceptor = errorInterceptor;
      httpRequest = $injector.get("httpRequest");
      $httpBackend = $injector.get('$httpBackend');
    }));

    it("should intercept an error", function () {
      var rejection = {status: 400, data: "Not found"};
      interceptor.responseError(rejection);

      expect(note.addMessage).toHaveBeenCalledWith("Not found");
    });
  });

});