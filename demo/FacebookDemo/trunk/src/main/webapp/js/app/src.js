dojo.provide("app.src");

dojo.require("dojo.parser");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Slider");
dojo.require("dojox.form.DropDownSelect");
dojo.require("dojox.grid.Grid");
dojo.require("org.ppwcode.dojo.dijit.form.PpwMasterView");
dojo.require("org.ppwcode.dojo.dojox.grid.data.model");
dojo.require("org.ppwcode.dojo.dijit.form.PpwObjectForm");
dojo.require("org.ppwcode.dojo.dijit.form.PpwCrudForm");
dojo.require("org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr");
dojo.require("org.ppwcode.dojo.dijit.form.PpwImageBox");

var fbfriendsgridview = {
        cells: [
          [
            {name: "First Name", field: "first_name", width: "8em"},
            {name: "Last Name", field: "last_name", width: "100%"}
          ]
        ]
};
var fbfriendsgridlayout = [ fbfriendsgridview ];
var fbfriendsgridmodel = new org.ppwcode.dojo.dojox.grid.data.PpwObjects();


var yourmovielistgridlayout = [
	{
		cells: [[
		         {name: "Title", field: "title", width:"100%"}
		        ]]	
	}
];
var yourmovielistgridmodel = new org.ppwcode.dojo.dojox.grid.data.PpwObjects();


function formatRating(therating) {
	var result = "";
	var i = 0;
	for (i = 0; i < therating; i++) {
		result += "<img src='image/str_full_lg.gif'></img>";
	}
	for (i = 0; i < 5 - therating; i++) {
		result += "<img src='image/str_none_lg.gif'></img>";
	}
	return result;
}


function formatMediaType(value) {
	var mediaTypeMap = {
			OTHER: "Other",
			BLURAY: "Blu-Ray",
			DVD: "DVD",
			DIVX_XVID: "DivX/XviD"
	}
	return mediaTypeMap[value];
}

var fbfriendsmoviesgridlayout = [
  {
    cells: [[
      {name: "Title", field: "title", width:"30em"},
      {name: "Rating", field: "rating", width: "100px", formatter: formatRating},
      {name: "Media type", field: "mediaType", width: "6em", formatter: formatMediaType}
    ]]	
  }
];

var fbfriendsmoviesgridmodel = new org.ppwcode.dojo.dojox.grid.data.PpwObjects();


var fbFriendFormMap = [
  { property: "pic", fieldid: "FbFriendPicture", isEditable: false},
  { property: "first_name", fieldid: "FbFriendFirstName", isEditable: false},
  { property: "last_name", fieldid: "FbFriendLastName", isEditable: false}
];

var yourMovieFormMap = [
  { property: "persistenceId", fieldid: "yourMovieIdHiddenBox", isId: true},
  { property: "persistenceVersion", fieldid: "yourMoviePersistenceVersionHiddenBox"},
  { property: "facebookUserId", fieldid: "yourFacebookUserIdHiddenBox"},
  { property: "title", fieldid: "yourMovieTitle"},
  { property: "mediaType", fieldid: "yourMovieMediaType" },
  { property: "rating", fieldid: "yourMovieRating" }
];
                         
function retrieve_movies(fbuid) {
	console.log("getting movies for user " + fbuid);
	JpaMovieDaoWrapper.findByFacebookUser(fbuid,
		{
			callback: function(response) {
				console.log (response.length + " movies for user " + fbuid);
				lstYourMovies.setData(response);
			},	
			errorHandler: function(errorString, exception) {
				console.error("could not get movies for user " + fbuid + ": " + errorString);
			}
		}
	);
}

var yourMovieViewFormController;

function initializeDwrControllers() {
	yourMovieViewFormController = new org.ppwcode.dojo.dijit.form.PpwViewFormControllerDwr();
	yourMovieViewFormController.dwrRetrieveFunction = JpaMovieDaoWrapper.findByFacebookUser;
    yourMovieViewFormController.dwrCreateFunction = JpaAtomicStatelessCrudDaoWrapper.createPersistentBean;
    yourMovieViewFormController.dwrUpdateFunction = JpaAtomicStatelessCrudDaoWrapper.updatePersistentBean;
    yourMovieViewFormController.configure(lstYourMovies, frmYourMovie);
    
    yourMovieViewFormController.beforeItemCreate = function(item) {
    	item.facebookUserId = FB.Connect.get_loggedInUser();
    }
}

function show_user() {
   var node = document.getElementById("userbox");
   node.innerHTML = 
         "<table>"
       + "  <tbody><tr>"
       + "    <td rowspan='2'>"
       + "      <fb:profile-pic uid='loggedinuser' facebook-logo='true'></fb:profile-pic>"
       + "    </td><td style='vertical-align: top'>"
       + "      <span>Welcome, <fb:name uid='loggedinuser' useyou='false'></fb:name>.  You are signed in with"
       + "      your facebook account.</span>"
       + "    </td>"
       + "  </tr><tr>"
       + "    <td>"
       + "      <button dojoType='dijit.form.Button' onclick='do_logout();'>logout</button>"
       + "    </td>"
       + "  </tr></tbody>"
       + "</table>"
   FB.XFBML.Host.parseDomTree();
   dojo.parser.parse(node);
}

function retrieve_friends() {
    FB.Facebook.apiClient.friends_get(null, function(result, ex) {
            FB.Facebook.apiClient.users_getInfo(result, 
                                                ["pic", "first_name", "last_name"], 
                                                function(result, ex) {
            fbfriendsgridmodel.setData(result);
        });
      });
}

function do_login() {
	console.log("doing login routine");
	show_user();
	retrieve_friends();
	yourMovieViewFormController.setDwrRetrieveFunctionParameters([FB.Connect.get_loggedInUser()]); 
	yourMovieViewFormController.fillMasterView();
}


function do_logout() {
	console.log("doing logout routine");
    fbfriendsgridmodel.clear();
    fbFriendForm.reset();
    lstYourMovies.setData(null);
    fbfriendsmoviesgridmodel.setData(null);
    FB.Connect.logout(function() {
        var node = document.getElementById("userbox");
        node.innerHTML = "<fb:login-button onlogin='show_user();'></fb:login-button>";
        FB.XFBML.Host.parseDomTree();
    });
}

function showall() {
	setTimeout(function() {
			dojo.fadeOut({node: 'splashscreen', duration: 150}).play();
			setTimeout(function() {dojo.query("#splashscreen").orphan();}, 250);
		},
		20
	);
}

dojo.addOnLoad(function() {
	dojo.parser.parse();
	console.info("done parsing");
	fbFriendForm.setFormMap(fbFriendFormMap);
	frmYourMovie.setFormMap(yourMovieFormMap);
	initializeDwrControllers();

	FB.ensureInit(function() {
		console.log("before ifUserConnected");
		FB.Connect.ifUserConnected(do_login, do_logout);
		console.log("after ifUserConnected");
		showall();
	});  
});
