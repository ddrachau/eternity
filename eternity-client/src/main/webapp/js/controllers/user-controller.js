angular.module('Eternity').controller('UserCtrl', function($scope, $rootScope, $location, UserService, data) {

    $scope.users = data;

    //UserService.find(function(success) {
    //    $scope.users = success;
    //}, function(error) {
    //    $scope.users = error;
    //});

});