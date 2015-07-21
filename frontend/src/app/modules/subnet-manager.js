angular.module("subnet-manager", ['http', 'networkInterfaces'])

        .controller("SubnetManagerCtrl", ["$scope", "$location", "subnetGateway", function ($scope, $location, subnetGateway) {
          var subnetID = $location.search().id;

          subnetGateway.getById(subnetID).then(function (data) {
            $scope.subnet = data;
          });

        }]);