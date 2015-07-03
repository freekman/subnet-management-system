/**
 * Created by clouway on 15-7-10.
 */
describe("notificationModule", function () {

  beforeEach(module("notificationModule"));

  describe("notification", function () {
    var note, scope, timeout, rootScope;

    beforeEach(inject(function ($rootScope, $timeout, notification) {
      rootScope = $rootScope;
      spyOn(rootScope, "$emit");
      note = notification;
      timeout = $timeout;
      scope = $rootScope.$new();
    }));

    it("should add message", function () {
      note.addMessage("Some message");
      scope.$digest();

      expect(rootScope.$emit).toHaveBeenCalledWith('notification:msg', 'Some message');
    });

    it("should remove message", function () {
      note.addMessage("Some message");
      scope.$digest();

      expect(rootScope.$emit).toHaveBeenCalledWith('notification:msg', 'Some message');

      timeout.flush();

      expect(rootScope.$emit).toHaveBeenCalledWith('notification:msg',undefined);
    });

  });
});