angular.module('Eternity').controller('BookingCtrl', function ($scope, BookingService, bookings, projects) {

    $scope.bookings = bookings;
    $scope.projects = projects;

    var startTime = new Date();
    startTime.setHours(9);
    startTime.setMinutes(0);

    var endTime = new Date();
    endTime.setHours(16);
    endTime.setMinutes(0);

    $scope.bookingDate = new Date();
    $scope.startTime = startTime;
    $scope.endTime = endTime;

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };
    $scope.dateFormat = 'dd.MM.yyyy';
    $scope.hstep = 1;
    $scope.mstep = 5;


    $scope.changed = function () {
    };

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.createBooking = function () {

        var sTime = new Date(0);
        sTime.setUTCFullYear(this.bookingDate.getUTCFullYear());
        sTime.setUTCMonth(this.bookingDate.getUTCMonth());
        sTime.setUTCDate(this.bookingDate.getUTCDate());
        sTime.setUTCHours(this.startTime.getUTCHours());
        sTime.setUTCMinutes(this.startTime.getUTCMinutes());

        console.log(sTime);

        var eTime = new Date(0);
        eTime.setUTCFullYear(this.bookingDate.getUTCFullYear());
        eTime.setUTCMonth(this.bookingDate.getUTCMonth());
        eTime.setUTCDate(this.bookingDate.getUTCDate());
        eTime.setUTCHours(this.endTime.getUTCHours());
        eTime.setUTCMinutes(this.endTime.getUTCMinutes());

        var booking = {
            startTime: sTime.getTime(),
            endTime: eTime.getTime(),
            breakDuration: 0,
            description: 'mapping pending',
            projectIdentifier: 'P00001'
        };

        console.log(booking);

        BookingService.save(booking);

    }

});
