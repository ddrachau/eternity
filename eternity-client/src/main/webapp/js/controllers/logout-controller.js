angular.module('Eternity').controller('LogoutCtrl', function ($rootScope, $scope, SessionService) {

    (new SessionService()).$delete(function (success) {

        $rootScope.loggedIn = false;
        $rootScope.user = undefined;
        $scope.logoutSuccess = success;

    }, function (error) {

        $scope.logoutError = error;

    });

});