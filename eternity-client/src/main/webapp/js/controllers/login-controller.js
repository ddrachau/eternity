angular.module('Eternity').controller('LoginCtrl', function($scope, $rootScope, $location, SessionService) {

    $scope.user = {identifier: 'khansen', password: 'pw'};

    $scope.login = function() {
        console.log("calling login");
        $scope.user = SessionService.save($scope.user, function(success) {
            $rootScope.loggedIn = true;
            $location.path('/');
        }, function(error) {
            $scope.loginError = true;
        });
    };

});