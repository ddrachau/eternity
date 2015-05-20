angular.module('Eternity').controller('BookingCtrl',
    function ($scope, BookingService, ProjectService, ServerPushService, projects) {

        var ctrl = this;

        $scope.projects = projects;

        ServerPushService.on('project', $scope, function (data) {

            $scope.projects = ProjectService.getProjectsForCurrentUser();

        });
        ServerPushService.on('assignment', $scope, function (data) {

            $scope.projects = ProjectService.getProjectsForCurrentUser();

        });

    });
