/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('app', ['ui.router', 'http', 'subnet-manager', 'networkInterfaces', 'header', 'notificationModule', 'ui.bootstrap'])

        .run(function (editableOptions) {
          editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
        })
        .config(['$stateProvider', '$urlRouterProvider',
          function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
                    .state("network", {
                      url: '/',
                      templateUrl: 'partials/subnets.html',
                      controller: 'NetworkCtrl'
                    })
                    .state("subnetManager", {
                      url: '/subnet/manager',
                      templateUrl: 'partials/subnetManager.html',
                      controller: 'SubnetManagerCtrl'
                    })
          }]);