(function () {

    'use strict';

    angular.module('Eternity', ['ngRoute', 'ngResource', 'ngCookies', 'ui.bootstrap',
        'ngWebsocket', 'angularModalService', 'smart-table']);

    angular.module('Eternity').config(function ($httpProvider) {
        $httpProvider.interceptors.push(function ($cookies, $rootScope, $location, $q) {
            return {
                'request': function (request) {

                    // if we're not logged-in to the AngularJS app, redirect to login page
                    $rootScope.loggedIn = $cookies["XSRF-TOKEN"] !== undefined ||
                        $cookies["REMEMBER-ME"] !== undefined || $rootScope.loggedIn;
                    if (!$rootScope.loggedIn && $location.path() != '/login') {
                        $location.path('/login');
                    }

                    return request;
                },
                'responseError': function (rejection) {

                    // if we're not logged-in to the web service, redirect to login page
                    if (rejection.status === 401) {

                        $rootScope.user = undefined;

                        delete $cookies["XSRF-TOKEN"];

                        if ($cookies["REMEMBER-ME"]) {
                            $location.path('/tokenLogin');
                        } else if ($location.path() != '/login') {
                            $location.path('/login');
                        }

                    } else if (rejection.status === 403) {

                        $location.path('/forbidden');

                    }

                    return $q.reject(rejection);
                }
            };
        });
    });

    angular.module('Eternity').controller("EternityController", function ($rootScope, $location, UserService) {

        $rootScope.loggedIn = false;

        $rootScope.user = UserService.getBySession();

        $rootScope.$on("$locationChangeStart", function (event, nextUrl, currentUrl) {

            if (currentUrl) {
                $rootScope.nextRoute = currentUrl;
            }

        });

        $rootScope.hasAdminPermissions = function () {
            return $rootScope.user && ("ADMINISTRATOR" === $rootScope.user.role);
        };

        $rootScope.hasManagerPermissions = function () {
            return $rootScope.user &&
                ("ADMINISTRATOR" === $rootScope.user.role || "MANAGER" === $rootScope.user.role);
        };

    });

})();