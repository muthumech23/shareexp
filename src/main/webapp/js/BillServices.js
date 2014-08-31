var billServices = angular.module('BillServices', []);

billServices.factory("BillingServices", function($resource, FlashService, SessionService) {

    var billAllRes = $resource("api/bills/all", {}, {getHomeBillData: {method: 'POST', isArray: true}});
    var billRes = $resource('api/bill/:Id', {billId: '@billid'}, {update: {method: 'PUT'}});
    return {
        getHomeBills: function() {
            var userId = SessionService.get('userId');
            var billData = billAllRes.getHomeBillData(userId).$promise;

            billData.then(function(response) {
                return response.data;
            }, function(response) {
                FlashService.show("Status Code: " + response.status + " Message: " + response.statusText, "alert-danger");
            });
            return billData;
        }
    };
});
