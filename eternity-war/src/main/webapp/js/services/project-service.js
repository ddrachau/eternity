(function () {

    'use strict';

    angular.module('Eternity').factory('ProjectService', function ($resource) {

        return $resource('rest/projects/', [],
            {
                delete: {method: 'DELETE', url: 'rest/projects/:id'},
                update: {method: 'PUT', url: 'rest/projects/:id'},
                find: {method: 'GET'},
                getProjectsForCurrentUser: {method: 'GET', url: 'rest/users/projects', isArray: true}
            });

    });

})();