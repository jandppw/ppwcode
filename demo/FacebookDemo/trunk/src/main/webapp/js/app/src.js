dojo.provide("app.src");

dojo.require("dojo.fx");
dojo.require("dojox.fx.easing");
dojo.require("dojo.parser");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Slider");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dojox.form.DropDownSelect");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.data.QueryReadStore");
dojo.require("org.ppwcode.dojo.dojo.data.ObjectArrayStore");
dojo.require("org.ppwcode.dojo.dijit.form.MasterView");
dojo.require("org.ppwcode.dojo.dijit.form.ObjectForm");
dojo.require("org.ppwcode.dojo.dijit.form.CrudForm");
dojo.require("org.ppwcode.dojo.dijit.form.ViewFormControllerDwr");
dojo.require("org.ppwcode.dojo.dijit.form.ImageBox");

var fbfriendsgridview = {
		cells: [
		        [
		         {name: "First Name", field: "first_name", width: "8em"},
		         {name: "Last Name", field: "last_name", width: "auto"}
		         ]
		         ]
};
var fbfriendsgridlayout = [ fbfriendsgridview ];

var yourmovielistgridlayout = [
                               {
                            	   cells: [[
                            	            {name: "Title", field: "title", width: "auto"}
                            	            ]]	
                               }
                               ];


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
	};
	return mediaTypeMap[value];
}

var fbfriendsmoviesgridlayout = [
                                 {
                                	 cells: [[
                                	          {name: "Title", field: "title", width: "auto"},
                                	          {name: "Rating", field: "rating", width: "100px", formatter: formatRating},
                                	          {name: "Media type", field: "mediaType", width: "6em", formatter: formatMediaType}
                                	          ]]	
                                 }
                                 ];



var fbFriendFormMap = [
                       { property: "pic", fieldid: "FbFriendPicture", isEditable: false},
                       { property: "first_name", fieldid: "FbFriendFirstName", isEditable: false},
                       { property: "last_name", fieldid: "FbFriendLastName", isEditable: false}
                       ];

var yourMovieFormMap = [
                        { property: "persistenceId", fieldid: "yourMovieIdHiddenBox", isId: true},
                        { property: "persistenceVersion", fieldid: "yourMoviePersistenceVersionHiddenBox"},
                        { property: "facebookUserId", fieldid: "yourFacebookUserIdHiddenBox"},
                        { property: "id", fieldid: "yourMovieId"},
                        { property: "mediaType", fieldid: "yourMovieMediaType" },
                        { property: "rating", fieldid: "yourMovieRating" }
                        ];

function retrieve_movies(fbuid) {
	//console.log("getting movies for user " + fbuid);
	JpaMovieDaoWrapper.findByFacebookUser(fbuid,
			{
		callback: function(response) {
		//console.log (response.length + " movies for user " + fbuid);
		lstYourMovies.setData(response);
	},	
	errorHandler: function(errorString, exception) {
		//console.error("could not get movies for user " + fbuid + ": " + errorString);
	}
			}
	);
}

var yourMovieViewFormController;

function initializeGrids() {
	fbFriendsGrid.selection.setMode("single");
}

function initializeDwrControllers() {
	yourMovieViewFormController = new org.ppwcode.dojo.dijit.form.ViewFormControllerDwr();
	yourMovieViewFormController.dwrRetrieveFunction = JpaMovieDaoWrapper.findByFacebookUser;
	yourMovieViewFormController.dwrCreateFunction = JpaAtomicStatelessCrudDaoWrapper.createPersistentBean;
	yourMovieViewFormController.dwrUpdateFunction = JpaAtomicStatelessCrudDaoWrapper.updatePersistentBean;
	yourMovieViewFormController.configure(lstYourMovies, frmYourMovie);

	yourMovieViewFormController.beforeItemCreate = function(item) {
		item.facebookUserId = FB.Connect.get_loggedInUser();
	};
}

function show_user() {
	var node = document.getElementById("userbox");
	node.innerHTML = 
		"<table>" +
		"  <tbody><tr>" +
		"    <td rowspan='2'>" +
		"      <fb:profile-pic uid='loggedinuser' facebook-logo='true'></fb:profile-pic>" +
		"    </td><td style='vertical-align: top'>" +
		"      <span>Welcome, <fb:name uid='loggedinuser' useyou='false'></fb:name>.  You are signed in with" +
		"      your facebook account.</span>" +
		"    </td>" +
		"  </tr><tr>" +
		"    <td>" +
		"      <button dojoType='dijit.form.Button' onclick='do_logout();'>logout</button>" +
		"    </td>" +
		"  </tr></tbody>" +
		"</table>";
	FB.XFBML.Host.parseDomTree();
	dojo.parser.parse(node);
}

