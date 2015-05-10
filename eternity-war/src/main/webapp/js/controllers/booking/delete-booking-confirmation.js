angular.module('Eternity').controller('DeleteBookingConfirmationCtrl', ['$scope', 'close','booking', function($scope, close, booking) {

    $scope.booking = booking;

    $scope.close = function(result) {
        close(result, 200); // close, but give 500ms for bootstrap to animate
    };

}]);