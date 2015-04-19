angular.module('Eternity').controller('LogoutCtrl', function ($cookies,$cookieStore, $rootScope, $location, SessionService) {


    (new SessionService()).$delete(function ($cookies, success) {

        $rootScope.loggedIn = false;

    });


});