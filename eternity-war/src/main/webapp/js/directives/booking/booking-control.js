angular.module('Eternity').directive('bookingControl', function () {

    return {
        restrict: 'E',
        scope: {
            projects: "="
        },
        controller: 'BookingControlCtrl',
        controllerAs: 'bcCtrl',
        templateUrl: 'templates/booking/booking-control.html'
    };

});
