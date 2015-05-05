angular.module('Eternity').controller('BookingCtrl', function ($scope, BookingService, UserService, bookings, projects) {

    var ctrl = this;

    ctrl.bookings = bookings;
    ctrl.projects = projects;

});
