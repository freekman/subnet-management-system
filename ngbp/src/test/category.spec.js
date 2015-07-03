/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
xdescribe('category', function () {

  var categoryGateway, scope, defer, fetchDefer;

  beforeEach(module('category'));

  beforeEach(inject(function ($rootScope, $q) {
    scope = $rootScope.$new();
    defer = $q.defer();
    fetchDefer = $q.defer();

    categoryGateway = {
      fetchAll: jasmine.createSpy().andReturn(fetchDefer.promise),
      register: jasmine.createSpy().andReturn(defer.promise),
      delete: jasmine.createSpy().andReturn(defer.promise)
    };
  }));


  describe('CategoryCtrl', function () {

    beforeEach(inject(function ($controller, $rootScope, $q) {
      $controller('CategoryCtrl', {$scope: scope, categoryGateway: categoryGateway});
    }));

    it('should contain no predefined categories', function () {
      expect(scope.categories).toEqual([]);
    });

    it('should fetch all categories', function () {
      expect(categoryGateway.fetchAll).toHaveBeenCalled();

      var category = [{id: '123', type: 'test'}];
      fetchDefer.resolve(category);
      scope.$digest();

      expect(scope.categories).toEqual(category);
    });

    it('should delete category', function () {
      var television = {id: 'dummyId', type: 'Television'}, internet = {id: 'dummyId2', type: 'Internet'};

      scope.categories = [television, internet];

      scope.delete(television);
      expect(categoryGateway.delete).toHaveBeenCalledWith(television.id);

      defer.resolve('Deleted a category');
      scope.$digest();

      var expected = [internet];

      expect(scope.categories).toEqual(expected);
    });

  });


  describe('CategoryRegistryCtrl', function () {

    var categoryRegistryCtrl;

    beforeEach(inject(function ($controller) {

      categoryRegistryCtrl = {register: jasmine.createSpy().andReturn(defer.promise)};

      $controller('CategoryRegistryCtrl', {$scope: scope, categoryRegistryCtrl: categoryGateway});
    }));


    iit('should register new category', function () {
      scope.register('test');

      expect(categoryGateway.register).toHaveBeenCalledWith('test');

      defer.resolve('Registration successful');
      scope.$digest();

      expect(messenger.success).toHaveBeenCalledWith('Registration successful');
      expect(categoryGateway.fetchAll).toHaveBeenCalled();

      expect(scope.messages).toEqual(dummyMessage);
    });

    xit('should fail registering category', function () {
      scope.register('testy');

      expect(categoryGateway.register).toHaveBeenCalledWith('testy');

      defer.reject('Registration failed');
      scope.$digest();

      expect(messenger.fail).toHaveBeenCalledWith('Registration failed');

      expect(scope.categories).toEqual([]);
      expect(scope.messages).toEqual(dummyMessage);
    });

  });

});