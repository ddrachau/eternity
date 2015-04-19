angular.module('Eternity').config(function ($routeProvider) {

    $routeProvider
        .when('/', {templateUrl: 'templates/list-users.html', controller: 'UserCtrl', resolve: {authorize: function ($http) {
            return $http.get(PING_LOCATION);
        }}})
        .when('/login', {templateUrl: 'templates/authentication/login.html', controller: 'LoginCtrl'})
        .when('/logout', {templateUrl: 'templates/authentication/logout.html', controller: 'LogoutCtrl'})
        .otherwise({redirectTo: '/'});

});