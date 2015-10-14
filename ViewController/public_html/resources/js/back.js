//This is an event that fires when the user presses the device back button
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    document.addEventListener("backbutton", backKeyDown, true);
}

function backKeyDown() {
    //back on all pages, exit on Logon
    if ($('#searchcitiespp1').length) {
        var cFirm = confirm("Are you sure you want to exit the application?");
        if (cFirm == true) {
            //Code to exit the application
            navigator.app.exitApp();
        }
    }
    else {
        adf.mf.api.amx.doNavigation("__back");
    }
}

function onInvokeSuccess(param) {
    //To do code after success
};

function onFail() {
    //To do code after failure
};