angular.module('Eternity').controller('LoginCtrl', function ($scope, $rootScope, $location, SessionService) {

    $scope.login = {
        username: 'admin',
        password: 'pw',
        remember: false
    };

    $scope.loginMeIn = function () {

        $scope.user = SessionService.login($scope.login, function (success) {

            // essential since the cookie is not yet available for checking
            $rootScope.loggedIn = true;
            $rootScope.user = success;

            if ($rootScope.nextRoute && $rootScope.nextRoute.indexOf('#') > 0
                && $rootScope.nextRoute.indexOf('logout') < 0
                && $rootScope.nextRoute.indexOf('login') < 0) {

                $location.path($rootScope.nextRoute.substr($rootScope.nextRoute.indexOf('#') + 1));

                $rootScope.nextRoute = undefined;

            } else {

                // default page
                $location.path('/');

            }

            return success;

        }, function (error, $q) {

            $scope.loginError = true;

            return $q.reject(error);

        });

    };

});