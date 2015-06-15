/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('category', ['gateway'])
        .controller('CategoryCtrl', ['$scope', 'categoryGateway', 'messenger', function ($scope, categoryGateway, messenger) {

          $scope.categories = [];

          fetchAll();

          $scope.register = function (category) {

            categoryGateway.register(category).then(
                    function (message) {
                      $scope.messages = messenger.success(message);
                      fetchAll();
                    },
                    function (message) {
                      $scope.messages = messenger.fail(message);
                    })
          };

          $scope.delete = function (category) {

            categoryGateway.delete(category.id).then(
                    function (message) {
                      $scope.messages = messenger.success(message);

                      var index = $scope.categories.indexOf(category);
                      $scope.categories.splice(index, 1);
                    })
          };

          $scope.closeMessage = function (index) {
            $scope.messages.splice(index, 1);
          };

          function fetchAll() {
            categoryGateway.fetchAll().then(
                    function (data) {
                      $scope.categories = data;
                    });
          }

        }]);