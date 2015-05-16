angular.module('Eternity').directive('projectListing', function () {

    return {
        restrict: 'E',
        scope: {
            projects: "="
        },
        controller: 'ProjectListingCtrl',
        controllerAs: 'plCtrl',
        templateUrl: 'templates/project/project-listing.html'
    };

});
