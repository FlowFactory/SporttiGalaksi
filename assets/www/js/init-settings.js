$(document).bind("mobileinit",
function() {
    $.mobile.defaultPageTransition = 'slide';
    // i18n
    $.mobile.loadingMessage = false;
    // PhoneGap
    $.mobile.allowCrossDomainPages = true;
    $.support.cors = true;
    $.mobile.pushStateEnabled = false;
    $.mobile.ajaxEnabled = true;
});