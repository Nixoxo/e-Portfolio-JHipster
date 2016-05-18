(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .controller('OwnerDialogController', OwnerDialogController);

    OwnerDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Owner', 'Car'];

    function OwnerDialogController ($scope, $stateParams, $uibModalInstance, entity, Owner, Car) {
        var vm = this;
        vm.owner = entity;
        vm.cars = Car.query();
        vm.load = function(id) {
            Owner.get({id : id}, function(result) {
                vm.owner = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('carOwnerProjectApp:ownerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.owner.id !== null) {
                Owner.update(vm.owner, onSaveSuccess, onSaveError);
            } else {
                Owner.save(vm.owner, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
