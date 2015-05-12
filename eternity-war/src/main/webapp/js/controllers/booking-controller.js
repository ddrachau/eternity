angular.module('Eternity').controller('BookingCtrl',
    function ($scope, BookingService, UserService, ServerPushService,  projects) {

    var ctrl = this;

    ctrl.projects = projects;

    ServerPushService.on('project', $scope, function (data) {

        console.log('Received "project" event: ' + data);
        ctrl.projects = UserService.getProjectsForCurrentUser();

    });

});
