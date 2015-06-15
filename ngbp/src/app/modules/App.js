/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var app = angular.module('Subnets', ['ui.router', 'tModule']);

app.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider.
            state(
            '/', {
              url: '/',
              templateUrl: 'partials/subnets.html',
              controller: 'aaa'
            });
  }]);