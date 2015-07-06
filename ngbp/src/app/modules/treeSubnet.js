/**
 * @author Marian Zlatev <mzlatev91@gmail.com>
 *
 */
angular.module('treeSubnet', ['angularTreeview', 'gateway']).controller('TreeCtrl', ['$scope', 'categoryGateway', function ($scope, categoryGateway) {

  $scope.networkModel = [];

  categoryGateway.fetchAll().then(
          function (categories) {

            categories.forEach(function (category) {

              $scope.networkModel.push(newCategoryNode(category));
            });
          });

  $scope.editDescription = function (description) {
    var label = $scope.networkTree.currentNode.label;

    var dash = " - ";

    var labels = label.split(dash);

    label = labels[0] + dash + description;

    $scope.networkTree.currentNode.label = label;

    $scope.networkTree.currentNode.description = description;
  };

}]);
///home/clouway/workspaces/idea/subnet-management-system/ngbp/node_modules/karma/static/karma.js
function newCategoryNode(category) {
  return {type: 'category', label: category.type, id: category.id, description: '', children: []}
}
