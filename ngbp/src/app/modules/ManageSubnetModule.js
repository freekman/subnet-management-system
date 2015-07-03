/**
 * Created by clouway on 15-6-29.
 */
var manager = angular.module("manageSubnetModule", ["httpModule"]);

manager.service("gateway", ["httpRequest", function (httpRequest) {

  this.removeSubnet = function (id) {
    return httpRequest.send("Delete", "/r/subnets", id);
  }
}]);

manager.controller("ManageSubnetCtrl", ["$scope", "gateway", function ($scope, gateway) {
  
  $scope.removeSubnet = function (id) {
    console.log(id);
    gateway.removeSubnet({"id":id}).then(function(){
    },function(){

    });
  }
}]);