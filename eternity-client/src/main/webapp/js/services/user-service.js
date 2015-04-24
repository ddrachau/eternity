(function () {

    'use strict';

    angular.module('Eternity').factory('UserService', function ($resource) {

        return $resource('rest/user/', [],
            {
                find: {method: 'GET', isArray: true}
            });

    });

})();