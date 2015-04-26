angular.module('Eternity')
    .directive('header', function ($rootScope) {
        return {

            restrict: 'E',
            scope: true,
            templateUrl: 'templates/directives/header.html',
            controller: function($rootScope) {
                this.root = $rootScope;
            }
        };
    });
