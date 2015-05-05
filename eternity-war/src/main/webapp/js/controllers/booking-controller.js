angular.module('Eternity').controller('BookingCtrl', function ($scope, BookingService, UserService, bookings, projects) {

    var ctrl = this;

    ctrl.bookings = bookings;
    ctrl.projects = projects;
    ctrl.breakDuration = 0;
    ctrl.description = '';

    //if (projects.length > 0) {
    //    ctrl.selectedProject = projects[0].identifier;
    //}

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

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    ctrl.createBooking = function () {

        var sTime = new Date(0);
        sTime.setUTCFullYear($scope.bookingDate.getUTCFullYear());
        sTime.setUTCMonth($scope.bookingDate.getUTCMonth());
        sTime.setUTCDate($scope.bookingDate.getUTCDate());
        sTime.setUTCHours($scope.startTime.getUTCHours());
        sTime.setUTCMinutes($scope.startTime.getUTCMinutes());

        var eTime = new Date(0);
        eTime.setUTCFullYear($scope.bookingDate.getUTCFullYear());
        eTime.setUTCMonth($scope.bookingDate.getUTCMonth());
        eTime.setUTCDate($scope.bookingDate.getUTCDate());
        eTime.setUTCHours($scope.endTime.getUTCHours());
        eTime.setUTCMinutes($scope.endTime.getUTCMinutes());

        var booking = {
            startTime: sTime.getTime(),
            endTime: eTime.getTime(),
            breakDuration: this.breakDuration,
            description: this.description,
            projectIdentifier: this.selectedProject
        };

        console.log(booking);

        BookingService.save(booking, function (success) {

            $scope.bookingError = false;
            $scope.projects = UserService.getProjectsForCurrentUser();

            return success;

        }, function (error, $q) {

            $scope.bookingError = true;
            $scope.error = error.statusText;

            return $q.reject(error);

        });

    };

});
