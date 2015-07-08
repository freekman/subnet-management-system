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
  this.updateSubnetDescription = function (id, description) {
    return httpRequest.send("PUT", "/r/subnets/" + id + "/description", description);
  };
  this.updateBindingDescription = function (id, description) {
    return httpRequest.send("PUT", "/r/bindings/" + id + "/description", description);
  };
  this.findBinding = function (id, bindingIP) {
    return httpRequest.send("POST", "/r/bindings/" + id, bindingIP);
  };
}]);

binding.controller("BindingCtrl", ["$scope", "$stateParams", "bindingGateway", "$location", "$state", function ($scope, $stateParams, bindingGateway, $location, $state) {
  var id = getId().id;
  //To Remove just for  test.
  $scope.subnet = {"subnetIP": "0.0.0.0", "slash": "30", "description": "note"};
  //$scope.binding = {"ip": "0.0.0.0", "description": "note"};

  extractSubnet();

  $scope.updateSubnetDescription = function (newDescription) {
    bindingGateway.updateSubnetDescription(id, {"text": newDescription})
  };

  $scope.updateBindingDescription = function (newDescription) {
    console.log(newDescription);
    bindingGateway.updateBindingDescription($scope.binding.id, {"text": newDescription});
  };

  $scope.removeSubnet = function () {
    bindingGateway.removeSubnetById(id).then(function () {
      extractSubnet();
    });
  };

  $scope.resize = function (newSlash) {
    bindingGateway.resizeSubnet(id, {"value": newSlash}).then(function () {
      $scope.resizeError = undefined;
      extractSubnet();
    }, function (err) {
      $scope.resizeError = err;
    });
  };

  $scope.findBinding = function (bindingIP) {

    bindingGateway.findBinding(id, {"value": bindingIP}).then(function (data) {
      $scope.bindingError = undefined;
      $scope.binding = data;
    }, function (error) {
      $scope.binding = undefined;
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
              // $state.go("subnet");
            });
  }
}]);