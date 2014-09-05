var userControllers = angular.module('UserControllers', []);

userControllers.controller('UserController',
        function($scope, $log, $state, $location, $modal, cfpLoadingBar, SessionService, FlashService, BillingServices, homeBillData, recentBills, addBill) {

            $log.info = 'Inside User Controller';
            $scope.usersBillData = homeBillData;
            $scope.bills = recentBills;

            $scope.addBillData = addBill;

            $scope.addBillSplits = addBill.billSplits;

            $scope.updatedBillSPlitList = [];

            $scope.splittype = 'equally';
            $scope.billAmountChng = function() {
                updateSplitAmount();
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
                            $state.go('userhome.list', {}, {reload: true});
                        },
                        function(response) {
                            FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                            cfpLoadingBar.complete();
                        }

                );
            };
        });
