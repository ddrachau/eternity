angular.module('Eternity').controller('ProjectCtrl', function ($scope, $rootScope, $location, ServerPushService, ProjectService, data) {

    $scope.projects = data;

    ServerPushService.on('project', $scope, function (data) {

        console.log('Received "project" event: ' + data);

    });

});