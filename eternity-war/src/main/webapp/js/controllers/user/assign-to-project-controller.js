angular.module('Eternity').controller('AssignToProjectCtrl',
    ['$scope', '$element', 'BookingService', 'UserService', 'close', 'title',
        function ($scope, $element, BookingService, UserService, close, title) {

            $scope.alerts = [];
            $scope.title = title;

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

            var createErrorAlert = function (error) {

                if (error.status === 500) {
                    $scope.addAlert('danger', error.statusText);
                } else {
                    $scope.addAlert('danger', error.statusText + "\n" + error.data.error);
                }

            };

            $scope.cancel = function () {

                //  Manually hide the modal.
                $element.modal('hide');

                //  Now call close, returning control to the caller.
                close(undefined, 500);

            };

            $scope.createUpdateBooking = function () {

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
                    id: $scope.bookingId,
                    startTime: sTime.getTime(),
                    endTime: eTime.getTime(),
                    breakDuration: $scope.breakDuration,
                    description: $scope.description,
                    projectIdentifier: $scope.selectedProject
                };

                if (booking.id) {
                    BookingService.update(booking, function (success) {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Buchung erfolgreich aktualisiert'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                } else {
                    BookingService.save(booking, function (success) {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Buchung erfolgreich angelegt'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                }

            };

        }]);
