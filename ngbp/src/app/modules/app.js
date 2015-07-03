/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var app = angular.module('app', ['ui.router', 'httpModule', 'registerModule', 'treeSubnet', 'category', 'header']);

app.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
            .state("register", {
              url: '/',
              templateUrl: 'partials/subnets.html',
              controller: 'RegisterCtrl'
            })
            .state('/tree', {
              url: '/pools',
              templateUrl: 'partials/pools.html',
              controller: 'TreeCtrl'
            })
            .state('/category', {
              url: '/category',
              templateUrl: 'partials/category.html',
              controller: 'CategoryCtrl'
            })
            .state('/category/new', {
              url: '/category/new',
              templateUrl: 'partials/new-category.html',
              controller: 'CategoryRegistryCtrl'
            });
  }]);