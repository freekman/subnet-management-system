/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */

angular.module('message', ['ui.bootstrap']).service('messenger', function () {

  return {
    messages: [],

    success: function (message) {
      this.messages = [];

      this.messages.push({type: 'success', msg: message});

      return this.messages;
    },

    fail: function (message) {
      this.messages = [];

      this.messages.push({type: 'danger', msg: message});

      return this.messages;
    },

    close: function (index) {
      if (index < this.messages.length) {
        this.messages.splice(index, 1);
      }

      return this.messages;
    }
  }

});