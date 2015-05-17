angular.module('Eternity').controller('BookingCtrl',
    function ($scope, BookingService, ProjectService, ServerPushService,  projects) {

    var ctrl = this;

    ctrl.projects = projects;

    ServerPushService.on('project', $scope, function (data) {

        ctrl.projects = ProjectService.getProjectsForCurrentUser();

    });

});
