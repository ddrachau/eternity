angular.module('Eternity').controller('UserCtrl', function ($scope, $rootScope, $location, UserService, data) {

    $scope.users = data;

});