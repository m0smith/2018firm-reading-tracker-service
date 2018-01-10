function daysUntil(year, month, day) {
  var now = new Date(),
      dateEnd = new Date(year, month - 1, day), // months are zero-based
      days = (dateEnd - now) / 1000/60/60/24;   // convert milliseconds to days

  return Math.round(days);
}

var readChapters = function() {
    $.ajax({
        url: "/read",
        type: "GET",
        datatype:"JSON",
        contentType: "application/json",
      
         
        error : function(data){console.log("error:",data)
    },
        success: function(response){
        	
            response.forEach(function(data) {
        	$("#" + data).prop('checked', true);
            })
	    summarize();
      }	
})
}
var summarize = function() {
        var boxes = $('input[type=checkbox]').length;
    var cboxes = $('input[type=checkbox]:checked').length;
    var days = daysUntil(2018,7,1);
    var remaining = (boxes - cboxes);
    
    $('#days').text(days);
    $('#read').text(cboxes);
    $('#remaining').text(remaining);
    $('#chapters').text( Math.ceil(remaining / days));
    var p = cboxes/boxes * 100;
    $('#percent').text(p.toFixed(2));
}

$(document).on("click", "input[type='checkbox']", function () {
    summarize()

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
	    url: '/read/' + this.id,
	    type: 'DELETE',
	    success: function(data) {
		console.log('DELETE was performed for ' + this.id + '.');
	    }
	});
    }


});

$(document).ready(function () {
    $('#days').text(daysUntil(2018,7,1));

    var webAuth = new auth0.WebAuth({
	domain: '2018firm.auth0.com',
	clientID: 'S2peKq6xxBILjv2vu4LPRXR0WywJKm1M',
	redirectUri: 'http://openshift-jee-sample-2018firm.7e14.starter-us-west-2.openshiftapps.com',
	audience: 'https://' + '2018firm.auth0.com' + '/userinfo',
	responseType: 'token id_token',
	scope: 'openid'
    });
    
    var loginBtn = $('#btn-login');
    
    loginBtn.click(function(e) {
	e.preventDefault();
    webAuth.authorize();
    });

    readChapters();
    
    var loginStatus = $('#login-status');
    var loginView = $('#login-view');
    var homeView = $('#home-view');
    
    // buttons and event listeners
    var homeViewBtn = $('#btn-home-view');
    var loginBtn = $('#btn-login');
    var logoutBtn = $('#btn-logout');
    
    homeViewBtn.click(function() {
	homeView.css('display', 'inline-block');
	loginView.css('display', 'none');
    });

    loginBtn.click(function(e) {
	e.preventDefault();
	webAuth.authorize();
    });

    logoutBtn.click(logout);

    function getProfile(accessToken) {
	if (!localStorage.getItem['profile']) {

	    if (!accessToken) {
		console.log('Access token must exist to fetch profile');
	    }
	    
	    webAuth.client.userInfo(accessToken, function(err, profile) {
		if (profile) {
		    console.log(profile);
		    localStorage.setItem('profile', profile);
		}
	    });
	} else {
	    console.log(profile);
	}
    }

    function setSession(authResult, profile) {
	// Set the time that the access token will expire at
	var expiresAt = JSON.stringify(
	    authResult.expiresIn * 1000 + new Date().getTime()
	);
	localStorage.setItem('access_token', authResult.accessToken);
	localStorage.setItem('id_token', authResult.idToken);
	localStorage.setItem('expires_at', expiresAt);
    }

    function logout() {
	// Remove tokens and expiry time from localStorage
	localStorage.removeItem('access_token');
	localStorage.removeItem('id_token');
	localStorage.removeItem('expires_at');
	localStorage.removeItem('profile');
	displayButtons();
    }

    function isAuthenticated() {
	// Check whether the current time is past the
	// access token's expiry time
	var expiresAt = JSON.parse(localStorage.getItem('expires_at'));
	return new Date().getTime() < expiresAt;
    }
    
    function handleAuthentication() {
	webAuth.parseHash(function(err, authResult) {
	    if (authResult && authResult.accessToken && authResult.idToken) {
		window.location.hash = '';
		getProfile(authResult.accessToken);
		setSession(authResult);
		loginBtn.css('display', 'none');
		homeView.css('display', 'inline-block');
	    } else if (err) {
		homeView.css('display', 'inline-block');
		console.log(err);
		alert(
		    'Error: ' + err.error + '. Check the console for further details.'
		);
	    }
	    displayButtons();
	});
    }

    function displayButtons() {
	if (isAuthenticated()) {
	    loginBtn.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    logoutBtn.css('display', 'inline-block');
	    loginStatus.text('You are logged in!');
	} else {
	    loginBtn.css('display', 'inline-block');
	    logoutBtn.css('display', 'none');
	    homeView.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    loginStatus.text('You are not logged in! Please log in to continue.');
	}
    }

    handleAuthentication();

});
