/**
 * Created by clouway on 15-6-29.
 */
var manager = angular.module("manageSubnetModule", ["httpModule"]);

manager.service("gateway", ["httpRequest", function (httpRequest) {

  this.getAllSubnets = function () {
    return httpRequest.send("GET", "/r/subnets", "");
  };
  this.removeSubnet = function (id) {
    return httpRequest.send("Delete", "/r/subnets", id);
  }
}]);

manager.controller("ManageSubnetCtrl", ["$scope", "gateway", function ($scope, gateway) {

  gateway.getAllSubnets().then(function (data) {
    $scope.subnets = data;
  }, function () {
  });

  $scope.removeSubnet = function (id) {
    console.log(id);
    gateway.removeSubnet({"id":id}).then(function(){
    },function(){

    });
  }
}]);