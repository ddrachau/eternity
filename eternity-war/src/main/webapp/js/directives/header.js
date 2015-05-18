angular.module('Eternity').directive('header', function () {

    return {

        restrict: 'E',
        scope: true,
        templateUrl: 'templates/header.html',
        controller: 'HeaderCtrl',
        controllerAs: 'hCtrl'

    };

});
