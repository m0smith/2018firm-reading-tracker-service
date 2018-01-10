function daysUntil(year, month, day) {
  var now = new Date(),
      dateEnd = new Date(year, month - 1, day), // months are zero-based
      days = (dateEnd - now) / 1000/60/60/24;   // convert milliseconds to days

  return Math.round(days);
}

var apiHeaders = function(secured) {
    var accessToken = localStorage.getItem('access_token');

    var headers;
    if (secured && accessToken) {
	headers = { Authorization: 'Bearer ' + accessToken };
    }
    return headers;
}

var readChapters = function() {
    $.ajax({
        url: "/read",
        type: "GET",
        datatype:"JSON",
        contentType: "application/json",
	headers: apiHeaders(true),
         
        error : function(data){ console.log("error:",data) },
        success: function(response){
        	
            response.forEach(function(data) {
		var ele = $("#" + data)
        	ele.prop('checked', true);
		ele.parent('label').addClass('active');
            })
	    summarize();
      }	
})
}
var summarize = function() {
    console.log("summarize")
    var boxes = $('input[type=checkbox]').length;
    var cboxes = $('input[type=checkbox]:checked').length;
    var days = daysUntil(2018,7,1);
    var remaining = (boxes - cboxes);
    var p = cboxes/boxes * 100;
    var p_display = p.toFixed(1) + '%'; 
    
    $('#days').text(days);
    $('#read').text(cboxes);
    $('#remaining').text(remaining);
    $('#chapters').text( Math.ceil(remaining / days));
    
    $('#percent').text(p.toFixed(2));
    $('#personal-progress-bar span').text(p_display);
    $('#personal-progress-bar').attr('aria-valuenow', p_display).css('width',p_display);
}


// $("").on("click", function () {
//      $('input[type="checkbox"]').bind('change', function (v) {

//     }


//      })});


var submitRegistration = function(){
    console.log("POST");

    $("#registration-view").css('display', 'none');
    console.log($("#registration-form").serialize());
    $.ajax({
	url: '/reg',
	type: 'POST',
	contentType : 'application/x-www-form-urlencoded',
	data: $("#registration-form").serialize(),
	headers: apiHeaders(true),
	success: function(data) {
	    console.log('POST was performed for ' + this + '.');
	}
    });
}

$(document).ready(function () {

    $('#attendee-group-youth').change( function(ele) {
	console.log("YOUTH");
	$("#ward-select-view").css('display', 'inline-block');
	$("#registration-submit").removeAttr("disabled").removeClass("disabled").addClass("btn-success");
    });
    $('.attendee-group-non-youth').change( function(ele) {
	console.log("NON YOUTH");
	$("#ward-select-view").css('display', 'none');
	$("#registration-submit").removeAttr("disabled").removeClass("disabled").addClass("btn-success");
    });

    $('#registration-submit').on('click', function() {
	submitRegistration();
    });
    
    $('.chapter-cb').change( function(ele) {
	console.log(this.checked);
	summarize();

	if(this.checked) {
	    $.ajax({
		url: '/read',
		type: 'PUT',
		data: "chapter=" + this.id,
		headers: apiHeaders(true),
		success: function(data) {
		    console.log('PUT was performed.' + data);
		}
	    });
	} else {
	    $.ajax({
		url: '/read/' + this.id,
		type: 'DELETE',
		headers: apiHeaders(true),
		success: function(data) {
		    console.log('DELETE was performed for ' + this.id + '.');
		}
	    });
	}});
    

    
    $('#days').text(daysUntil(2018,7,1));

    var webAuth = new auth0.WebAuth({
	domain: '2018firm.auth0.com',
	clientID: 'S2peKq6xxBILjv2vu4LPRXR0WywJKm1M',
	redirectUri: 'http://openshift-jee-sample-2018firm.7e14.starter-us-west-2.openshiftapps.com',
	audience: 'http://reading.2018firm.life',
	responseType: 'token id_token',
	scope: 'openid profile read:chapter write:chapter read:user write:user'
    });
    
    var loginBtn = $('#btn-login');
    
    loginBtn.click(function(e) {
	e.preventDefault();
	webAuth.authorize();
    });

    var loginStatus = $('#login-status');
    var loginStatusText = $('#login-status-text');
    var logoutStatus = $('#logout-status');
    var loginView = $('#login-view');
    var homeView = $('#home-view');
    var summaryView = $('#summary-view');
    var registrationView = $('#registration-view');
    var userPhoto = $( '#user-photo' );
    
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
		    loginStatusText.text(profile.name);
		    userPhoto.attr('src', profile.picture);
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

    function lookForRegistration() {
	$.ajax({
	    url: '/reg',
	    type: 'GET',
	    data: "chapter=" + this.id,
	    headers: apiHeaders(true),
	    success: function(data) {
		console.log('REG read: ' + data);
	    }
	});
    }
    
    function handleAuthentication() {
	webAuth.parseHash(function(err, authResult) {
	    if (authResult && authResult.accessToken && authResult.idToken) {
		window.location.hash = '';
		getProfile(authResult.accessToken);
		setSession(authResult);
		lookForRegistration();
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
	    readChapters();
	    loginBtn.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    homeView.css('display', 'inline-block');
	    summaryView.css('display', 'inline-block');
	    logoutBtn.css('display', 'inline-block');
	    loginStatus.css('display', 'inline-block');
	    logoutStatus.css('display', 'none');
	} else {
	    loginBtn.css('display', 'inline-block');
	    logoutBtn.css('display', 'none');
	    homeView.css('display', 'none');
	    summaryView.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    logoutStatus.css('display', 'inline-block');
	    loginStatus.css('display', 'none');
	}
    }

    handleAuthentication();

});
