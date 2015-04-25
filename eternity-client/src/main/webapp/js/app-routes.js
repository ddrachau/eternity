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
            templateUrl: 'templates/authentication/login.html',
            controller: 'TokenLoginCtrl'
            /*resolve: {
                tokenLogin: function ($cookies, SessionService) {
                    if (!$cookies["XSRF-TOKEN"] && $cookies["REMEMBER-ME"]) {
                        return SessionService.loginWithToken(
                            function (success) {
                                console.log("success: " + success)
                                return success;
                            }, function (error) {
                                console.log("error: " + success)
                                delete $cookies["REMEMBER-ME"];
                                return;
                            });
                    }
                }
            }*/
        })
        .when('/logout', {
            templateUrl: 'templates/authentication/logout.html',
            controller: 'LogoutCtrl'
        })
        .otherwise({
            redirectTo: '/users'
        });

});