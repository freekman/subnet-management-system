/**
 * @author Marian Zlatev (mzlatev91@gmail.com)
 */
describe('message', function () {

  describe('messenger', function () {
    var messenger;

    beforeEach(function () {

      module('message');

      inject(function ($injector) {

        messenger = $injector.get('messenger');
      });

    });

    it('should create warning message', function () {
      messenger.fail('Error msg');

      expect(messenger.messages).toEqual([{type: 'danger', msg: 'Error msg'}]);
    });

    it('should create successful message', function () {
      messenger.success('Happy days');

      expect(messenger.messages).toEqual([{type: 'success', msg: 'Happy days'}]);
    });

    it('should dismiss warning message', function () {
      messenger.success('Random msg');

      messenger.close(0);

      expect(messenger.messages.length).toEqual(0);
    })

  });

});