function retrieve_friends() {
	FB.Facebook.apiClient.friends_get(null, function(result, ex) {
		FB.Facebook.apiClient.users_getInfo(result, 
				["pic", "first_name", "last_name"], 
				function(result, ex) {
			var data = result ? result : [];
			var fbfriendsgriddatastore = new org.ppwcode.dojo.dojo.data.ObjectArrayStore({data: data});
			fbFriendsGrid.setStore(fbfriendsgriddatastore);
		});
	});
}

function do_login() {
	//console.log("doing login routine");
	show_user();
	retrieve_friends();
	yourMovieViewFormController.setDwrRetrieveFunctionParameters([FB.Connect.get_loggedInUser()]); 
	yourMovieViewFormController.fillMasterView();
	lstYourMovies.disableButtons(false);
}


function do_logout() {
	//console.log("doing logout routine");
	lstYourMovies.disableButtons(true);
	fbFriendsGrid.setStore(null);
	fbFriendForm.reset();
	lstYourMovies.clear();
	lstFriendsMoviesGrid.setStore(null);
	FB.Connect.logout(function() {
		var node = document.getElementById("userbox");
		node.innerHTML = "<fb:login-button onlogin='show_user();'></fb:login-button>";
		FB.XFBML.Host.parseDomTree();
	});
}

function showall() {
	setTimeout(function() {
		dojo.fadeOut({node: 'splashscreen', duration: 1000, onEnd: function() { dojo.query("#splashscreen").orphan(); }}).play();
		createHideMovieAnimation().play();
		dojo.fadeOut({node: 'fbFriendContentPane', duration: 10}).play();
	},
	20
	);
}

function createShowMovieDetailsAnimation() {
	return dojo.fx.combine([
	                        dojo.fadeIn({node: "yourMoviePicture", duration: 600 }),
	                        dojo.fadeIn({node: "yourMovieDescription", duration: 600 }),
	                        dojo.fadeIn({node: "yourMovieActors", duration: 600 })
	                        ]);
}

function createHideMovieDetailsAnimation() {
	return dojo.fx.combine([
	                        dojo.fadeOut({node: "yourMoviePicture", duration: 400 }),
	                        dojo.fadeOut({node: "yourMovieDescription", duration: 400 }),
	                        dojo.fadeOut({node: "yourMovieActors", duration: 400 })
	                        ]);
}

function createShowFrmYourMovieAnimation() {
	return dojo.fx.slideTo({node: "frmYourMovie", left: (0).toString(), unit: "px"});
}

function createHideFrmYourMovieAnimation() {
	return dojo.fx.slideTo({node: "frmYourMovie", left: (-400).toString(), unit: "px"});
}

function createShowMovieAnimation() {
	return dojo.fx.combine([
	                        createShowMovieDetailsAnimation(),
	                        createShowFrmYourMovieAnimation()
	                        ]);
}

function createHideMovieAnimation() {
	return dojo.fx.combine([
	                        createHideMovieDetailsAnimation(),
	                        createHideFrmYourMovieAnimation()
	                        ]);
}


function showFriendDetails(user) {

	/* dojo.fx.chain is buggy: http://www.dojotoolkit.org/forum/dojo-core-dojo-0-9/dojo-core-support/dojo-fx-chain-causing-multiple-errors
    dojo.fx.chain([
	      dojo.fx.combine([
	          dojo.fadeOut({node: fbFriendForm.id, duration: 250, onEnd: function() { fbFriendForm.displayItem(user) } }),
	          dojo.fx.slideTo({ node: "fbFriendContentPane", left: (1500).toString(), unit: "px", duration: 500 })
	      ]),
		  dojo.fx.combine([
              dojo.fadeIn({node: fbFriendForm.id, duration: 1500}),
              dojo.fx.slideTo({ node: "fbFriendContentPane", left: (0).toString(), unit: "px", duration: 1000, easing: dojox.fx.easing.bounceOut })
		  ])

	]).play();
	 */

	var slideOut = dojo.fx.combine([
	                                dojo.fadeOut({node: "fbFriendContentPane", duration: 250, onEnd: function() { fbFriendForm.displayItem(user); } }),
	                                dojo.fx.slideTo({ node: "fbFriendContentPane", left: (1500).toString(), unit: "px", duration: 500 })
	                                ]);
	var slideIn = dojo.fx.combine([
	                               dojo.fadeIn({node: "fbFriendContentPane", duration: 1500}),
	                               dojo.fx.slideTo({ node: "fbFriendContentPane", left: (0).toString(), unit: "px", duration: 1000, easing: dojox.fx.easing.bounceOut })
	                               ]);
	dojo.connect(slideOut, "onEnd", slideIn, 'play');
	slideOut.play();

	//console.dir(user);
	JpaMovieDaoWrapper.findByFacebookUser(user.uid,
			{
		callback: function(response) {
		lstFriendsMoviesGrid.setStore(new org.ppwcode.dojo.dojo.data.ObjectArrayStore({data: response}));
	},
	errorHandler: function(errorString, ex) {
		//console.error(errorString);
	}
			}
	);

}

