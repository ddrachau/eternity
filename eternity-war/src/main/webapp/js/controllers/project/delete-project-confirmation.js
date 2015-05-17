angular.module('Eternity').controller('DeleteProjectConfirmationCtrl',
    ['$scope', 'close', 'project', function ($scope, close, project) {

        $scope.project = project;

        $scope.close = function (result) {
            close(result, 500); // close, but give 500ms for bootstrap to animate
        };

    }]);