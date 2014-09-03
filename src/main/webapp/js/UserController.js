var userControllers = angular.module('UserControllers', []);

userControllers.controller('UserController',
        function($scope, $log, $location, $modal, cfpLoadingBar, SessionService, FlashService, BillingServices, homeBillData, recentBills, addBill) {

            $log.info = 'Inside User Controller';
            $scope.usersBillData = homeBillData;
            $scope.bills = recentBills;
            $scope.billvalue = {};

            $scope.addBillData = addBill;
            $scope.addBillSplits = addBill.billSplits;

            $scope.splitAmount = function(billsplit){
                //console.log($scope.billvalue.addBillSplits.length);
                billsplit.amount = 20;
            }

            $scope.saveBill = function(bill) {

                console.log(bill);
                console.log($scope.billvalue);
                /*cfpLoadingBar.start();
                 var saveBill = BillingServices.getBillResource().save(bill).$promise;
                 saveBill.then(
                 function(response) {
                 FlashService.show("Bill added Successfully", "alert-success");
                 console.log(response);
                 cfpLoadingBar.complete();
                 },
                 function(response) {
                 FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
                 cfpLoadingBar.complete();
                 }
                 
                 );*/
            };
        });
