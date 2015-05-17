angular.module('Eternity').controller('CreateUpdateProjectCtrl',
    ['$scope', '$element', 'ProjectService', 'close', 'project', 'title',
        function ($scope, $element, ProjectService, close, project, title) {

            $scope.alerts = [];
            $scope.title = title;
            $scope.project = project;

            $scope.projectId = project.id;
            $scope.identifier = project.identifier || '';
            $scope.description = project.description || '';

            $scope.addAlert = function (type, msg) {
                $scope.clearAlerts();
                $scope.alerts.push({type: type, msg: msg});
            };

            $scope.clearAlerts = function () {
                $scope.alerts.length = 0;
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

            var createErrorAlert = function (error) {

                if (error.status === 500) {
                    $scope.addAlert('danger', error.statusText);
                } else {
                    $scope.addAlert('danger', error.statusText + "\n" + error.data.error);
                }

            };

            $scope.cancel = function () {

                //  Manually hide the modal.
                $element.modal('hide');

                //  Now call close, returning control to the caller.
                close(undefined, 500);

            };

            $scope.createUpdateProject = function () {

                var project = {
                    id: $scope.projectId,
                    identifier: $scope.identifier,
                    description: $scope.description
                };

                if (project.id) {
                    ProjectService.update(project, function () {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Projekt erfolgreich aktualisiert'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                } else {
                    ProjectService.save(project, function () {

                        $element.modal('hide');

                        close({type: 'success', msg: 'Projekt erfolgreich angelegt'}, 500);

                    }, function (error) {

                        createErrorAlert(error);

                    });
                }

            };

        }]);
