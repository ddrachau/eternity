(function () {

    'use strict';

    angular.module('Eternity').factory('SessionService', function ($resource) {

        return $resource('rest/auth/');

    });

})();