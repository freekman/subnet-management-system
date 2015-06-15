/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var app = angular.module('app', ['ui.router','registerModule','httpModule']);

app.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
           .state("register",{
              url: '/',
              templateUrl: 'partials/subnets.html',
              controller: 'registerCtrl'
            });
  }]);