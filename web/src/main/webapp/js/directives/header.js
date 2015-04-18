angular.module('Eternity')
    .directive('header', function () {
        return {

            restrict: 'E',
            scope: {
                data: "="
            },
            templateUrl: 'templates/directives/header.html'
        };
    });
