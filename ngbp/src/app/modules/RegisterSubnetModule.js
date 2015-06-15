/**
 * Created by clouway on 15-6-15.
 */
var registerModule = angular.module("registerModule", ['httpModule']);

registerModule.service("subnetGateway", ["httpRequest", function (httpRequest) {

  this.getAllCategories = function () {
    return httpRequest.send("GET", "/r/category", "");
  };

  this.register = function (obj) {
    return httpRequest.send("POST", "/r/subnets/new", obj);
  };
}]);

registerModule.controller("RegisterCtrl", ["$scope", "subnetGateway", function ($scope, subnetGateway) {

  $scope.selected = "";

  subnetGateway.getAllCategories().then(function (data) {
    $scope.categories = data;
  }, function () {
    $scope.categories = [{type: "TV"}, {type: "Internet"}, {type: "Show"}, {type: "Something Else"}];
  });

  $scope.registerSubnet = function () {
    subnetGateway.register({
      "category": $scope.selected,
      "ip": $scope.networkIP,
      "slash": $scope.slash
    }).then(function (data) {
      $scope.success = data;
      $scope.error = "";

    }, function (error) {
      $scope.error = error;
      $scope.success = "";
    });
  }
}]);

