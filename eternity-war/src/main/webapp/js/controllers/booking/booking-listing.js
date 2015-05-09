angular.module('Eternity').controller('BookingListingCtrl', function ($scope) {

    $scope.editBooking = function (booking) {

        console.info("Edit booking: " + booking.id);

    }

    $scope.removeBooking = function (booking) {

        console.info("Remove booking: " + booking.id);

    }

});
