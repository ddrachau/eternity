angular.module('Eternity').directive('allBookingListing', function () {

    return {
        restrict: 'E',
        scope: true,
        controller: 'AllBookingListingCtrl',
        controllerAs: 'ablCtrl',
        templateUrl: 'templates/booking/all-booking-listing.html'
    };

});
