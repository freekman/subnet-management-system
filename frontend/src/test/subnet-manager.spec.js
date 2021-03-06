/**
 * Created by clouway on 15-7-21.
 */
describe("subnet-manager", function () {

  beforeEach(module('subnet-manager'));

  describe("SubnetManagerCtrl", function () {
    var scope, gateway, deffer, location;

    beforeEach(function () {

      inject(function ($controller, $rootScope, $q) {
        scope = $rootScope.$new();
        deffer = $q.defer();
        gateway = {
          getById: jasmine.createSpy().andReturn(deffer.promise)
        };

        location = {
          search: jasmine.createSpy().andReturn({"id": "fakeID"})
        };

        $controller('SubnetManagerCtrl', {
          $scope: scope,
          $location: location,
          subnetGateway: gateway
        })
      });
    });

    it("should get subnet by id", function () {
      var subnet = {"ip": "0.0.0.0", "slash": "23"};

      expect(location.search).toHaveBeenCalled();
      expect(gateway.getById).toHaveBeenCalledWith('fakeID');

      deffer.resolve(subnet);
      scope.$digest();

      expect(scope.subnet).toEqual(subnet);
    });

  });

});