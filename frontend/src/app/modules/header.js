/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
angular.module('header', []).directive('netsHeader', ['$compile', function ($compile) {

  return {
    restrict: 'E',

    scope: {activeUrl: '@'},

    link: function (scope, element, attrs) {

      var template = header();

      element.html('').append($compile(template)(scope));

      function header() {
        return "<div class='navbar navbar-inverse'>" +
                "<div class='container'>" +

                rightNav() +
                mainNav() +

                "</div>" +
                "</div>";
      }

      function rightNav() {
        return "<div class='nav navbar-right'>"

                + "<a href='/#' class='navbar-brand'></a>" +

                "</div>"
      }

      function mainNav() {
        return "<div id='navbar'' class='collapse navbar-collapse'>" +
                "<ul class='nav navbar-nav'>" +

                mainLabel('Subnets') +

                "</ul>" +
                "</div>"
      }

      function mainLabel(name) {
        var activeUrl = attrs.activeUrl;

        if (name == activeUrl) {
          return "<li class='active'><a href='/#" + name.toLowerCase() + "'>" + name + "</a></li>"
        }

        return "<li><a href='/#" + name.toLowerCase() + "'>" + name + "</a></li>"
      }

    }

  }

}]);