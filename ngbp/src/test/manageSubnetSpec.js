///**
// * Created by clouway on 15-7-2.
// */
//ddescribe("manageSubnetModule", function () {
//  var subGateway, scope, deffer;
//
//  beforeEach(module("manageSubnetModule"));
//
//  beforeEach(module(function ($provide) {
//    httpRequest = {
//      send: jasmine.createSpy()
//    };
//    $provide.value("httpRequest", httpRequest);
//  }));
//
//  describe("gateway", function () {
//
//    beforeEach(inject(function (gateway) {
//      subGateway = gateway;
//    }));
//
//    it("should make request for all subnets", function () {
//      subGateway.getAllSubnets();
//      expect(httpRequest.send).toHaveBeenCalledWith("GET", "/r/subnets", "");
//    });
//  });
//  describe("ManageSubnetCtrl", function () {
//
//    beforeEach(inject(function ($controller, $q, $rootScope) {
//      scope = $rootScope.$new();
//      deffer = $q.defer();
//      subGateway = {
//        getAllSubnets: jasmine.createSpy().andReturn(deffer.promise)
//      };
//      $controller("ManageSubnetCtrl", {$scope: scope, gateway: subGateway});
//    }));
//
//    it("should fetch all subnets", function () {
//      var dummySubnet = {"type": "Dummy"};
//      deffer.resolve(dummySubnet);
//
//      expect(subGateway.getAllSubnets).toHaveBeenCalled();
//
//      scope.$digest();
//
//      expect(scope.subnets).toEqual(dummySubnet);
//    });
//
//  });
//})
//;