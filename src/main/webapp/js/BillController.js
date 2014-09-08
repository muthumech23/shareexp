var billControllers = angular.module('BillControllers', []);

billControllers.controller('BillListController',
        function($scope, homeBillData) {
            $scope.usersBillData = homeBillData;

        });

billControllers.controller('BillRecentController',
        function($scope, recentBills, $state) {

            $scope.bills = recentBills;
            
            $scope.editBill = function(billId) {
                console.log(billId);
                $state.go('billhome.edit', {billId: billId});
            };
        });

billControllers.controller('BillAddController',
        function($scope, $state, cfpLoadingBar, FlashService, BillingServices, addBill) {

            $scope.addBillData = addBill;

            $scope.bill;

            $scope.addBillSplits = addBill.billSplits;

            $scope.updatedBillSPlitList = [];

            $scope.splittype = 'equally';

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



billControllers.controller('BillEditController',
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
                console.log('isSelected'+$scope.updatedBillSPlitList.indexOf(billsplit));
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
