angular.module('Eternity').controller('BookingCtrl', function ($scope, BookingService, UserService, ServerPushService, bookings, projects) {

    var ctrl = this;

    ctrl.bookings = bookings;
    ctrl.projects = projects;

    ServerPushService.on('project', $scope, function (data) {

        console.log('Received "project" event: ' + data);

    });
    ServerPushService.on('booking', $scope, function (data) {

        console.log('Received "booking" event: ' + data);
        ctrl.bookings = UserService.getBookingsForCurrentUser();

    });

});
