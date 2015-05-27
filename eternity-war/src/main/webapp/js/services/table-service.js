(function () {

    'use strict';

    angular.module('Eternity').factory('TableService', function () {

        return {
            createRequestFilterFromTableState: function (tableState) {

                var requestFilter = {};

                if (tableState.sort && tableState.sort.predicate) {
                    requestFilter.sort = tableState.sort.reverse ? '-' : '+';
                    requestFilter.sort += tableState.sort.predicate;
                }

                if (tableState.search && tableState.search.predicateObject) {
                    var filters = [];
                    for (var key in tableState.search.predicateObject) {
                        filters.push(key + ":" + tableState.search.predicateObject[key]);
                    }
                    requestFilter.filter = filters;
                }

                if (tableState.pagination) {
                    requestFilter.start = tableState.pagination.start || 0;
                    requestFilter.pageSize = tableState.pagination.number || 5;
                }

                return requestFilter;

            },
            processResult: function (tableState, result) {

                tableState.pagination.numberOfPages = result.numberOfPages;
                tableState.pagination.start = result.offset;
                tableState.pagination.number = result.pageSize;


            }
        };

    });

})();