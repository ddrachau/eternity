angular.module('Eternity').controller('LoginCtrl', function ($scope, $rootScope, $location, SessionService) {

    $scope.login = {
        username: 'khansen',
        password: 'pw',
        remember: false
    };

    $scope.loginMeIn = function () {
        $scope.user = SessionService.save($scope.login, function (success) {
            $rootScope.loggedIn = true;
            $location.path('/');
        }, function (error) {
            $scope.loginError = true;
        });
    };

});