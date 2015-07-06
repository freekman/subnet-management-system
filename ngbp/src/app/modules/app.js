/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
var app = angular.module('app', ['ui.router', 'httpModule',"bindingModule",'subnetModule', 'treeSubnet', 'category', 'header',"xeditable"]);

app.run(function (editableOptions) {
  editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
});


app.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
            .state("subnet", {
              url: '/',
              templateUrl: 'partials/subnets.html',
              controller: 'SubnetCtrl'
            })
            .state("bindings", {
              url: '/bindings',
              templateUrl: 'partials/bindings.html',
              params: {"id": ""},
              controller: 'BindingCtrl'
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