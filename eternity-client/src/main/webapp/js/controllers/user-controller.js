angular.module('Eternity').controller('UserCtrl', function($scope, $rootScope, $location, UserService) {

    $scope.users = [];

    UserService.find(function(success) {
        $scope.users = success;
    }, function(error) {
        $scope.users = error;
    });

});