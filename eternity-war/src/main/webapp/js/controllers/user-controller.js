angular.module('Eternity').controller('UserCtrl', function ($scope, $rootScope, $location, ServerPushService, UserService, data) {

    $scope.users = data;

    ServerPushService.on('user', 'UserCtrl', function (data) {

        console.log('Received "user" event: ' + data);

    });

    $scope.$on("$destroy", function () {

        ServerPushService.un('UserCtrl');

    });

});