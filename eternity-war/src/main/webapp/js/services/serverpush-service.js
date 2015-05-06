(function () {

    'use strict';

    angular.module('Eternity').factory('ServerPushService', function ($rootScope, $websocket) {

        var service = this;
        service.events = {};

        $rootScope.ws = $websocket.$new('ws://localhost:8080/eternity-war/push');

        $rootScope.ws.$on('$open', function () {

            console.log('Open new server push connection');

        });

        $rootScope.ws.$on('$close', function () {

            console.log('Server push connection lost');

        });

        $rootScope.ws.$on('$message', function (data) {

            if (data.event && data.event.length > 0) {

                var handlers = service.events[data.event];

                if (handlers) {

                    for (var h = 0; h < handlers.length; h++) {
                        handlers[h].handler(data);
                    }

                }

            }

        });

        return {

            on: function (event, controller, handler) {

                if (!service.events[event]) {
                    service.events[event] = [];
                }

                var eventHandler = {
                    controller: controller,
                    handler: handler
                };

                service.events[event].push(eventHandler);

            },
            un: function (controller) {

                for (var event in service.events) {
                    if (service.events.hasOwnProperty(event)) {
                        service.events[event] =
                            service.events[event].filter(function (handler) {
                                return handler.controller !== controller;
                            });
                    }
                }

            }

        }

    });

})();