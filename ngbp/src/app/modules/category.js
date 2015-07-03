/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('category', ['gateway'])

        .controller('CategoryCtrl', ['$scope', 'categoryGateway', function ($scope, categoryGateway) {

          //$scope.categories = [];
          //
          //categoryGateway.fetchAll().then(
          //        function (data) {
          //          $scope.categories = data;
          //        });
          //
          //$scope.delete = function (category) {
          //
          //  categoryGateway.delete(category.id).then(
          //          function () {
          //            var index = $scope.categories.indexOf(category);
          //            $scope.categories.splice(index, 1);
          //          })
          //};

        }])

        .controller('CategoryRegistryCtrl', ['$scope', 'categoryGateway','$state', function ($scope, categoryGateway,$state) {

          //$scope.register = function (category) {
          //
          //  categoryGateway.register(category).then(
          //          function () {
          //            $state.go("/category");
          //          },
          //          function (message) {
          //            $scope.message = message;
          //          })
          //};
        }]);
