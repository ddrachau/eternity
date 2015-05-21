angular.module('Eternity').directive('bookingListing', function () {

    return {
        restrict: 'E',
        scope: true,
        controller: 'BookingListingCtrl',
        controllerAs: 'blCtrl',
        templateUrl: 'templates/booking/booking-listing.html'
    };

});
