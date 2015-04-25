angular.module('Eternity').controller('LoginCtrl', function ($scope, $rootScope, $location, SessionService, tokenLogin) {

    var success2 = function (success) {

        // essential since the cookie is not yet available for checking
        $rootScope.loggedIn = true;

        if ($rootScope.nextRoute && $rootScope.nextRoute.indexOf('#') > 0
            && $rootScope.nextRoute.indexOf('logout') < 0) {

            console.log($rootScope.nextRoute);
            console.log($rootScope.nextRoute.substr($rootScope.nextRoute.indexOf('#') + 1));

            $location.path($rootScope.nextRoute.substr($rootScope.nextRoute.indexOf('#') + 1));

            $rootScope.nextRoute = undefined;

        } else {

            // default page
            $location.path('/');

        }

        return success;

    };

    if(tokenLogin) {

        console.log("successful logged in via token")
        success2();
    }

    $scope.login = {
        username: 'khansen',
        password: 'pw',
        remember: false
    };

    $scope.loginMeIn = function () {

        $scope.user = SessionService.login($scope.login, success2
            , function (error, $q) {

            $scope.loginError = true;

            return $q.reject(error);

        });

    };

});