angular.module('Eternity').controller('ProjectCtrl', function($scope, $rootScope, $location, ProjectService) {

    $scope.projects = [];

    ProjectService.find(function(success) {
        $scope.projects = success;
    }, function(error) {
        $scope.projects = error;
    });

});