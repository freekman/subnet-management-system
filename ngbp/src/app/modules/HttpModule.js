/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var httpModule = angular.module('httpModule', []);

httpModule.service('httpRequest', ['$http', '$q', function ($http, $q) {
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
}]);