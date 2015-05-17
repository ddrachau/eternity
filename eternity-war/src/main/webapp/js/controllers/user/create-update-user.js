angular.module('Eternity').controller('CreateUpdateUserCtrl',
    ['$scope', '$element', 'UserService', 'close', 'user', 'title',
        function ($scope, $element, UserService, close, user, title) {

            $scope.alerts = [];
            $scope.title = title;
            $scope.user = user;

            $scope.userId = user.id;
            $scope.identifier = user.identifier || '';
            $scope.forename = user.forename || '';
            $scope.surname = user.surname || '';
            $scope.role = user.role || 'USER';

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

            $scope.createUpdateUser = function () {

                var user = {
                    id: $scope.userId,
                    identifier: $scope.identifier,
                    forename: $scope.forename,
                    surname: $scope.surname,
                    role: $scope.role
                };

                if (user.id) {
                    UserService.update(user, function () {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Benutzer erfolgreich aktualisiert'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                } else {
                    UserService.save(user, function () {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Benutzer erfolgreich angelegt'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                }

            };

        }]);
