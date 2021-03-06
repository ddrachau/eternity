angular.module('Eternity').controller('TokenLoginCtrl', function ($scope, $rootScope, $location, ServerPushService, SessionService) {

    (function () {

        SessionService.loginWithToken(function (success) {

            ServerPushService.open();
            // essential since the cookie is not yet available for checking
            $rootScope.loggedIn = true;
            $rootScope.user = success;

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

        }, function (error, $q) {

            $location.path('/login');

            return $q.reject(error);

        });

    })();

});