(function () {

    'use strict';

    angular.module('Eternity').factory('BookingService', function ($resource) {

        return $resource('rest/bookings/');
        //, [],
        //    {
        //        find: {method: 'GET', isArray: true},
        //        getBookingsForCurrentUser: {method: 'GET', url: 'rest/users/bookings', isArray: true}
        //    });

    });

})();