/**
 * Created by clouway on 15-6-15.
 */
var registerModule = angular.module("subnetModule", ['httpModule']);

registerModule.service("subnetGateway", ["httpRequest", function (httpRequest) {

  this.register = function (obj) {
    return httpRequest.send("POST", "/r/subnets", obj);
  };
}]);

registerModule.controller("SubnetCtrl", ["$scope", "subnetGateway", "$location", function ($scope, subnetGateway, $location) {

  $scope.handleSubnet = function (id) {
    $location.path('/bindings').search({"id": id});
  };

  $scope.registerSubnet = function (nodeId) {

    subnetGateway.register({
      "ip": $scope.networkIP,
      "nodeId": nodeId,
      "slash": $scope.slash
    }).then(function (data) {
      $scope.responce = data;
    }, function (error) {
      $scope.responce = error;
    });
  }
}]);

