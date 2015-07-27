angular.module("subnet-manager", ['http', 'networkInterfaces'])

        .controller("SubnetManagerCtrl", ["$scope", "$location", "$state", "subnetGateway", function ($scope, $location, $state, subnetGateway) {
          var subnetID = $location.search().id;

          subnetGateway.getById(subnetID).then(function (data) {
            $scope.subnet = data;
          });

          $scope.remove = function () {
            subnetGateway.remove(subnetID).then(function () {
              $state.go('network');
            });
          }

        }]);