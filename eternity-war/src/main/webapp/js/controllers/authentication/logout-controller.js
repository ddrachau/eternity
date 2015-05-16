angular.module('Eternity').controller('LogoutCtrl', function ($rootScope, $scope, ServerPushService, SessionService) {

    (new SessionService()).$delete(function (success) {

        $rootScope.loggedIn = false;
        ServerPushService.close();
        $rootScope.user = undefined;
        $scope.logoutSuccess = success;

    }, function (error) {

        $scope.logoutError = error;

    });

});