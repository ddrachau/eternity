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

            console.log("location change:");

            if (currentUrl) {
                $rootScope.nextRoute = currentUrl;
                console.log("from:" + currentUrl);
            }
            if (nextUrl) {
                console.log("to:" + nextUrl)
            }

        });

        $rootScope.$on('$routeChangeStart', function (event, next, current) {
            console.log("route change:");

            if (current) {
               // $rootScope.nextRoute = current.originalPath;
                console.log("from:" + current.originalPath)
            }
            if (next) {
                console.log("to:" + next.originalPath)
            }

        });

    });

    angular.module('Eternity').config(['$resourceProvider', function ($resourceProvider) {
        // Don't strip trailing slashes from calculated URLs
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }]);

    angular.module('Eternity').factory('UserService', function ($resource) {

        return $resource('rest/user/', [],
            {
                find: {method: 'GET', isArray: true}
            });

    });

    angular.module('Eternity').factory('ProjectService', function ($resource) {

        return $resource('rest/project/', [],
            {
                find: {method: 'GET', isArray: true}
            });

    });

    angular.module('Eternity').factory('SessionService', function ($resource) {

        return $resource('rest/auth/');
        //, [],
        //    {
        //        login: {method: 'POST', url: 'http://localhost:8080/eternity-server-war/rest/auth/login', isArray: false}
        //    });

        //var CreditCard = $resource('/user/:userId/card/:cardId',
        //    {userId: 123, cardId: '@id'}, {
        //        charge: {method: 'POST', params: {charge: true}}
        //    });

    });

})();