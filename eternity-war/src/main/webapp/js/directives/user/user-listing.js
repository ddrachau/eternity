angular.module('Eternity').directive('userListing', function () {

    return {
        restrict: 'E',
        scope: true,
        controller: 'UserListingCtrl',
        controllerAs: 'ulCtrl',
        templateUrl: 'templates/user/user-listing.html'
    };

});
