(function () {

    'use strict';

    angular.module('Eternity').factory('BookingService', function ($resource) {

        return $resource('rest/bookings/', [],
            {
                delete: {method: 'DELETE', url: 'rest/bookings/:id'},
                update: {method: 'PUT', url: 'rest/bookings/:id'},
                getBookingsForCurrentUser: {method: 'GET', url: 'rest/users/bookings'}
            });

    });

})();