function updateMovieDetails(movieId) {

	TheOpenMovieDBUtilWrapper.getPosterThumb(movieId, function(url) {
		yourMoviePicture.setValue(url);
	});

	TheOpenMovieDBUtilWrapper.getShortOverview(movieId, function(description) {
		dojo.byId("yourMovieDescription").innerHTML = "<p>" + description + "</p>";
	});

	TheOpenMovieDBUtilWrapper.getMainActors(movieId, function(actors) {
		var actorsHTML = "";
		if (actors.length > 0) {
			actorsHTML = "<b>Main Actors</b><br/>";
		}
		for (var i = 0; i < actors.length; i++) {
			actorsHTML += actors[i] + "<br/>";
		}
		dojo.byId("yourMovieActors").innerHTML = "<p>" + actorsHTML + "</p>";
	});
}


function lstYourMoviesAddButtonClick() {
	var newProps = { onEnd: function() {
		yourMoviePicture.setValue("image/blank.gif");
		dojo.byId("yourMovieDescription").innerHTML = "<p></p>";
		dojo.byId("yourMovieActors").innerHTML = "<p></p>";
		createShowMovieAnimation().play();
	} };
	dojo.mixin(createHideMovieAnimation(), newProps).play();
}

function lstYourMoviesGridRowClick(e) {
	var movie = lstYourMovies.getSelectedItem();
	var newProps = { onEnd: function() {
		updateMovieDetails(movie.id);
		createShowMovieAnimation().play();
	} };
	dojo.mixin(createHideMovieAnimation(), newProps).play();
}

function frmYourMovieCreateModeCancelButtonClick() {
	createHideMovieAnimation().play();
}

function frmYourMovieCreateModeSaveButtonClick(e) {
	var movieId = e.formObject.id;
	updateMovieDetails(movieId);
}

function frmYourMovieUpdateModeSaveButtonClick(e) {
	var movieId = e.formObject.id;
	updateMovieDetails(movieId);
}

function fbFriendsGridRowClick(e) {
	var user = fbFriendsGrid.selection.getFirstSelected();
	showFriendDetails(user);
}

function frmYourMovieReset(e) {
	// Hack to work around the problem of resetting a FilteringSelect:
		// value isn't cleared, invalid-state isn't reset
		// Should be resolved in dojo 1.2, although I'm not sure that patch
		// covers our use case
		var d = dijit.byId("yourMovieId");
d.valueNode.value = ""; 
dijit.form.FilteringSelect.superclass.setValue.call(d, "", true, ""); 
d._lastDisplayedValue = ""; 
d.state = ""; 
d._setStateClass(); 	
}

dojo.addOnLoad(function() {
	dojo.config.usePlainJson=true;
	dojo.parser.parse();
	//console.info("done parsing");
	fbFriendForm.setFormMap(fbFriendFormMap);
	frmYourMovie.setFormMap(yourMovieFormMap);
	initializeGrids();
	initializeDwrControllers();

	dojo.connect(lstYourMovies,"onCreateButtonClick", lstYourMoviesAddButtonClick);
	dojo.connect(lstYourMovies,"onGridRowClick", lstYourMoviesGridRowClick);

	dojo.connect(frmYourMovie, "onCreateModeCancelButtonClick", frmYourMovieCreateModeCancelButtonClick);
	dojo.connect(frmYourMovie, "onCreateModeSaveButtonClick", frmYourMovieCreateModeSaveButtonClick);
	dojo.connect(frmYourMovie, "onUpdateModeSaveButtonClick", frmYourMovieUpdateModeSaveButtonClick);
	dojo.connect(frmYourMovie, "reset", frmYourMovieReset);

	dojo.connect(fbFriendsGrid, "onRowClick", fbFriendsGridRowClick);

	FB.ensureInit(function() {
		//console.log("before ifUserConnected");
		FB.Connect.ifUserConnected(do_login, do_logout);
		//console.log("after ifUserConnected");
		showall();
	});  
});
