angular.module('Eternity').controller('DeleteUserConfirmationCtrl',
    ['$scope', 'close', 'user', function ($scope, close, user) {

        $scope.user = user;

        $scope.close = function (result) {
            close(result, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);