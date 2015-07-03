/**
 * Created by clouway on 15-6-15.
 */
angular.module("subnet", ['http'])

        .factory("subnetGateway", ["httpRequest", function (httpRequest) {
          return {
            register: function (subnet) {
              return httpRequest.send("POST", "/r/subnets", subnet);
            }
          }
        }])


        .controller("SubnetCtrl", ["$scope", "$modal", function ($scope, $modal) {
          $scope.add = function () {

              $modal.open({
                templateUrl: 'partials/subnetModal.html',
                controller: 'NewSubnetCtrl',
                backdrop:false,
                resolve: {
                  subnet: function () {
                    return $scope.subnet;
                  }
                }
              });
            }
        }])
        .controller('NewSubnetCtrl', ['$scope', '$modalInstance', 'subnetGateway', function ($scope, $modalInstance, subnetGateway) {

          $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
          };

          $scope.register = function (subnet) {
            //This will come from the tree node(After merge it will be removed).
            subnet.nodeId = "fakeNodeID";

            subnetGateway.register(subnet).then(function (data) {
              $scope.responce = data;
            });
          }
        }]);


