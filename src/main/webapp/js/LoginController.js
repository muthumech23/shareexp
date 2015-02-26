'use strict'
var loginControllers = angular.module('LoginControllers', []);

loginControllers.controller('HomeController', function($scope, $log, $location) {

    $scope.myInterval = 5000;

    var slides = $scope.slides = [{
        image: "img/slide1.png", text: ''
    }, {
        image: "img/slide2.png", text: ''
    }, {
        image: "img/slide3.png", text: ''
    }];

    $scope.chart = {
        "type": "ColumnChart",
        "cssStyle": "height:200px; width:300px;",
        "data": {
            "cols": [{
                "id": "month",
                "label": "Month",
                "type": "string",
                "p": {}
            }, {
                "id": "laptop-id",
                "label": "Laptop",
                "type": "number",
                "p": {}
            }, {
                "id": "desktop-id",
                "label": "Desktop",
                "type": "number",
                "p": {}
            }, {
                "id": "server-id",
                "label": "Server",
                "type": "number",
                "p": {}
            }, {
                "id": "cost-id",
                "label": "Shipping",
                "type": "number"
            }],
            "rows": [{
                "c": [{
                    "v": "January"
                }, {
                    "v": 19,
                    "f": "42 items"
                }, {
                    "v": 12,
                    "f": "Ony 12 items"
                }, {
                    "v": 7,
                    "f": "7 servers"
                }, {
                    "v": 4
                }]
            }, {
                "c": [{
                    "v": "February"
                }, {
                    "v": 13
                }, {
                    "v": 1,
                    "f": "1 unit (Out of stock this month)"
                }, {
                    "v": 12
                }, {
                    "v": 2
                }]
            }, {
                "c": [{
                    "v": "March"
                }, {
                    "v": 24
                }, {
                    "v": 0
                }, {
                    "v": 11
                }, {
                    "v": 6
                }]
            }]
        },
        "options": {
            "title": "Sales per month",
            "isStacked": "false",
            "fill": 20,
            "displayExactValues": true,
            "vAxis": {
                "title": "Sales unit",
                "gridlines": {
                    "count": 6
                }
            },
            "hAxis": {
                "title": "Date"
            }
        },
        "formatters": {},
        "displayed": true
    }

});

loginControllers.controller('ActivateController', function($scope, SessionService) {

    $scope.userSecure = {};

    $scope.userSecure.userId = SessionService.get('userEmail');

});

loginControllers.controller('ChgpwdController', function($scope, SessionService) {
    $scope.user = {};
    $scope.user.email = SessionService.get('userEmail');
    $scope.user.name = SessionService.get('userName');

});

/* Login Controller */
loginControllers
    .controller(
        'LoginController',
        function($scope, $state, cfpLoadingBar, UserServices, flash) {

            $scope.title = 'Share Expenses - Login';


            $scope.registerUser = function(user) {

                if (user.password !== $scope.confirmpassword) {
                    flash.pop({
                        title: '',
                        body: 'Password and Confirm password are different. Please correct it.',
                        type: 'alert-danger'
                    });
                    return;
                } else {
                    cfpLoadingBar.start();

                    var hashpassword = CryptoJS.SHA256(user.password);
                    user.password = hashpassword.toString();

                    var userAdd = UserServices.save(user).$promise;
                    userAdd
                        .then(
                            function(response) {
                                flash.set({
                                    title: '',
                                    body: "Register has been completed successfully! Activation code has been sent your Inbox (please check your spam in case not available in inbox). Please enter activation code to activate your account.",
                                    type: 'alert-success'
                                });
                                $state.go("home.activation");
                            },
                            function(response) {
                                $scope.errorresource = response.data;
                                flash.pop({
                                    title: '',
                                    body: $scope.errorresource.message,
                                    type: 'alert-danger'
                                });
                                cfpLoadingBar.complete();
                            }

                        );

                }

            };

            $scope.saveUser = function(user) {
                cfpLoadingBar.start();

                var hashpassword = CryptoJS.SHA256(user.password);
                user.password = hashpassword.toString();

                var userAdd = UserServices.save(user).$promise;
                userAdd.then(function(response) {
                        flash.pop({
                            title: '',
                            body: "Your password has been updated successfully",
                            type: 'alert-success'
                        });
                    }, function(response) {
                        $scope.errorresource = response.data;
                        flash.pop({
                            title: '',
                            body: $scope.errorresource.message,
                            type: 'alert-danger'
                        });
                        cfpLoadingBar.complete();
                    }

                );
            };
        });

/* Login Controller */
loginControllers.controller('UpdateUserController', function($scope, SessionService, cfpLoadingBar, UserServices, flash) {

    var user = SessionService.get('userId');
    var getUser = UserServices.get({
        Id: user
    }).$promise;
    getUser.then(function(response) {
            $scope.user = response;
        }, function(response) {
            $scope.errorresource = response.data;
            flash.pop({
                title: '',
                body: $scope.errorresource.message,
                type: 'alert-danger'
            });
            cfpLoadingBar.complete();
        }

    );

    $scope.saveUser = function(user) {

        cfpLoadingBar.start();

        var hashpassword = CryptoJS.SHA256(user.password);
        user.password = hashpassword.toString();

        var userAdd = UserServices.save(user).$promise;

        userAdd.then(function(response) {
                flash.pop({
                    title: '',
                    body: "Your password has been updated successfully.",
                    type: 'alert-success'
                });
            }, function(response) {
                $scope.errorresource = response.data;
                flash.pop({
                    title: '',
                    body: $scope.errorresource.message,
                    type: 'alert-danger'
                });
                cfpLoadingBar.complete();
            }

        );
    };
});

/* Contact Controller */
loginControllers.controller('ContactController', function($scope, SessionService, cfpLoadingBar, AuthenticationService, flash) {

    $scope.contactUs = function(contact) {

        cfpLoadingBar.start();

        var userMsgSent = AuthenticationService.contactUs().sendMsg(contact).$promise;

        userMsgSent.then(function(response) {
            flash.pop({
                title: '',
                body: "Your message has been sent successfully.",
                type: 'alert-success'
            });
        }, function(response) {
            $scope.errorresource = response.data;
            flash.pop({
                title: '',
                body: $scope.errorresource.message,
                type: 'alert-danger'
            });
            cfpLoadingBar.complete();
        });

    };
});