/**
 * Created by clouway on 15-7-6.
 */
describe("bindingModule", function () {
  var httpRequest, bindGateway, scope, deffer, state, stateParams;

  beforeEach(module("bindingModule"));

  beforeEach(module(function ($provide) {
    httpRequest = {
      send: jasmine.createSpy()
    };
    $provide.value("httpRequest", httpRequest);
  }));

  describe("bindingGateway", function () {

    beforeEach(inject(function (bindingGateway) {
      bindGateway = bindingGateway;
    }));

    it("should make a request for subnet by id", function () {
      bindGateway.getSubnetById("abc");
      expect(httpRequest.send).toHaveBeenCalledWith('GET', '/r/subnets/abc');
    });

    it("should make a request to delete subnet by id", function () {
      bindGateway.removeSubnetById("abc");
      expect(httpRequest.send).toHaveBeenCalledWith('DELETE', '/r/subnets/abc');
    });

    it("should make a request to resize subnet", function () {
      bindGateway.resizeSubnet("abc", "23");
      expect(httpRequest.send).toHaveBeenCalledWith('PUT', '/r/subnets/abc/resize', "23");
    });

    it("should make a request to update  subnet description", function () {
      bindGateway.updateDescription("abc", "TV");
      expect(httpRequest.send).toHaveBeenCalledWith('PUT', '/r/subnets/abc/description', "TV");
    });

    it("should make a request for subnet binging", function () {
      bindGateway.findBinding("0.0.0.0", "abc");
      expect(httpRequest.send).toHaveBeenCalledWith('GET', '/r/bindings/0.0.0.0', "abc");
    });


  });

  describe("BindingCtrl", function () {

    beforeEach(inject(function ($controller, $rootScope, $q) {
      scope = $rootScope.$new();
      deffer = $q.defer();
      state = {
        go: jasmine.createSpy()
      };
      stateParams = {
        id: jasmine.createSpy().andReturn("nodeId")
      };
      bindGateway = {
        getSubnetById: jasmine.createSpy().andReturn(deffer.promise),
        removeSubnetById: jasmine.createSpy().andReturn(deffer.promise),
        resizeSubnet: jasmine.createSpy().andReturn(deffer.promise),
        updateDescription: jasmine.createSpy().andReturn(deffer.promise),
        findBinding: jasmine.createSpy().andReturn(deffer.promise)
      };

      $controller("BindingCtrl", {
        $scope: scope,
        $stateParams: stateParams,
        bindingGateway: bindGateway,
        $state: state
      });
    }));

    it("should get a subnet by id", function () {
      var dummySubnet = {"type": "dummySubnet"};
      deffer.resolve(dummySubnet);
      scope.$digest();

      expect(scope.subnet).toEqual(dummySubnet);
    });

    it("should redirect to default page if subnet not found", function () {
      deffer.reject();
      scope.$digest();

      expect(state.go).toHaveBeenCalledWith("subnet");
    });

    it("shuold remove a subnet", function () {
      scope.removeSubnet();

      expect(bindGateway.removeSubnetById).toHaveBeenCalledWith(stateParams.id);

    });

    it("shuold resize a subnet", function () {
      scope.resize("29");
      deffer.resolve();

      expect(bindGateway.resizeSubnet).toHaveBeenCalledWith(stateParams.id, {"slash": "29"});
    });

    it("shuold not resize a subnet", function () {
      scope.resize("29");
      deffer.reject("Error");
      scope.$digest();

      expect(bindGateway.resizeSubnet).toHaveBeenCalledWith(stateParams.id, {"slash": "29"});

      expect(scope.resizeError).toEqual("Error");
    });

    it("should update description", function () {
      scope.updateDescription("TV");

      expect(bindGateway.updateDescription).toHaveBeenCalledWith(stateParams.id, {"text": "TV"})
    });

    it("should find binding", function () {
      var dummyBinding = {"ip": "0.0.0.0"};
      var id = {"id": "abc"};
      scope.findBinding("0.0.0.0", id);
      deffer.resolve(dummyBinding);
      scope.$digest();

      expect(bindGateway.findBinding).toHaveBeenCalledWith("0.0.0.0",stateParams.id);
      expect(scope.binding).toEqual(dummyBinding);
    });

  });

});