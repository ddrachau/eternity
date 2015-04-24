(function () {

    'use strict';

    angular.module('Eternity').factory('ProjectService', function ($resource) {

        return $resource('rest/project/', [],
            {
                find: {method: 'GET', isArray: true}
            });

    });

})();