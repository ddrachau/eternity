(function () {

    'use strict';

    angular.module('Eternity').factory('UserService', function ($resource) {

        return $resource('rest/users/', [],
            {
                find: {method: 'GET', isArray: true},
                getBySession: {method: 'GET', url: 'rest/users/session'},
                getBookingsForCurrentUser: {method: 'GET', url: 'rest/users/bookings', isArray: true},
                getProjectsForCurrentUser: {method: 'GET', url: 'rest/users/projects', isArray: true}
            });

    });

})();