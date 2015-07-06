var binding = angular.module("bindingModule", ["httpModule"]);

binding.service("bindingGateway", ["httpRequest", function (httpRequest) {

  this.getSubnetById = function (id) {
    return httpRequest.send("GET", "/r/subnets/" + id);
  };
  this.removeSubnetById = function (id) {
    return httpRequest.send("DELETE", "/r/subnets/" + id);
  };
  this.resizeSubnet = function (id, newSlash) {
    return httpRequest.send("PUT", "/r/subnets/" + id + "/resize", newSlash);
  };
  this.updateDescription = function (id, description) {
    return httpRequest.send("PUT", "/r/subnets/" + id + "/description", description);
  };
  this.findBinding = function (ip, id) {
    return httpRequest.send("GET", "/r/bindings/" + ip, id);
  };
}]);

binding.controller("BindingCtrl", ["$scope", "$stateParams", "bindingGateway", "$location","$state",function ($scope, $stateParams, bindingGateway, $location,$state) {
  var id = getId().id;

  //To Remove just for  test.
  $scope.subnet = {"subnetIP": "0.0.0.0", "slash": "30", "description": "note"};

  extractSubnet();

  $scope.updateDescription = function (newDescription) {

    bindingGateway.updateDescription(id, {"text": newDescription}).then(function () {

    }, function () {

    });
  };
  $scope.removeSubnet = function () {
    bindingGateway.removeSubnetById(id).then(function () {
      extractSubnet();
    });
  };

  $scope.resize = function (newSlash) {
    bindingGateway.resizeSubnet(id, {"slash": newSlash}).then(function () {
      $scope.resizeError = undefined;
      extractSubnet();
    }, function (err) {
      $scope.resizeError = err;
    });
  };

  $scope.findBinding = function (bindingIP) {

    bindingGateway.findBinding(bindingIP, id).then(function (data) {
      $scope.binding = data;
    }, function (error) {
      $scope.bindingError = error;
    });
  };

  function getId() {
    return $location.search();
  }

  function extractSubnet() {
    bindingGateway.getSubnetById(id)
            .then(function (data) {
              $scope.subnet = data;
            }, function () {
              $state.go("subnet");
            });
  }
}]);