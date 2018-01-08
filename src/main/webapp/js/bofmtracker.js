function daysUntil(year, month, day) {
  var now = new Date(),
      dateEnd = new Date(year, month - 1, day), // months are zero-based
      days = (dateEnd - now) / 1000/60/60/24;   // convert milliseconds to days

  return Math.round(days);
}

$(document).on("click", "input[type='checkbox']", function () {

    var boxes = $('input[type=checkbox]').length;
    var cboxes = $('input[type=checkbox]:checked').length;
    var days = daysUntil(2018,7,1);
    var remaining = (boxes - cboxes);
    console.log(this.checked);
    console.log(this.id);
    console.log(boxes)
    console.log(cboxes)
    
    $('#days').text(days);
    $('#read').text(cboxes);
    $('#remaining').text(remaining);
    $('#chapters').text( Math.ceil(remaining / days));
    var p = cboxes/boxes * 100;
    $('#percent').text(p.toFixed(2));

    if(this.checked) {
	$.ajax({
	    url: '/read',
	    type: 'PUT',
	    data: "chapter=" + this.id,
	    success: function(data) {
		console.log('PUT was performed.' + data);
	    }
	});
    } else {
	$.ajax({
	    url: '/read',
	    type: 'DELETE',
	    data: "chapter=" + this.id,
	    success: function(data) {
		console.log('DELETE was performed.' + data);
	    }
	});
    }


});

$(document).ready(function () {
    $('#days').text(daysUntil(2018,7,1));

    var webAuth = new auth0.WebAuth({
	domain: '2018firm.auth0.com',
	clientID: 'S2peKq6xxBILjv2vu4LPRXR0WywJKm1M',
	redirectUri: 'http://reading.2018firm.life',
	audience: 'https://' + '2018firm.auth0.com' + '/userinfo',
	responseType: 'token id_token',
	scope: 'openid'
    });
    
    var loginBtn = $('#btn-login');
    
    loginBtn.click(function(e) {
	e.preventDefault();
    webAuth.authorize();
    });
});
