(function () {

    'use strict';

    angular.module('Eternity', ['ngRoute']);

    angular.module('Eternity').config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: 'event-list.html', controller: 'EventListCtrl'})
            .when('/login', {templateUrl: 'templates/authentication/login.html', controller: 'LoginCtrl'})
            .when('/logout', {templateUrl: 'login.html', controller: 'LogoutCtrl'})
            .otherwise({redirectTo: '/'});
    });

})();