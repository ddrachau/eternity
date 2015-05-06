angular.module('Eternity').controller('ProjectCtrl', function ($scope, $rootScope, $location, ServerPushService, ProjectService, data) {

    $scope.projects = data;

    ServerPushService.on('project', 'ProjectCtrl', function (data) {

        console.log('Received "project" event: ' + data);

    });

    $scope.$on("$destroy", function () {

        ServerPushService.un('ProjectCtrl');

    });

});