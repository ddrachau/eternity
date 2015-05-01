angular.module('Eternity').controller('ProjectCtrl', function ($scope, $rootScope, $location, ProjectService,data) {

    $scope.projects = data;

});