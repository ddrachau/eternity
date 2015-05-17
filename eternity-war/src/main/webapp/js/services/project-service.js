(function () {

    'use strict';

    angular.module('Eternity').factory('ProjectService', function ($resource) {

        return $resource('rest/projects/', [],
            {
                delete: {method: 'DELETE', url: 'rest/projects/:identifier'},
                update: {method: 'PUT', url: 'rest/projects'},
                find: {method: 'GET'},
                getProjectsForCurrentUser: {method: 'GET', url: 'rest/users/projects', isArray: true}
            });

    });

})();