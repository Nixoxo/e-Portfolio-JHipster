(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .controller('OwnerDetailController', OwnerDetailController);

    OwnerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Owner', 'Car'];

    function OwnerDetailController($scope, $rootScope, $stateParams, entity, Owner, Car) {
        var vm = this;
        vm.owner = entity;
        vm.load = function (id) {
            Owner.get({id: id}, function(result) {
                vm.owner = result;
            });
        };
        var unsubscribe = $rootScope.$on('carOwnerProjectApp:ownerUpdate', function(event, result) {
            vm.owner = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
