var billServices = angular.module('BillServices', []);

billServices.factory('BillingServices', function($resource, FlashService, SessionService) {

    var billAllRes = $resource('api/bill/:userId', {}, {getUsersBillData: {method: 'GET', isArray: true}});
    var billRecentRes = $resource('api/bill/recent/:Id', {}, {getRecentBills: {method: 'GET', isArray: true}});
    var addBillRes = $resource('api/bill/add/:Id', {}, {addBill: {method: 'GET'}});
    var billingRes = $resource('api/bill/:Id', {billId: '@billid'}, {update: {method: 'PUT'}});
    var userId = SessionService.get('userId');
    
    return {
        getUsersBill: function() {
            
            var billData = billAllRes.getUsersBillData({userId: userId}).$promise;

            billData.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show('Status Code: ' + response.status + ' Message: ' + response.statusText, 'alert-danger');
            });
            return billData;
        },
        getBillResource: function(billData) {
            var addBill = billingRes.save(billData).$promise;
            return addBill;
        },
        addBillPage: function() {
            
            var addBill = addBillRes.addBill({Id: userId}).$promise;

            addBill.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show('Status Code: ' + response.status + ' Message: ' + response.statusText, 'alert-danger');
            });
            return addBill;
        },
        getBills: function() {
            
            var bills = billRecentRes.getRecentBills({Id: userId}).$promise;

            bills.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show('Status Code: ' + response.status + ' Message: ' + response.statusText, 'alert-danger');
            });
            return bills;
        }
    };
});
