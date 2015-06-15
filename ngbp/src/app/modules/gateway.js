var gateway = angular.module('gateway', ['httpModule', 'message']);

gateway.service('categoryGateway', ['httpRequest', function (httpRequest) {

  return {

    fetchAll: function () {
      return httpRequest.send('GET', '/r/category');
    },

    register: function (name) {
      return httpRequest.send('POST', '/r/category', {type: name});
    },

    delete: function (id) {
      return httpRequest.send('DELETE', '/r/category/' + id);
    }
  }

}]);
