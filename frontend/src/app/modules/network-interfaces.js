/**
 * Created by clouway on 15-6-15.
 */
angular.module("networkInterfaces", ['http', 'tree', 'xeditable', 'ui.bootstrap'])

        .factory('nodeGateway', ['httpRequest', function (httpRequest) {
          return {
            fetchAllByParent: function (id) {
              return httpRequest.send('GET', '/r/nodes/' + id);
            },

            register: function (node) {
              return httpRequest.send('POST', '/r/nodes', node);
            },

            delete: function (id) {
              return httpRequest.send('DELETE', '/r/nodes/' + id);
            }
          }
        }])

        .factory("subnetGateway", ["httpRequest", function (httpRequest) {
          return {
            register: function (subnet) {
              return httpRequest.send("POST", "/r/subnets", subnet);
            },
            getById: function (id) {
              return httpRequest.send("GET", "/r/subnets/" + id);
            },
            resize: function (id, slash) {
              return httpRequest.send("PUT","/r/subnets/"+id+"/resize",slash);
            }
          }
        }])

        .controller("NetworkCtrl", ["$scope", "$modal", "$location", "subnetGateway", "nodeGateway", function ($scope, $modal, $location, subnetGateway, nodeGateway) {

          $scope.networkNodes = [];

          $scope.navigateToSubnetManager = function (id) {
            $location.path('/subnet/manager').search({"id": id});
          };

          nodeGateway.fetchAllByParent("root").then(
                  function (data) {
                    $scope.networkNodes = data;
                  });

          $scope.add = function () {
            $modal.open({
              templateUrl: 'partials/subnetModal.html',
              controller: 'NewSubnetCtrl',
              backdrop: false,
              resolve: {
                nodeId: function () {
                  return $scope.selectedBranch.data.id;
                }
              }
            });
          };

          $scope.registerNode = function () {
            var modal = $modal.open({
              animation: true,
              templateUrl: 'partials/new-node.html',
              controller: 'NewNodeCtrl',
              backdrop: false,
              resolve: {
                tree: function () {
                  return {root: $scope.networkNodes, branch: $scope.selectedBranch};
                }
              }
            });
          };

          $scope.onNodeSelect = function (branch, parent) {
            $scope.selectedBranch = branch;
            $scope.selectedParent = parent;
          };

          $scope.onNodeExpand = function (branch) {
            var id = branch.data.id;
            nodeGateway.fetchAllByParent(id).then(function (data) {
              branch.children = data;
            });
          };

          $scope.deleteNode = function () {
            var branch = $scope.selectedBranch;

            nodeGateway.delete(branch.data.id).then(function () {
              if ($scope.selectedParent === undefined) {

                var index = $scope.networkNodes.indexOf(branch);
                $scope.networkNodes.splice(index, 1)
              } else {

                var index = $scope.selectedParent.children.indexOf(branch);
                $scope.selectedParent.children.splice(index, 1);
              }

              $scope.selectedBranch = undefined;
            });
          };
        }])

        .controller('NewSubnetCtrl', ['$scope', '$modalInstance', 'subnetGateway', 'nodeId', function ($scope, $modalInstance, subnetGateway, nodeId) {

          $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
          };

          $scope.register = function (subnet) {
            subnet.nodeId = nodeId;

            subnetGateway.register(subnet).then(function (data) {
              $scope.responce = data;
            });
          }
        }])

        .controller("NewNodeCtrl", ["$scope", "$modalInstance", "nodeGateway", 'tree', function ($scope, $modalInstance, nodeGateway, tree) {

          $scope.selectedBranch = tree.branch;
          $scope.root = tree.root;
          $scope.selectedType = 'root';
          $scope.nodeTypes = ['child', 'root'];

          $scope.submit = function (name) {
            var isRootType = $scope.selectedType === 'root';
            var parentId = isRootType ? 'root' : $scope.selectedBranch.data.id;
            var siblings = isRootType ? $scope.root : $scope.selectedBranch.children;

            var node = {name: name, parentId: parentId};

            nodeGateway.register(node).then(function (nodeId) {
              siblings.push(newNode(name, nodeId, parentId));

              if ($scope.selectedBranch) {
                $scope.selectedBranch.noLeaf = true;
              }

              $modalInstance.close();
            });
          };

          $scope.cancel = function () {
            $modalInstance.dismiss();
          };

        }]);

function newNode(name, id, parentId) {
  return {
    label: name, children: [],
    data: {id: id, parentId: parentId}
  }
}