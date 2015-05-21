angular.module('Eternity').controller('AllBookingListingCtrl',
    function ($scope, TableService, ServerPushService, BookingService, ModalService) {

        var ctrl = this;

        this.displayed = [];

        ServerPushService.on('booking', $scope, function () {
            ctrl.callServer(ctrl.tableState);
        });

        this.callServer = function (tableState) {

            ctrl.tableState = tableState;
            ctrl.isLoading = true;

            var requestFilter = TableService.createRequestFilterFromTableState(tableState);

            BookingService.findAll(requestFilter, function (result) {
                ctrl.displayed = result.data;
                tableState.pagination.numberOfPages = result.numberOfPages;
                ctrl.isLoading = false;
            });

        };

    });
