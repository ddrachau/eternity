angular.module('Eternity').controller('BookingControlCtrl', function ($scope, BookingService, UserService) {

    var ctrl = this;

    ctrl.projects = $scope.projects;
    $scope.breakDuration = 0;
    $scope.description = '';
    $scope.selectedProject = '';

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
            breakDuration: $scope.breakDuration,
            description: $scope.description,
            projectIdentifier: $scope.selectedProject
        };

        console.log(booking);

        BookingService.save(booking, function (success) {

            ctrl.bookingError = false;
            ctrl.projects = UserService.getProjectsForCurrentUser();

        }, function (error) {

            ctrl.bookingError = true;
            ctrl.error = error.statusText;

        });

    };

});
