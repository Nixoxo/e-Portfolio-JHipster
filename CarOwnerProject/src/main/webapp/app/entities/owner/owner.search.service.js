(function() {
    'use strict';

    angular
        .module('carOwnerProjectApp')
        .factory('OwnerSearch', OwnerSearch);

    OwnerSearch.$inject = ['$resource'];

    function OwnerSearch($resource) {
        var resourceUrl =  'api/_search/owners/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
