$(document).bind("mobileinit",
function() {
    $.mobile.defaultPageTransition = 'none';
    // i18n
    $.mobile.loadingMessage = false;
    // PhoneGap
    $.mobile.allowCrossDomainPages = true;
    $.support.cors = true;
    $.mobile.pushStateEnabled = false;
    $.mobile.ajaxEnabled = true;
});