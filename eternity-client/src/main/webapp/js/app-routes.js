angular.module('Eternity').config(function ($routeProvider) {

    $routeProvider
        .when('/users', {
            templateUrl: 'templates/list-users.html',
            controller: 'UserCtrl',
            resolve: {
                data: function (UserService) {
                    return UserService.find();
                }
            }
        })
        .when('/projects', {
            templateUrl: 'templates/list-projects.html',
            controller: 'ProjectCtrl',
            resolve: {
                data: function (ProjectService) {
                    return ProjectService.find();
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
        .otherwise({
            redirectTo: '/users'
        });

});