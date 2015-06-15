/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var app = angular.module('app', ['ui.router', 'treeSubnet', 'category', 'header']);

app.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
            .state(
            '/tree', {
              url: '/pools',
              templateUrl: 'partials/pools.html',
              controller: 'TreeCtrl'
            })
            .state('/category', {
              url: '/category',
              templateUrl: 'partials/category.html',
              controller: 'CategoryCtrl'
            });
  }]);