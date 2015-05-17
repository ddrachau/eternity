angular.module('Eternity').controller('UserListingCtrl',
    function ($scope, TableService, ServerPushService, UserService, ModalService) {

        var ctrl = this;

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

        ServerPushService.on('user', $scope, function () {

            ctrl.callServer(ctrl.tableState);

        });

        this.displayed = [];

        this.callServer = function (tableState) {

            ctrl.tableState = tableState;
            ctrl.isLoading = true;

            var requestFilter = TableService.createRequestFilterFromTableState(tableState);

            UserService.find(requestFilter, function (result) {
                ctrl.displayed = result.data;
                tableState.pagination.numberOfPages = result.numberOfPages;
                ctrl.isLoading = false;
            });

        };

        this.createErrorAlert = function (error) {

            if (error.status === 500) {
                $scope.addAlert('danger', error.statusText);
            } else {
                $scope.addAlert('danger', error.statusText + "\n" + error.data.error);
            }

        };

        $scope.createBooking = function () {

            ModalService.showModal({
                templateUrl: "templates/booking/create-update-booking.html",
                controller: "CreateUpdateBookingCtrl",
                inputs: {
                    title: 'Neue Buchung anlegen',
                    projects: $scope.projects,
                    booking: {}
                }
            }).then(function (modal) {

                modal.element.modal({backdrop: 'static'});
                modal.close.then(function (result) {

                    if (result) {

                        $scope.addAlert(result.type, result.msg);

                    }

                });
            });

        }

        $scope.editBooking = function (booking) {

            ModalService.showModal({
                templateUrl: "templates/booking/create-update-booking.html",
                controller: "CreateUpdateBookingCtrl",
                inputs: {
                    title: 'Bestehende Buchung anpassen',
                    projects: $scope.projects,
                    booking: booking
                }
            }).then(function (modal) {

                modal.element.modal({backdrop: 'static'});
                modal.close.then(function (result) {

                    if (result) {

                        $scope.addAlert(result.type, result.msg);

                    }

                });
            });

        }

        $scope.deleteUser = function (user) {

            ModalService.showModal({
                templateUrl: "templates/user/delete-user-confirmation.html",
                controller: "DeleteUserConfirmationCtrl",
                inputs: {
                    user: user
                }
            }).then(function (modal) {

                modal.element.modal({backdrop: 'static'});
                modal.close.then(function (result) {

                    if (result) {

                        UserService.delete({identifier: user.identifier}, function (success) {

                            $scope.addAlert('success', 'Benutzer erfolgreich gelöscht');

                        }, function (error) {

                            ctrl.createErrorAlert(error);

                        });

                    }
                });
            });

        }

    });
