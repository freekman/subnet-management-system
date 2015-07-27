angular.module("subnet-manager", ['http', 'networkInterfaces'])

        .controller("SubnetManagerCtrl", ["$scope", "$location", "subnetGateway", function ($scope, $location, subnetGateway) {
          var subnetID = $location.search().id;

          subnetGateway.getById(subnetID).then(function (data) {
            $scope.subnet = data;
          });

          $scope.resize = function (newSlash) {
            subnetGateway.resize(subnetID, {"value": newSlash}).then(function (data) {
              $scope.subnet.slash = data.value;
            })
          }
        }]);