/**
 * Created by clouway on 15-6-15.
 */
describe("networkInterfaces", function () {

  beforeEach(module("networkInterfaces"));

  describe("subnetGateway", function () {
    var scope, httpRequest, gateway;

    beforeEach(function () {
      httpRequest = {send: jasmine.createSpy().andReturn({promise: "DummyPromise"})};

      module(function ($provide) {
        $provide.value("httpRequest", httpRequest);
      });

      inject(function ($rootScope, subnetGateway) {
        gateway = subnetGateway;
        scope = $rootScope.$new();
      });

    });

    it("should register subnet", function () {
      var subnet = {"promise": "dummy"};
      var promise = gateway.register(subnet);

      expect(httpRequest.send).toHaveBeenCalledWith("POST", "/r/subnets", subnet);
      expect(promise).toEqual({promise: "DummyPromise"});
    });

  });

  describe('networkGateway', function () {
    var nodeGateway, httpRequest, dummyPromise = {promise: 'dummy promise'};

    beforeEach(function () {

      httpRequest = {send: jasmine.createSpy().andReturn(dummyPromise)};

      module(function ($provide) {
        $provide.value('httpRequest', httpRequest);
      });

      inject(function ($injector) {
        nodeGateway = $injector.get('nodeGateway');
      });
    });

    it('should register network node', function () {
      var node = {name: 'Internet', parentId: "id1"};
      var result = nodeGateway.register(node);

      expect(httpRequest.send).toHaveBeenCalledWith('POST', '/r/nodes', node);
      expect(result).toEqual(dummyPromise);
    });

    it('should fetch network nodes by parent id', function () {
      var result = nodeGateway.fetchAllByParent("id1");

      expect(httpRequest.send).toHaveBeenCalledWith('GET', '/r/nodes/id1');
      expect(result).toEqual(dummyPromise);
    });

    it('should delete network node', function () {
      var dummyId = '123abc';
      var result = nodeGateway.delete(dummyId);

      expect(httpRequest.send).toHaveBeenCalledWith('DELETE', '/r/nodes/' + dummyId);
      expect(result).toEqual(dummyPromise);
    })

  });

  describe("NetworkCtrl", function () {
    var scope, modal, nodeGateway, subGateway, defer, modalDefer, networkDeffer;

    beforeEach(inject(function ($rootScope, $controller, $q) {
      scope = $rootScope.$new();
      defer = $q.defer();
      modalDefer = $q.defer();
      networkDeffer = $q.defer();

      modal = {open: jasmine.createSpy().andReturn({result: modalDefer.promise})};

      subGateway = {register: jasmine.createSpy().andReturn(defer.promise)};

      nodeGateway = {
        fetchAllByParent: jasmine.createSpy().andReturn(networkDeffer.promise),
        register: jasmine.createSpy().andReturn(defer.promise),
        delete: jasmine.createSpy().andReturn(defer.promise)
      };

      $controller("NetworkCtrl", {
        $scope: scope,
        $modal: modal,
        subnetGateway: subGateway,
        nodeGateway: nodeGateway
      });
    }));

    it("should open subnet modal", function () {
      scope.subnet = {};
      scope.selectedBranch = {data: {id: '123'}};
      scope.add();

      expect(modal.open).toHaveBeenCalled();
    });

    it('should init networkNodes', function () {

      expect(scope.networkNodes).toEqual([]);
    });

    it("should fetch root nodes", function () {
      expect(nodeGateway.fetchAllByParent).toHaveBeenCalledWith("root");

      var nodes = ['node1', 'node2'];

      networkDeffer.resolve(nodes);
      scope.$digest();

      expect(scope.networkNodes).toEqual(nodes);
    });

    it('should open node modal', function () {
      scope.selectedBranch = {children: [], noLeaf: false, data: {id: 'selectedId'}};
      scope.registerNode();
      expect(modal.open).toHaveBeenCalled();
    });

    it('should fetch network nodes on expand', function () {
      var branch = {children: [], data: {id: 'id1'}};
      var child = 'child node';

      scope.onNodeExpand(branch);
      expect(nodeGateway.fetchAllByParent).toHaveBeenCalledWith(branch.data.id);

      networkDeffer.resolve(child);
      scope.$digest();

      expect(branch).toEqual({children: 'child node', data: {id: 'id1'}});
    });

    it("should delete selected node", function () {
      scope.selectedBranch = {data: {id: 'selectedId'}};
      scope.selectedParent = {children: [{data: {id: 'selectedId'}}]};

      scope.deleteNode('selectedId');

      expect(nodeGateway.delete).toHaveBeenCalledWith('selectedId');

      defer.resolve();
      scope.$digest();

      var expected = {children: []};
      expect(scope.selectedBranch).not.toBeDefined();
      expect(scope.selectedParent).toEqual(expected);
    });

    it('should delete root branch', function () {
      scope.networkNodes = [{data: {id: 'id1', parentId: 'root'}}];
      scope.selectedBranch = {data: {id: 'id1', parentId: 'root'}};
      scope.selectedParent = undefined;

      scope.deleteNode('id1');

      expect(nodeGateway.delete).toHaveBeenCalledWith('id1');

      defer.resolve();
      scope.$digest();

      expect(scope.networkNodes).toEqual([]);
      expect(scope.selectedBranch).not.toBeDefined();
      expect(scope.selectedParent).not.toBeDefined();
    });

    it('should bind selected branch and parent branch', function () {
      var childBranch = 'child';
      var parentBranch = 'parent';

      scope.onNodeSelect(childBranch, parentBranch);

      expect(scope.selectedBranch).toEqual('child');
      expect(scope.selectedParent).toEqual('parent');
    });

  });

  describe("NewSubnetCtrl", function () {
    var scope, deffer, gateway, modalInst;

    beforeEach(inject(function ($rootScope, $controller, $q) {
      deffer = $q.defer();
      scope = $rootScope.$new();

      modalInst = {
        dismiss: jasmine.createSpy()
      };

      gateway = {
        register: jasmine.createSpy().andReturn(deffer.promise)
      };

      $controller("NewSubnetCtrl", {$scope: scope, $modalInstance: modalInst, subnetGateway: gateway, nodeId: 'fake'});
    }));

    it("should register subnet", function () {
      var subnet = {nodeId: "fakeNodeId"};
      scope.register(subnet);

      expect(gateway.register).toHaveBeenCalledWith(subnet);

      deffer.resolve("Success");
      scope.$digest();

      expect(scope.responce).toEqual("Success");
    });

    it("should dismiss modalInstance", function () {
      scope.cancel();

      expect(modalInst.dismiss).toHaveBeenCalledWith("cancel");
    });

  });

  describe('NewNodeCtrl', function () {
    var registerNodeCtrl, scope, modalInstance, nodeGateway, defer;

    beforeEach(inject(function ($controller, $rootScope, $q) {
      defer = $q.defer();
      scope = $rootScope.$new();
      modalInstance = {close: jasmine.createSpy(), dismiss: jasmine.createSpy()};
      nodeGateway = {register: jasmine.createSpy().andReturn(defer.promise)};

      registerNodeCtrl = $controller('NewNodeCtrl', {
        $scope: scope,
        $modalInstance: modalInstance,
        nodeGateway: nodeGateway,
        tree: {root: 'dummyRoot', branch: 'dummyBranch'}
      });
    }));

    it('should init scope data', function () {
      expect(scope.selectedBranch).toEqual('dummyBranch');
      expect(scope.root).toEqual('dummyRoot');
      expect(scope.selectedType).toEqual('child');
      expect(scope.nodeTypes).toEqual(['child', 'root']);
    });

    it('should dismiss modal', function () {
      scope.cancel();
      expect(modalInstance.dismiss).toHaveBeenCalled();
    });

    it('should register root node', function () {
      scope.selectedType = 'root';
      scope.root = [];
      scope.submit('NewNode');
      expect(nodeGateway.register).toHaveBeenCalledWith({name: 'NewNode', parentId: 'root'});

      defer.resolve('id2');
      scope.$digest();

      var expected = [{label: 'NewNode', children: [], data: {id: 'id2', parentId: 'root'}}];

      expect(scope.root).toEqual(expected);
      expect(modalInstance.close).toHaveBeenCalled();
    });

    it('should register child node', function () {
      scope.selectedType = 'child';
      scope.selectedBranch = {children: [], data: {id: 'selectedId'}};
      scope.submit('NewNode');

      var node = {name: 'NewNode', parentId: 'selectedId'};
      expect(nodeGateway.register).toHaveBeenCalledWith(node);

      defer.resolve('id2');
      scope.$digest();

      var newNode = {label: 'NewNode', children: [], data: {id: 'id2', parentId: 'selectedId'}};
      var expected = {children: [newNode], noLeaf: true, data: {id: 'selectedId'}};

      expect(scope.selectedBranch).toEqual(expected);
      expect(modalInstance.close).toHaveBeenCalled();
    });

  });

});
