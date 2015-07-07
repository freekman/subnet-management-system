/**
 * Created by clouway on 15-6-15.
 */
describe("subnetModule", function () {
  var scope, httpRequest, deffer, gateway, state,location;

  beforeEach(module("subnetModule"));

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

    it("should send post to register a subnet", function () {
      var dummyObject = {"type": "dummy"};
      gateway.register(dummyObject);

      expect(httpRequest.send).toHaveBeenCalledWith("POST", "/r/subnets", dummyObject)
    });
  });

  describe("RegisterCtrl", function () {

    beforeEach(inject(function ($rootScope, $controller, $q,$location) {
              deffer = $q.defer();
              scope = $rootScope.$new();
              location=$location;
              state = {
                go: jasmine.createSpy()
              };
              gateway = {
                register: jasmine.createSpy().andReturn(deffer.promise)
              };
              $controller("SubnetCtrl", {$scope: scope, subnetGateway: gateway, $state: state,$location:location});
            })
    );


    it("should register subnet", function () {
      var dummyObj = {"type": "Success"};
      scope.networkIP = "0.0.0.0";
      scope.slash = "23";

      scope.registerSubnet("132");

      deffer.resolve(dummyObj);

      expect(gateway.register).toHaveBeenCalledWith({ip: '0.0.0.0', nodeId: '132', slash: '23'});

    });

  });

})
;