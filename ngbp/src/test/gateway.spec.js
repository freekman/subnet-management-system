/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */

describe('Gateway services', function () {

  describe('categoryGateway', function () {

    var categoryGateway, httpRequest;
    var dummyPromise = {promise: 'dummy promise'};

    beforeEach(module('gateway'));

    beforeEach(function () {
      httpRequest = {send: jasmine.createSpy().andReturn(dummyPromise)};

      module(function ($provide) {
        $provide.value('httpRequest', httpRequest);
      });

      inject(function ($injector) {
        categoryGateway = $injector.get('categoryGateway');
      });

    });

    it('should register new category request', function () {
      var result = categoryGateway.register('Internet');

      expect(httpRequest.send).toHaveBeenCalledWith('POST', '/r/category', {type: 'Internet'});
      expect(result).toEqual(dummyPromise);
    });

    it('should fetch all categories request', function () {
      var result = categoryGateway.fetchAll();

      expect(httpRequest.send).toHaveBeenCalledWith('GET', '/r/category');
      expect(result).toEqual(dummyPromise);
    });

    it('should send delete category request', function () {
      var dummyId = '123abc';
      var result = categoryGateway.delete(dummyId);

      expect(httpRequest.send).toHaveBeenCalledWith('DELETE', '/r/category/' + dummyId);
      expect(result).toEqual(dummyPromise);
    })

  });

});