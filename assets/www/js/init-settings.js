$(document).bind("mobileinit", function(){
  $.mobile.defaultPageTransition = 'none';
  // i18n
  $.mobile.loadingMessage = 'Liitytään peliin';
  $.mobile.pageLoadErrorMessage = 'lataamisessa tapahtui virhe';
  // PhoneGap
  $.mobile.allowCrossDomainPages = true;
  $.support.cors = true;
  $.mobile.pushStateEnabled = false;
  $.mobile.ajaxEnabled = true;
});