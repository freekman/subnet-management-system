/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
describe('treeSubnet', function () {


  describe('TreeCtrl', function () {
    var categoryGateway, scope, defer;

    beforeEach(module('treeSubnet'));

    beforeEach(inject(function ($controller, $rootScope, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();

      categoryGateway = {
        fetchAll: jasmine.createSpy().andReturn(defer.promise)
      };

      $controller('TreeCtrl', {$scope: scope, categoryGateway: categoryGateway})

    }));

    it('should not contain any predefined values',function(){
      expect(scope.networkModel).toEqual([]);
    });

    it('should fetch all categories', function () {
      expect(categoryGateway.fetchAll).toHaveBeenCalled();

      var category1 = {id: '123abc', type: 'category1'};

      defer.resolve([category1]);
      scope.$digest();

      var expected = [newCategoryNode(category1)];

      expect(scope.networkModel).toEqual(expected);

    });

    it('should edit description of node', function () {
      var label = 'testLabel', description = 'testDescription', dash = ' - ';
      var node = {label: label, description: description};

      scope.networkTree = {currentNode: node};

      scope.editDescription(description);

      expect(node.label).toEqual(label + dash + description)
    })

  });

  function newCategoryNode(category) {
    return {type: 'category', label: category.type, id: category.id, description: '', children: []}
  }

});