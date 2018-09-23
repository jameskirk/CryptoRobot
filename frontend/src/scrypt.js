var isMobile =false;
$(document).ready(function(){
  var ua = navigator.userAgent;

  if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini|Mobile|mobile|CriOS/i.test(ua))
    //$('a.mobile-other').show();
    isMobile = true;

  else if (/Chrome/i.test(ua))
    //$('a.chrome').show();
    isMobile = false;

  else {
    //$('a.desktop-other').show();
    isMobile = false;}

  console.log("isMobile = " + isMobile);
});
