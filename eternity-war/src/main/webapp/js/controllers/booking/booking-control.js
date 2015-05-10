angular.module('Eternity').controller('BookingControlCtrl', function ($scope, BookingService, UserService) {

    $scope.breakDuration = 0;
    $scope.description = '';
    $scope.selectedProject = $scope.projects && $scope.projects.length > 0 ? $scope.projects[0].identifier : undefined;
    $scope.bookingSuccess = false;

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
    $scope.alerts = [];

    $scope.addAlert = function (type, msg) {
        $scope.clearAlerts();
        $scope.alerts.push({type: type, msg: msg});
    };

    $scope.clearAlerts = function () {
        $scope.alerts.length = 0;
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.createBooking = function () {

        if (!$scope.bookingForm.$valid) {
            $scope.validationFailed = true;
        } else {
            $scope.validationFailed = false;
        }

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

            $scope.addAlert('success', 'Buchung erfolgreich angelegt');

        }, function (error) {

            if (error.status === 417 || error.status === 412) {
                $scope.addAlert('danger', error.data.error);
            } else {
                $scope.addAlert('danger', error.statusText);
            }

        });

    };

});
