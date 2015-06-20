angular.module('Eternity').controller('LoginCtrl',
    function ($scope, $rootScope, $q, $location, ServerPushService, SessionService) {

        $scope.login = {
            username: '',
            password: '',
            remember: false
        };

        $scope.loginMeIn = function () {

            $scope.user = SessionService.login($scope.login, function (success) {

                ServerPushService.open();
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

            });

        };

    });