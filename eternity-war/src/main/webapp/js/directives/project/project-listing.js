angular.module('Eternity').directive('projectListing', function () {

    return {
        restrict: 'E',
        scope: true,
        controller: 'ProjectListingCtrl',
        controllerAs: 'plCtrl',
        templateUrl: 'templates/project/project-listing.html'
    };

});
