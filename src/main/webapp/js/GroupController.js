var groupControllers = angular.module('GroupControllers', []);

groupControllers.controller('GroupListController',
        function($scope, $log, $state, cfpLoadingBar, SessionService, FlashService, GroupServices, getGroupList) {

            $scope.groupList = getGroupList;

            $scope.getGroupBills = function(groupId) {
                console.log(groupId);
                $state.go('billhome.groupbills', {groupId: groupId});
            };

            $scope.editGroup = function(groupId) {
                console.log(groupId);
                $state.go('billhome.groupedit', {groupId: groupId});
            };

        });

groupControllers.controller('GroupRecentController',
        function($scope, $stateParams, $state, cfpLoadingBar, SessionService, FlashService, GroupServices) {

            console.log($stateParams.groupId);
            $scope.groupId = $stateParams.groupId;
            var groupBill = GroupServices.getGroupBills($scope.groupId);

            groupBill.then(
                    function(response) {
                        $scope.groupBillsData = response;
                        cfpLoadingBar.complete();
                    },
                    function(response) {
                        FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                        cfpLoadingBar.complete();
                    }
            );

            $scope.addGroupBill = function(grpId) {
                console.log('Group Add' + grpId);
                $state.go('billhome.grpaddbill', {groupId: grpId});
            };
        });

