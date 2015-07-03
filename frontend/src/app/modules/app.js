/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('app', ['ui.router', 'http', 'subnet', 'header', 'notificationModule', 'ui.bootstrap'])

        .config(['$stateProvider', '$urlRouterProvider',
          function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
                    .state("subnet", {
                      url: '/',
                      templateUrl: 'partials/subnets.html',
                      controller: 'SubnetCtrl'
                    })
          }]);