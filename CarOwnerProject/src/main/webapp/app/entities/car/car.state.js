(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car', {
            parent: 'entity',
            url: '/car?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cars'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car/cars.html',
                    controller: 'CarController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('car-detail', {
            parent: 'entity',
            url: '/car/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Car'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car/car-detail.html',
                    controller: 'CarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Car', function($stateParams, Car) {
                    return Car.get({id : $stateParams.id});
                }]
            }
        })
        .state('car.new', {
            parent: 'car',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-dialog.html',
                    controller: 'CarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                model: null,
                                company: null,
                                dateOfProduction: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('car', null, { reload: true });
                }, function() {
                    $state.go('car');
                });
            }]
        })
        .state('car.edit', {
            parent: 'car',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-dialog.html',
                    controller: 'CarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Car', function(Car) {
                            return Car.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('car', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car.delete', {
            parent: 'car',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car/car-delete-dialog.html',
                    controller: 'CarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Car', function(Car) {
                            return Car.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('car', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
