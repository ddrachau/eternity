var PING_LOCATION = "rest/auth/";

(function () {

    'use strict';

    angular.module('Eternity', ['ngRoute', 'ngResource', 'ngCookies']);

    angular.module('Eternity').config(function ($httpProvider) {
        $httpProvider.interceptors.push(function ($cookies, $rootScope, $location, $q) {
            return {
                'request': function (request) {
                    // if we're not logged-in to the AngularJS app, redirect to login page
                    $rootScope.loggedIn = $cookies["XSRF-TOKEN"] || $rootScope.loggedIn;
                    if (!$rootScope.loggedIn && $location.path() != '/login') {
                        //$rootScope.nextroute = request;
                        $location.path('/login');
                    }
                    return request;
                },
                'responseError': function (rejection) {
                    // if we're not logged-in to the web service, redirect to login page
                    if (rejection.status === 401 && $location.path() != '/login') {
                        if ($cookies["XSRF-TOKEN"]) {
                            $cookies["XSRF-TOKEN"] = undefined;
                        }
                        $location.path('/login');
                    }
                    return $q.reject(rejection);
                }
            };
        });
    });

    angular.module('Eternity').controller("EternityController", function ($rootScope) {

        $rootScope.$on("$locationChangeStart", function (event, nextUrl, currentUrl) {

            if (currentUrl) {
                $rootScope.nextRoute = currentUrl;
            }

        });

    });

    angular.module('Eternity').config(['$resourceProvider', function ($resourceProvider) {
        // Don't strip trailing slashes from calculated URLs
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }]);

})();