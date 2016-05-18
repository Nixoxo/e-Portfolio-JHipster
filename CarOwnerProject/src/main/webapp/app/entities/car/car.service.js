(function() {
    'use strict';
    angular
        .module('carOwnerProjectApp')
        .factory('Car', Car);

    Car.$inject = ['$resource', 'DateUtils'];

    function Car ($resource, DateUtils) {
        var resourceUrl =  'api/cars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateOfProduction = DateUtils.convertLocalDateFromServer(data.dateOfProduction);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateOfProduction = DateUtils.convertLocalDateToServer(data.dateOfProduction);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateOfProduction = DateUtils.convertLocalDateToServer(data.dateOfProduction);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
