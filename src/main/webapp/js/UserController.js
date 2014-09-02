var userControllers = angular.module('UserControllers', [ ]);

userControllers.controller('UserController',
        function($scope, $log, $location, $modal, cfpLoadingBar, SessionService, FlashService, BillingServices, homeBillData) {

            $log.info = 'Inside User Controller';
            $scope.usersBillData = homeBillData;
            
            $scope.billvalue = {};
            
            $scope.saveBill = function(bill){
                alert(bill);
                console.log(bill);
                bill.billSplits = $scope.billvalue;
                console.log($scope.billvalue);
                console.log(bill);
            };
            
            $scope.AddBill = function(billData, size) {
                $scope.billDatas = billData;
                console.log(billData);
                console.log(size);
                var modalInstance = $modal.open({
                    templateUrl: 'template/bill.html',
                    controller: BillInstanceCtrl,
                    size: size,
                    resolve: {
                        friendsData: function() {
                            return $scope.billDatas;
                        }
                    }
                });

                modalInstance.result.then(function(billData) {
                    cfpLoadingBar.start();

                    var user = SessionService.get('userId');
                    console.log(billData);
                }, function() {

                });
            };


        });

var BillInstanceCtrl = function($scope, $modalInstance, friendsData) {

console.log(friendsData);
    $scope.billDatas = friendsData;

    $scope.ok = function() {
        $modalInstance.close($scope.billDatas);

    };

    $scope.cancel = function() {
        console.log('inside');
        $modalInstance.dismiss('cancel');
    };
};