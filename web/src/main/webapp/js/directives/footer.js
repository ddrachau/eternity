angular.module('Eternity')
    .directive('footer', function () {
        return {

            restrict: 'E',
            scope: {
                data: "="
            },
            templateUrl: 'templates/directives/footer.html'
        };
    });
