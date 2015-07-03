/**
 * Created by clouway on 15-7-9.
 */angular.module("notificationModule", [])

        .service("notification", ["$rootScope", "$timeout", function ($rootScope, $timeout) {
          var emit,rest;

          this.addMessage = function (msg) {
            $timeout.cancel(rest);
            emit = $rootScope.$emit("notification:msg", msg);

            rest=$timeout(function () {
              emit = $rootScope.$emit("notification:msg", undefined);
            }, 4000);
          };

        }])

        .directive('notificationBox', function () {

          var directive = {
            replace: true,
            template: '<div class=\"alert alert-danger notification-message\" role=\"alert\" ng-show=\"message.length \" ng-bind=\"message\">' +
            ' <span class=\"glyphicon glyphicon-fire\" aria-hidden="true"></span>' +
            '</div>'
          };

          directive.controller = ["$scope", function ($scope) {
            $scope.$on("notification:msg", function (event, data) {
              $scope.message = data;
            });

          }];

          return directive;
        });
