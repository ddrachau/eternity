(function () {

    'use strict';

    angular.module('Eternity').factory('SessionService', function ($resource) {

        return $resource('rest/auth/', [],
            {
                login: {method: 'POST'},
                loginWithToken: {method: 'GET', url: 'rest/auth/token'}
            });

    });

})();