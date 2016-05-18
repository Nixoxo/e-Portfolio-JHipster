(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .controller('CarDialogController', CarDialogController);

    CarDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Car', 'Owner'];

    function CarDialogController ($scope, $stateParams, $uibModalInstance, entity, Car, Owner) {
        var vm = this;
        vm.car = entity;
        vm.owners = Owner.query();
        vm.load = function(id) {
            Car.get({id : id}, function(result) {
                vm.car = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('carOwnerProjectApp:carUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.car.id !== null) {
                Car.update(vm.car, onSaveSuccess, onSaveError);
            } else {
                Car.save(vm.car, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateOfProduction = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
