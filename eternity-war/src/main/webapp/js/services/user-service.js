(function () {

    'use strict';

    angular.module('Eternity').factory('UserService', function ($resource) {

        return $resource('rest/users/', [],
            {
                delete: {method: 'DELETE', url: 'rest/users/:identifier'},
                update: {method: 'PUT', url: 'rest/users'},
                find: {method: 'GET'},
                getBySession: {method: 'GET', url: 'rest/users/session'}
            });

    });

})();