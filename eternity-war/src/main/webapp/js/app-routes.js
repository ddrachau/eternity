angular.module('Eternity').config(function ($routeProvider) {

    $routeProvider
        .when('/users', {
            templateUrl: 'templates/user/users.html',
            controller: 'UserCtrl'
        })
        .when('/projects', {
            templateUrl: 'templates/project/projects.html',
            controller: 'ProjectCtrl'
        })
        .when('/bookings', {
            templateUrl: 'templates/booking/bookings.html',
            controller: 'BookingCtrl',
            controllerAs: 'bCtrl',
            resolve: {
                projects: function (ProjectService) {
                    return ProjectService.getProjectsForCurrentUser().$promise;
                }
            }
        })
        .when('/login', {
            templateUrl: 'templates/authentication/login.html',
            controller: 'LoginCtrl'
        })
        .when('/tokenLogin', {
            templateUrl: 'templates/authentication/token.html',
            controller: 'TokenLoginCtrl'
        })
        .when('/logout', {
            templateUrl: 'templates/authentication/logout.html',
            controller: 'LogoutCtrl'
        })
        .when('/forbidden', {
            templateUrl: 'templates/authentication/forbidden.html'
        })
        .otherwise({
            redirectTo: '/bookings'
        });

});