groupControllers.controller('GroupController',
        function($scope, $state, $stateParams, cfpLoadingBar, SessionService, FlashService, GroupServices) {

            console.log("Inside Edit Group" + $stateParams.groupId);

            $scope.shareGroup;

            $scope.updatedFriendList = [];

            if ($stateParams.groupId !== null || $stateParams.groupId !== "" || $stateParams.groupId !== 'undefined')
            {
                console.log("Inside Edit Group");
                $scope.groupId = $stateParams.groupId;
                var group = GroupServices.getGroup($scope.groupId);

                group.then(
                        function(response) {
                            $scope.shareGroup = response;
                            console.log("success");
                            console.log($scope.shareGroup);
                            console.log($scope.shareGroup.userIds);
                            $scope.updatedFriendList = $scope.shareGroup.userIds;
                            cfpLoadingBar.complete();
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }
                );
            }
            console.log($scope.updatedFriendList);
            var updateSelected = function(action, userId) {
                if (action === 'add' && $scope.updatedFriendList.indexOf(userId) === -1) {
                    $scope.updatedFriendList.push(userId);
                    console.log($scope.updatedFriendList);

                }
                if (action === 'remove' && $scope.updatedFriendList.indexOf(userId) !== -1) {
                    $scope.updatedFriendList.splice($scope.updatedFriendList.indexOf(userId), 1);
                    console.log($scope.updatedFriendList);
                }
            };

            $scope.updateSelection = function($event, userId) {
                var checkbox = $event.target;
                var action = (checkbox.checked ? 'add' : 'remove');
                console.log(action);
                updateSelected(action, userId);
            };

            $scope.isSelected = function(userId) {
                console.log('isSelected --<' + userId);
                return $scope.updatedFriendList.indexOf(userId) >= 0;
            };

            $scope.saveGroup = function(group) {

                console.log(group);

                console.log($scope.updatedFriendList);
                group.userIds = $scope.updatedFriendList;
                group.email = SessionService.get('userEmail');
                group.userId = SessionService.get('userId');
                console.log(group);

                cfpLoadingBar.start();
                var saveGroup = GroupServices.addGroup(group);
                saveGroup.then(
                        function(response) {
                            FlashService.show("Group added Successfully", "alert-success");
                            console.log(response);
                            cfpLoadingBar.complete();
                            $state.go('billhome.grouplist', {}, {reload: true});
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });


groupControllers.controller('GroupAddBillController',
        function($scope, $stateParams, $state, cfpLoadingBar, SessionService, FlashService, GroupServices, addBill) {

            $scope.addBillData = addBill;

            $scope.bill;

            $scope.addBillData.groupId = $stateParams.groupId;

            $scope.addBillSplits = addBill.billSplits;

            $scope.updatedBillSPlitList = [];

            $scope.splittype = 'equally';
            $scope.billAmountChng = function() {
                updateSplitAmount();
            };
            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };
            var updateSplitAmount = function() {
                //console.log($scope.billvalue.addBillSplits.length);
                console.log($scope.updatedBillSPlitList.length);
                console.log($scope.splittype);
                if ($scope.splittype === 'equally') {
                    updateSplitEqually();
                } else if ($scope.splittype === 'share') {
                    updateSplitShare();
                } else if ($scope.splittype === 'exact') {
                    updateSplitExact();
                }
            };

            var updateSelected = function(action, billsplit) {
                console.log($scope.updatedBillSPlitList.indexOf(billsplit));
                if (action === 'add' && $scope.updatedBillSPlitList.indexOf(billsplit) === -1) {
                    $scope.updatedBillSPlitList.push(billsplit);
                    console.log($scope.bill.amount);
                    console.log($scope.updatedBillSPlitList);

                    updateSplitAmount();
                }
                if (action === 'remove' && $scope.updatedBillSPlitList.indexOf(billsplit) !== -1) {
                    $scope.updatedBillSPlitList.splice($scope.updatedBillSPlitList.indexOf(billsplit), 1);
                    console.log($scope.updatedBillSPlitList);
                    updateSplitAmount();
                }
            };

            var updateSplitEqually = function() {
                var splitcount = $scope.updatedBillSPlitList.length;
                console.log('splitCount --<' + splitcount);
                var billAmount = $scope.bill.amount;

                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
                    console.log(billsplit.userId);
                    console.log(billAmount / splitcount);
                    console.log(billsplit.amount);
                    if ($scope.bill.userPaid === billsplit.userId) {
                        billsplit.amount = billAmount / splitcount;
                    } else {
                        billsplit.amount = -(billAmount / splitcount);
                    }

                    console.log(billsplit.amount);
                });
            };

            var updateSplitShare = function() {
                var splitcount = $scope.updatedBillSPlitList.length;
                console.log('splitCount --<' + splitcount);
                var billAmount = $scope.bill.amount;

                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
                    console.log(billsplit.userId);
                    console.log(billAmount / splitcount);
                    console.log(billsplit.amount);
                    if ($scope.bill.userPaid === billsplit.userId) {
                        billsplit.amount = billAmount / splitcount;
                    } else {
                        billsplit.amount = -(billAmount / splitcount);
                    }
                    console.log(billsplit.amount);
                });
            };

            var updateSplitExact = function() {
                var splitcount = $scope.updatedBillSPlitList.length;
                console.log('splitCount --<' + splitcount);
                var billAmount = $scope.bill.amount;

                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
                    console.log(billsplit.userId);
                    console.log(billAmount / splitcount);
                    console.log(billsplit.amount);
                    if ($scope.bill.userPaid === billsplit.userId) {
                        billsplit.amount = billAmount / splitcount;
                    } else {
                        billsplit.amount = -(billAmount / splitcount);
                    }
                    console.log(billsplit.amount);
                });
            };

            $scope.updateSelection = function($event, billsplit) {
                var checkbox = $event.target;
                var action = (checkbox.checked ? 'add' : 'remove');
                console.log(action);
                updateSelected(action, billsplit);
            };

            $scope.isSelected = function(billsplit) {
                console.log('isSelected --<' + billsplit);
                return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;
            };

            $scope.splitInitial = function() {
                /*for (var i = 0; i < $scope.entities.length; i++) {
                 var entity = $scope.entities[i];
                 updateSelected(action, entity.id);
                 }*/
            };

            $scope.saveBill = function(billData) {

                console.log(billData);
                console.log($scope.updatedBillSPlitList);
                billData.billSplits = $scope.updatedBillSPlitList;
                console.log(billData);

                cfpLoadingBar.start();
                var saveBill = BillingServices.getBillResource(billData);
                saveBill.then(
                        function(response) {
                            FlashService.show("Bill added Successfully", "alert-success");
                            console.log(response);
                            cfpLoadingBar.complete();
                            $state.go('billhome.list', {}, {reload: true});
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };

        });



