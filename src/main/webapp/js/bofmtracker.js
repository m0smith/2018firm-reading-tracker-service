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
         
        error: function(data){
	    console.log("error:",data);
	    $('#error-view').text("Failed to load your read chapters.  Please try again later.");
	},
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

var currentTally = function() {
    $.ajax({
        url: "/tally",
        type: "GET",
        datatype:"JSON",
        contentType: "application/json",
	headers: apiHeaders(true),
        
        error : function(data){
	    console.log("error:",data)
	    $('#error-view').text("Stake tally not loaded");
	},
        success: function(response){
            
            response.forEach(function(data) {
		var ward = data[0];
		var user_count = data[1];
		var total_chapters_read = data[2];

		var p = total_chapters_read / (user_count * 239) * 100;
		var p_display = p.toFixed(1) + '%';

		$("#"+ward+"-progress-bar").css("width", p_display);
		$("#"+ward+"-progress-bar span").text( p_display);
            })
	}	
    })
};

var summarize = function() {
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



$(document).ready(function () {


    $('#attendee-group-youth').change( function(ele) {
	$("#ward-select-view").css('display', 'inline-block');
	$("#registration-submit").removeAttr("disabled").removeClass("disabled").addClass("btn-success");
    });
    $('.attendee-group-non-youth').change( function(ele) {
	$("#ward-select-view").css('display', 'none');
	$("#registration-submit").removeAttr("disabled").removeClass("disabled").addClass("btn-success");
    });

    $('#registration-submit').on('click', function() {
	submitRegistration();
    });
    
    $('.chapter-cb').change( function(ev) {
	summarize();
	$("#"+ev.currentTarget.id).parent().addClass("changing");
	if(this.checked) {
	    $.ajax({
		url: '/read',
		type: 'PUT',
		data: "chapter=" + this.id,
		context: ev.currentTarget,
		headers: apiHeaders(true),
		success: function(data) {
		    $("#"+this.id).parent().removeClass("changing".removeClass("changed").removeClass("failed");
		    console.log('PUT was performed.' + data);
		},
		error: function(data) {
		    $("#"+this.id).parent().removeClass("changing").addClass("failed").removeClass("changed");
		    $('#error-view').text("Mark not saved.  Please try again later.");
		}
	    });
	} else {
	    $.ajax({
		url: '/read/' + this.id,
		type: 'DELETE',
		context: ev.currentTarget,
		headers: apiHeaders(true),
		success: function(data) {
		    console.log('DELETE was performed for ' + this.id + '.');
		    $("#"+this.id).parent().removeClass("changing").addClass("changed").removeClass("failed");

		},
		error: function(data) {
		    $("#"+this.id).parent().removeClass("changing").addClass("failed").removeClass("changed");
		    $('#error-view').text("Mark not removed.  Please try again later.");
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
    var logoutStatusText = $('#logout-status-text');
    var logoutStatus = $('#logout-status');
    var loginView = $('#login-view');
    var homeView = $('#home-view');
    var tallyView = $('#tally-view');
    var summaryView = $('#summary-view');
    var registrationView = $('#registration-view');
    var userPhoto = $( '#user-photo' );
    
    // buttons and event listeners
    var homeViewBtn = $('#btn-home-view');
    var tallyViewBtn = $('#btn-tally-view');
    var loginBtn = $('#btn-login');
    var logoutBtn = $('#btn-logout');
    
    homeViewBtn.click(function() {
	homeView.css('display', 'inline-block');
	summaryView.css('display', 'inline-block');
	tallyViewBtn.css('display', 'inline-block');
	tallyView.css('display', 'none');
	loginView.css('display', 'none');
    });

    tallyViewBtn.click(function() {
	tallyView.css('display', 'inline-block');
	homeViewBtn.css('display', 'inline-block');
	loginView.css('display', 'none');
	summaryView.css('display', 'none');
	homeView.css('display', 'none');
    });

    loginBtn.click(function(e) {
	e.preventDefault();
	webAuth.authorize();
    });

    logoutBtn.click(logout);
    
    var submitRegistration = function(){
	
	$("#registration-view").css('display', 'none');
	$.ajax({
	    url: '/reg',
	    type: 'POST',
	    contentType : 'application/x-www-form-urlencoded',
	    data: $("#registration-form").serialize(),
	    headers: apiHeaders(true),
	    success: function(data) {
		registrationView.css('display', 'none');
		displayButtons();
	    }
	});
}


    function getProfile(accessToken) {
	if (!localStorage.getItem['profile']) {

	    if (!accessToken) {
		console.log('Access token must exist to fetch profile');
	    }
	    
	    webAuth.client.userInfo(accessToken, function(err, profile) {
		if (profile) {
		    localStorage.setItem('profile', profile);
		    logoutStatusText.text(profile.name);
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
	loginBtn.css('display', 'inline-block');
	logoutBtn.css('display', 'none');	
	loginStatus.css('display', 'inline-block');
	logoutStatus.css('display', 'none');
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
		if( data != true){
		    logoutStatus.css('display', 'none');
		    registrationView.css('display', 'inline-block');
		} else {
		    displayButtons();
		}

	    }
	});
    }
    
    function handleAuthentication() {
	webAuth.parseHash(function(err, authResult) {
	    if (authResult && authResult.accessToken && authResult.idToken) {
		window.location.hash = '';
		getProfile(authResult.accessToken);
		setSession(authResult);
		loginBtn.css('display', 'none');
		logoutBtn.css('display', 'inline-block');
		loginStatus.css('display', 'none');
		logoutStatus.css('display', 'inline-block');
		summaryView.css('display', 'inline-block');
		readChapters();
		currentTally();
		lookForRegistration();
	    } else if (err) {
		homeView.css('display', 'inline-block');
		$('#error-view').text("Login error:" + err + " Please try again later.");
		console.log(err);
		displayButtons();
		
	    } else {
		loginBtn.css('display', 'inline-block');
		loginStatus.css('display', 'inline-block');
	    }
		
	});
    }

    function displayButtons() {
	if (isAuthenticated()) {
	    loginBtn.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    tallyViewBtn.css('display', 'inline-block');
	    homeView.css('display', 'inline-block');
	    summaryView.css('display', 'inline-block');
	    logoutBtn.css('display', 'inline-block');
	    loginBtn.css('display', 'none');
	    loginStatus.css('display', 'none');
	    logoutStatus.css('display', 'inline-block');
	} else {
	    loginBtn.css('display', 'inline-block');
	    logoutBtn.css('display', 'none');
	    homeView.css('display', 'none');
	    tallyView.css('display', 'none');
	    summaryView.css('display', 'none');
	    homeViewBtn.css('display', 'none');
	    tallyViewBtn.css('display', 'none');
	    loginStatus.css('display', 'inline-block');
	    logoutStatus.css('display', 'none');
	}
    }

    function startup() {
	loginBtn.css('display', 'none');
	logoutBtn.css('display', 'none');
	homeView.css('display', 'none');
	tallyView.css('display', 'none');
	summaryView.css('display', 'none');
	homeViewBtn.css('display', 'none');
	tallyViewBtn.css('display', 'none');
	logoutStatus.css('display', 'none');
	loginStatus.css('display', 'none');
	
	handleAuthentication();
    }
    
    startup();
});
