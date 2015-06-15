/**
 * Created by clouway on 15-6-15.
 */
describe("registerModule", function () {
  var scope, httpRequest, deffer, gateway;

  beforeEach(module("registerModule"));

  beforeEach(module(function ($provide) {
    httpRequest = {
      send: jasmine.createSpy()
    };
    $provide.value("httpRequest", httpRequest);
  }));

  describe("subnetGateway", function () {

    beforeEach(inject(function ($controller, $rootScope, subnetGateway) {
      gateway = subnetGateway;
      scope = $rootScope.$new();
    }));

    it("should send a get for all categories", function () {
      gateway.getAllCategories();

      expect(httpRequest.send).toHaveBeenCalledWith("GET", "/r/category", "");
    });
    it("should send post to register a subnet", function () {
      var dummyObject = {"type": "dummy"};
      gateway.register(dummyObject);

      expect(httpRequest.send).toHaveBeenCalledWith("POST", "/r/subnets/new", dummyObject)
    });
  });
  describe("RegisterCtrl", function () {

    beforeEach(inject(function ($rootScope, $controller, $q) {
      deffer = $q.defer();
      scope = $rootScope.$new();
      gateway = {
        getAllCategories: jasmine.createSpy().andReturn(deffer.promise),
        register: jasmine.createSpy().andReturn(deffer.promise)
      };
      $controller("RegisterCtrl", {$scope: scope, subnetGateway: gateway});
    }));

    it("should fetch categories", function () {
      var dummyObj = {"type": "TV"};
      deffer.resolve(dummyObj);

      expect(gateway.getAllCategories).toHaveBeenCalled();

      scope.$digest();

      expect(scope.categories).toEqual(dummyObj);
    });

    it("should register subnet", function () {
      var dummyObj = {"type": "Success"};
      scope.registerSubnet();
      deffer.resolve(dummyObj);

      expect(gateway.register).toHaveBeenCalled();

      scope.$digest();

      expect(scope.success).toEqual(dummyObj);

    });

  });

});
