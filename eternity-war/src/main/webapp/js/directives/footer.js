angular.module('Eternity')
    .directive('footer', function () {
        return {

            restrict: 'E',
            scope: true,
            templateUrl: 'templates/footer.html'
        };
    });
