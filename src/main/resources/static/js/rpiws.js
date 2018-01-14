
(function( $ ) {
    $.fn.initializeWebcam = function() {
        var socket = new WebSocket('wss://' + window.location.host + '/webcam');
        var webcamImage = document.getElementById("webcamImage");

        socket.onmessage = function(message) {
            webcamImage.setAttribute("src", URL.createObjectURL(message.data));
        };
    };
}(jQuery));

// kick off the socket connection as soon as the page is loaded
$(document).ready(function() {
    $.fn.initializeWebcam();
});