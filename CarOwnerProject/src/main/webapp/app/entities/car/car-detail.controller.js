(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .controller('CarDetailController', CarDetailController);

    CarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Car', 'Owner'];

    function CarDetailController($scope, $rootScope, $stateParams, entity, Car, Owner) {
        var vm = this;
        vm.car = entity;
        vm.load = function (id) {
            Car.get({id: id}, function(result) {
                vm.car = result;
            });
        };
        var unsubscribe = $rootScope.$on('carOwnerProjectApp:carUpdate', function(event, result) {
            vm.car = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
