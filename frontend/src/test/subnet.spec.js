/**
 * Created by clouway on 15-6-15.
 */
describe("subnetModule", function () {

  beforeEach(module("subnet"));

  describe("subnetGateway", function () {
    var scope, httpRequest, gateway;

    beforeEach(function () {
      httpRequest = {send: jasmine.createSpy().andReturn({promise: "DummyPromise"})};

      module(function ($provide) {
        $provide.value("httpRequest", httpRequest);
      });

      inject(function ($rootScope, subnetGateway) {
        gateway = subnetGateway;
        scope = $rootScope.$new();
      });

    });

    it("should register subnet", function () {
      var subnet = {"promise": "dummy"};
      var promise = gateway.register(subnet);

      expect(httpRequest.send).toHaveBeenCalledWith("POST", "/r/subnets", subnet);
      expect(promise).toEqual({promise: "DummyPromise"});
    });

  });

  describe("SubnetCtrl", function () {
    var scope,modal;

    beforeEach(inject(function ($rootScope, $controller) {
              scope = $rootScope.$new();

              modal = {
                open: jasmine.createSpy()
              };

              $controller("SubnetCtrl", {$scope: scope, $modal: modal});
            })
    );

    it("should open  subnet modal", function () {
      scope.add();

      expect(modal.open).toHaveBeenCalled();
    });

  });

  describe("NewSubnetCtrl", function () {
    var scope,deffer, gateway, modalInst;

    beforeEach(inject(function ($rootScope, $controller, $q) {
              deffer = $q.defer();
              modalInst = {
                dismiss: jasmine.createSpy()
              };
              scope = $rootScope.$new();
              gateway = {
                register: jasmine.createSpy().andReturn(deffer.promise)
              };
              $controller("NewSubnetCtrl", {$scope: scope, $modalInstance: modalInst, subnetGateway: gateway});
            })
    );

    it("should register subnet", function () {
      var subnet = {nodeId: "fakeNodeId"};
      scope.register(subnet);

      expect(gateway.register).toHaveBeenCalledWith(subnet);

      deffer.resolve("Success");
      scope.$digest();

      expect(scope.responce).toEqual("Success");
    });

    it("should dismiss modalInstance", function () {
      scope.cancel();

      expect(modalInst.dismiss).toHaveBeenCalledWith("cancel");
    });

  });
});
