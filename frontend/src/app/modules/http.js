/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('http', [])

        .service('httpRequest', ['$http', '$q', function ($http, $q) {
          'use strict';
          this.send = function (method, url, data) {
            var deferred = $q.defer();
            $http({method: method, url: url, data: data})
                    .success(function (data) {
                      deferred.resolve(data);
                    })
                    .error(function (data) {
                      deferred.reject(data);
                    });
            return deferred.promise;
          };
        }])

        .factory("errorInterceptor", ["$q", "notification", function ($q, notification) {
          return {
            responseError: function (rejection) {
                notification.addMessage(rejection.data);

              return $q.reject(rejection);
            }
          }
        }]).config(['$httpProvider', function ($httpProvider) {
          $httpProvider.interceptors.push('errorInterceptor');
        }]);