groupControllers.controller('GroupEditBillController',
        function($scope, $state, $stateParams, cfpLoadingBar, FlashService, BillingServices) {

            if ($stateParams.billId !== null || $stateParams.billId !== "" || $stateParams.billId !== 'undefined')
            {
                var bill = BillingServices.showBillResource($stateParams.billId);

                bill.then(
                        function(response) {
                            $scope.bill = response;
                            $scope.updatedBillSPlitList = $scope.bill.billSplits;
                            cfpLoadingBar.complete();
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }
                );
            }
            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };
            $scope.billAmountChng = function() {
                updateSplitAmount();
            };

            var updateSplitAmount = function() {
                if ($scope.splittype === 'equally') {
                    updateSplitEqually();
                } else if ($scope.splittype === 'share') {
                    updateSplitShare();
                } else if ($scope.splittype === 'exact') {
                    updateSplitExact();
                }
            };

            var updateSelected = function(action, billsplit) {
                if (action === 'add' && $scope.updatedBillSPlitList.indexOf(billsplit) === -1) {
                    $scope.updatedBillSPlitList.push(billsplit);
                    updateSplitAmount();
                }
                if (action === 'remove' && $scope.updatedBillSPlitList.indexOf(billsplit) !== -1) {
                    $scope.updatedBillSPlitList.splice($scope.updatedBillSPlitList.indexOf(billsplit), 1);
                    billsplit.amount = 0;
                    updateSplitAmount();
                }
            };

            var updateSplitEqually = function() {
                var splitcount = $scope.updatedBillSPlitList.length;
                var billAmount = $scope.bill.amount;

                var splittedAmt = billAmount / splitcount;

                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
                    if ($scope.bill.userPaid === billsplit.userId) {
                        billsplit.amount = splittedAmt;
                    } else {
                        billsplit.amount = -splittedAmt;
                    }
                });
            };

            var updateSplitShare = function() {
                var splitcount = 0;
                var billAmount = $scope.bill.amount;


                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {
                    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
                        splitcount = splitcount + 1;
                    } else {
                        splitcount = splitcount + billsplit.splitText;
                    }
                });

                var splittedAmt = billAmount / splitcount;

                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

                    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
                        billsplit.amount = splittedAmt * 1;
                    } else {
                        billsplit.amount = splittedAmt * billsplit.splitText;
                    }

                    if ($scope.bill.userPaid === billsplit.userId) {
                    } else {
                        billsplit.amount = -billsplit.amount;
                    }
                });
            };

            var updateSplitExact = function() {
                var splitcount = $scope.updatedBillSPlitList.length;
                var billAmount = $scope.bill.amount;



                angular.forEach($scope.updatedBillSPlitList, function(billsplit) {

                    var splittedAmt = billAmount / splitcount;
                    if (billsplit.splitText === "" || billsplit.splitText === null || billsplit.splitText === undefined) {
                        billsplit.amount = splittedAmt;
                    } else {

                        billsplit.amount = billsplit.splitText;
                        billAmount = billAmount - billsplit.splitText;
                        splitcount = splitcount - 1;
                    }

                    if ($scope.bill.userPaid === billsplit.userId) {
                    } else {
                        billsplit.amount = -billsplit.amount;
                    }
                });
            };

            $scope.updateSelection = function($event, billsplit) {
                var checkbox = $event.target;
                var action = (checkbox.checked ? 'add' : 'remove');
                updateSelected(action, billsplit);
            };

            $scope.isSelected = function(billsplit) {
                console.log('isSelected' + $scope.updatedBillSPlitList.indexOf(billsplit));
                return $scope.updatedBillSPlitList.indexOf(billsplit) >= 0;
            };

            $scope.splitInitial = function() {
                /*for (var i = 0; i < $scope.entities.length; i++) {
                 var entity = $scope.entities[i];
                 updateSelected(action, entity.id);
                 }*/
            };

            $scope.saveBill = function(billData) {
                billData.billSplits = $scope.updatedBillSPlitList;

                cfpLoadingBar.start();
                var saveBill = BillingServices.getBillResource(billData);
                saveBill.then(
                        function(response) {
                            FlashService.show("Bill added Successfully", "alert-success");
                            cfpLoadingBar.complete();
                            $state.go('billhome.list', {}, {reload: true});
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }
                );
            };
        });

