(function () {

    'use strict';

    angular.module('Eternity').factory('ServerPushService', function ($rootScope, $location, $websocket) {

        var service = this;
        service.events = {};

        var appPath = window.location.pathname.split('/')[1];
        var wsProtocol = $location.protocol() === 'http' ? 'ws' : 'wss';
        var wsUrl = wsProtocol + '://' + window.location.hostname + ':' + $location.port() + '/' + appPath + '/push';

        $rootScope.ws = $websocket.$new(wsUrl);

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