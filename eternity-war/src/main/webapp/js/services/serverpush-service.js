(function () {

    'use strict';

    angular.module('Eternity').factory('ServerPushService', function ($rootScope, $location, $websocket) {

        var service = this;
        service.events = {};

        var appPath = window.location.pathname.split('/')[1];
        var wsProtocol = $location.protocol() === 'http' ? 'ws' : 'wss';
        var wsUrl = wsProtocol + '://' + window.location.hostname + ':' + $location.port() + '/' + appPath + '/push';
        var ws = $websocket.$new(wsUrl);
        $rootScope.ws = ws;

        ws.$on('$open', function () {
            console.info('Open new server push connection');
        });

        ws.$on('$close', function () {
            console.info('Server push connection lost');
        });

        ws.$on('$message', function (data) {

            if (data.event && data.event.length > 0) {

                console.info("Message received: event ('" + data.event + "')");
                var handlers = service.events[data.event];

                if (handlers) {
                    for (var h = 0; h < handlers.length; h++) {
                        console.info("Message dispatch to scope with id: " + handlers[h].scopeId);
                        handlers[h].handler(data);
                    }
                }
            }

        });

        var unregister = function (scopeId) {

            for (var event in service.events) {
                if (service.events.hasOwnProperty(event)) {
                    service.events[event] =
                        service.events[event].filter(function (handler) {
                            return handler.scopeId !== scopeId;
                        });
                }
            }

        };

        var register = function (event, $scope, handler) {

            if (!service.events[event]) {
                service.events[event] = [];
            }

            var eventHandler = {
                scopeId: $scope.$id,
                handler: handler
            };

            service.events[event].push(eventHandler);

            $scope.$on("$destroy", function ($scope) {

                unregister($scope.targetScope.$id);

            });

        };

        var openConnection = function () {
            ws.$open();
        };

        var closeConnection = function () {
            ws.$close();
        };

        return {
            on: register,
            open: openConnection,
            close: closeConnection
        }

    });

})();