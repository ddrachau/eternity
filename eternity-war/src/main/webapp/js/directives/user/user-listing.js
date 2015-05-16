angular.module('Eternity').directive('userListing', function () {

    return {
        restrict: 'E',
        scope: {
            projects: "="
        },
        controller: 'UserListingCtrl',
        controllerAs: 'ulCtrl',
        templateUrl: 'templates/user/user-listing.html'
    };

});
