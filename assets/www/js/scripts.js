/* SporttiGalaksi GameController */

 (function($) {

    //==============================================================================
    // VARIABLES = Union & PhoneGap & GameController
    //==============================================================================
    // user object for user vars
    var User = {};

    // union vars
    var orbiter;
    var msgManager;
    var connManager;
    var UPC;
    var roomID;
    var client;
    var clientID;
    var player;
    var gameState;

    // acceleration vars
    var oldx = 0;
    var oldy = 0;
    var oldz = 0;

    // the watch id references the current `watchAcceleration`
    var watchID = null;

    //==============================================================================
    // FORM ACTIONS
    //==============================================================================
    $(document).bind('pagebeforechange',
    function(event, data) {
        // prevent pagechange when form is submitted
        if (typeof data.toPage === "string") {
            event.preventDefault();
        }
    });

    // submit form
    $('button').live('tap',
    function(event) {

        // get form values
        var $form = $(event.target).closest('form'),
        $url = $form.attr('action'),
        $action = $(event.target).val();

        // check that every field is filled
        var error = 0;
        $('input').each(function(index) {
            if (!$(this).val().length) {
                $(this).addClass('error');
                error = 1;
            } else {
                $(this).removeClass('error');
            }
        });

        // if error give message and return
        if (error) {
            //
            $('#progress').empty().append('<li data-theme="e" data-role="content">Liittyäksesi peliin täytä kaikki kentät</div>');
            $('#progress').listview('refresh').bind('tap',
            function(event) {
                $(this).empty();
            });
            return false;
        }

        // clear possible message from progress elements
        $('#progress').empty();

        if ($action == 'join') {
            // call join func
            join($url, $form);
        } else if ($action == 'leave') {
            // if action is leave > call the general leave() action
            leave();
        }

        // returning something
        return false;
    });

    //==============================================================================
    // INITIALIZATION
    //==============================================================================
    // Wait for PhoneGap to load function onLoad() { document.addEventListener("deviceready", onDeviceReady, false); }
    // PhoneGap is ready function onDeviceReady() { init(); }
    function init() {
        //
        roomID = $('#roomid').val();
        // Create Orbiter object
        orbiter = new net.user1.orbiter.Orbiter();
        // Register for connection events
        orbiter.addEventListener(net.user1.orbiter.OrbiterEvent.READY, readyListener, this);
        orbiter.addEventListener(net.user1.orbiter.OrbiterEvent.CLOSE, closeListener, this);
        // Register for incoming messages from Union
        msgManager = orbiter.getMessageManager();
        // Connect to Union
        orbiter.connect("socket.dreamschool.fi", 443);
    }

    //==============================================================================
    // ORBITER EVENT LISTENERS
    //==============================================================================
    // Triggered when the connection is ready
    function readyListener(e) {
        //
        $('#progress').empty();
        //
        UPC = net.user1.orbiter.UPC;
        // listeners
        msgManager.addMessageListener(UPC.JOINED_ROOM, joinedRoomListener, this);
        msgManager.addMessageListener("STATE_MESSAGE", stateListener, this, [roomID]);
        msgManager.addMessageListener(UPC.JOIN_ROOM_RESULT, joinRoomResultListener, this);
        //
        clientID = orbiter.getClientID();
        // Join the game room
        msgManager.sendUPC(UPC.JOIN_ROOM, roomID);
        //
        $.mobile.hidePageLoadingMsg();
    }

    // Triggered when the connection is closed
    function closeListener(e) {
        //
        $('#progress').empty();
        //
        leave();
    }

    // Triggered when the user has joined the room
    function joinRoomResultListener(roomID, status) {
        var err = 0,
        msg = "";

        switch (status)
        {
        case "ROOM_NOT_FOUND":
            msg = "Pelihuonetta ei löytynyt";
            err = 1;
            break;
        case "ERROR":
            msg = "Palvelimella tapahtui virhe";
            err = 1;
            break;
        case "ROOM_FULL":
            msg = "Pelihuone on täynnä";
            err = 1;
            break;
        case "ALREADY_IN_ROOM":
            msg = "Olet jo pelihuoneessa";
            err = 1;
            break;
        }

        if (err) {
            $('#progress').empty().append('<li data-theme="e" data-role="content">' + msg + '</div>');
            $('#progress').listview('refresh').bind('tap',
            function(event) {
                $(this).empty();
            });
        }

        return;
    }

    //==============================================================================
    //  ROOM EVENT LISTENER
    //==============================================================================
    // Triggered when a JOINED_ROOM message is received
    function joinedRoomListener() {
        // set user´s information
        var userinfo = User.user_id + ';' + User.username + ';' + User.nickname;

        // send user´s information
        msgManager.sendUPC(UPC.SET_CLIENT_ATTR, orbiter.getClientID(), "", "USERINFO", userinfo, roomID, "4");

        // disable inputs
        $('input').textinput('disable');

        // buttons
        $lBtn = $('<button type="submit" name="submit" data-theme="e" value="leave">Poistu pelistä</button>');

        // add button and init it
        $row = $('.form-button-row').empty();
        $lBtn.appendTo($row);
        $lBtn.button();
    }

    //==============================================================================
    // GAME STATE LISTENER
    //==============================================================================
    function stateListener(fromGame, stateMsg) {
        if (stateMsg == "play") {
            gameState = "play";
            startWatch();
        } else {
            gameState = "pause";
            // stopWatch();
        }
    }

    //==============================================================================
    // ACCELERATION STUFF
    //==============================================================================
    function jump() {
        msgManager.sendUPC(UPC.SEND_MESSAGE_TO_ROOMS, "MOVE_MESSAGE", roomID, "true", "", "jump");
    }

    // Start watching the acceleration
    function startWatch() {
        // Update acceleration
        var options = {
            frequency: 120
        };

        watchID = navigator.accelerometer.watchAcceleration(onSuccess, onError, options);
    }

    // Stop watching the acceleration
    function stopWatch() {
        if (watchID) {
            navigator.accelerometer.clearWatch(watchID);
            watchID = null;
        }
    }

    // onSuccess: Get a snapshot of the current acceleration
    function onSuccess(acceleration) {
        activateClient(acceleration.x, acceleration.y, acceleration.z);
    }

    // onError: Failed to get the acceleration
    function onError() {
        $('#progress').empty().append('<li data-theme="e" data-role="content">Kiihtyvyysanturin käyttäminen ei onnistunut</div>');
        $('#progress').listview('refresh').bind('tap',
        function(event) {
            $(this).empty();
        });
    }

    function activateClient(ax, ay, az) {
        var xx = (ax - oldx) * (ax - oldx);
        var yy = (ay - oldy) * (ay - oldy);
        var zz = (az - oldz) * (az - oldz);
        var sum = xx + yy + zz;
        var pituus = Math.abs(Math.sqrt(sum));

        $('#pituus').html('<b>Pituus:</b> ' + roundNumber(pituus));

        if (pituus > 20) {
            msgManager.sendUPC(UPC.SEND_MESSAGE_TO_ROOMS, "MOVE_MESSAGE", roomID, "true", "", "jump");
        }
        else if ((pituus < 19) && (pituus > 8)) {
            msgManager.sendUPC(UPC.SEND_MESSAGE_TO_ROOMS, "MOVE_MESSAGE", roomID, "true", "", "run");
        }
        else {
            oldx = ax;
            oldy = ay;
            oldz = az;
        }
        oldx = ax;
        oldy = ay;
        oldz = az;
    }

    function toggle(obj) {
        var el = document.getElementById(obj);

        if (el.style.display != 'none') {
            el.style.display = 'none';
        }
        else {
            el.style.display = '';
        }
    }

    function roundNumber(num) {
        var dec = 5;
        var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
        return result;
    }

    //==============================================================================
    // JOIN FUNCTION
    //==============================================================================
    function join($url, $form) {

        $.ajax({
            type: 'POST',
            url: $url,
            data: $form.serialize(),
            dataType: 'xml',
            cache: false,
            beforeSend: function() {
                // show loader spinner
                $.mobile.showPageLoadingMsg();
            },
            error: function() {
                $('#progress').empty().append('<li data-theme="e" data-role="content">Verkkovirhe liityttäessä peliin</div>');
                $('#progress').listview('refresh').bind('tap',
                function(event) {
                    $(this).empty();
                });
                $.mobile.hidePageLoadingMsg();
            },
            success: function(data) {
                // xml data var
                var $xml = $(data);

                if ($xml.text() == 0) {
                    // 0 as xml value means that auth went wrong
                    $('#progress').empty().append('<li data-theme="e" data-role="content">Käyttäjätunnus tai salasana oli virheellinen.</div>');
                    $('#progress').listview('refresh').bind('tap',
                    function(event) {
                        $(this).empty();
                    });
                    $.mobile.hidePageLoadingMsg();
                } else if ($xml.text() == 1) {
                    // 1 as xml value means that auth ok
                    // user information from xml var
                    var $user = $xml.find('user');
                    // set user info to User obj
                    User.username = $user.attr('username');
                    User.nickname = $user.attr('nickname');
                    User.user_id = $user.attr('id');

                    // union init
                    init();

                } else {
                    // xml is not readable so probably something wrong with sportti server or there is some connection problem
                    $('#progress').empty().append('<li data-theme="e" data-role="content">Verkkovirhe liityttäessä peliin</div>');
                    $('#progress').listview('refresh').bind('tap',
                    function(event) {
                        $(this).empty();
                    });
                    $.mobile.hidePageLoadingMsg();
                }
            },
            complete: function() {
                // hide loader window
                //$.mobile.hidePageLoadingMsg();
                }
        });

        return false;
    }

    //==============================================================================
    // LEAVE FUNCTION
    //==============================================================================
    function leave() {

        // clear fields
        $('#acc').text('');
        $('#pituus').text('');

        // enable fields
        $('input[type="text"], input[type="password"], input[type="number"]').textinput('enable');

        // buttons
        $jBtn = $('<button type="submit" name="submit" data-theme="b" value="join">Liity peliin</button>');

        // add button and init it
        $row = $('.form-button-row').empty();
        $jBtn.appendTo($row);
        $jBtn.button();

        orbiter.disconnect();

        $.mobile.hidePageLoadingMsg();

        return false;
    }

})(this.jQuery);