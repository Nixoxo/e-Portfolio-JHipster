'use strict';

describe('Controller Tests', function() {

    describe('Car Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCar, MockOwner;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCar = jasmine.createSpy('MockCar');
            MockOwner = jasmine.createSpy('MockOwner');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Car': MockCar,
                'Owner': MockOwner
            };
            createController = function() {
                $injector.get('$controller')("CarDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'carOwnerProjectApp:carUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
