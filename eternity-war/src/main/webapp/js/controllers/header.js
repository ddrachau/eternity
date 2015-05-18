angular.module('Eternity').controller('HeaderCtrl',
    function ($scope, TableService, ServerPushService, UserService, ModalService) {

        var ctrl = this;

        $scope.changePassword = function () {

            ModalService.showModal({
                templateUrl: "templates/authentication/change-password.html",
                controller: "ChangePasswordCtrl",
                inputs: {
                    title: 'Persönliches Passwort ändern'
                }
            }).then(function (modal) {

                modal.element.modal({backdrop: 'static'});
                modal.close.then(function (result) {

                });
            });

        }

    });
