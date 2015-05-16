(function () {

    'use strict';

    angular.module('Eternity').factory('UserService', function ($resource) {

        return $resource('rest/users/', [],
            {
                find: {method: 'GET'},
                getBySession: {method: 'GET', url: 'rest/users/session'}
            });

    });

})();