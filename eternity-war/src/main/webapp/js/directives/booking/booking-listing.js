angular.module('Eternity').directive('bookingListing', function () {

    return {
        restrict: 'E',
        scope: {
            projects: "="
        },
        controller: 'BookingListingCtrl',
        controllerAs: 'blCtrl',
        templateUrl: 'templates/booking/booking-listing.html'
    };

});
