angular.module('Eternity').controller('UserCtrl', function($scope, $rootScope, $location, UserService) {

    $scope.user={a:'124'};

    UserService.find(function(success) {
        $scope.user = success;
    }, function(error) {
        $scope.user = error;
    });

});