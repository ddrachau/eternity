angular.module('Eternity').directive('bookingListing', function () {

    return {
        restrict: 'E',
        scope: {
            bookings: "=",
            projects: "="
        },
        controller: 'BookingListingCtrl',
        controllerAs: 'blCtrl',
        templateUrl: 'templates/booking/booking-listing.html'
    };

});
