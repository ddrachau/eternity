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
            controller: 'LoginCtrl',
            resolve: {
                tokenLogin: function ($cookies, SessionService) {
                    if ($cookies["REMEMBER-ME"]) {
                        return SessionService.loginWithToken(
                            function (success) {
                                return success;
                            }, function (error) {
                                console.log(error);
                                delete $cookies["REMEMBER-ME"];
                                return;
                            });
                    }
                }
            }
        })
        .when('/logout', {
            templateUrl: 'templates/authentication/logout.html',
            controller: 'LogoutCtrl'
        })
        .otherwise({
            redirectTo: '/users'
        });

});