angular.module('Eternity').controller('ChangePasswordCtrl',
    ['$scope', '$element', 'BookingService', 'UserService', 'close', 'title',
        function ($scope, $element, BookingService, UserService, close, title) {

            $scope.alerts = [];
            $scope.title = title;
            $scope.oldPassword = '';
            $scope.newPassword1 = '';
            $scope.newPassword2 = '';

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

            $scope.changePassword = function () {

                var changePassword = {
                    oldPassword: $scope.oldPassword,
                    newPassword: $scope.newPassword1
                };

                UserService.changePassword(changePassword, function () {

                    $element.modal('hide');

                    close({type: 'success', msg: 'Passwort erfolgreich aktualisiert'}, 500);

                }, function (error) {

                    createErrorAlert(error);

                });

            };

        }]);
