angular.module('Eternity').controller('LoginCtrl', function($scope, $rootScope, $location, SessionService) {
    $scope.user = {username: '', password: ''};

    $scope.login = function() {
        $scope.user = SessionService.save($scope.user, function(success) {
            $rootScope.loggedIn = true;
            $location.path('/');
        }, function(error) {
            $scope.loginError = true;
        });
    };
});