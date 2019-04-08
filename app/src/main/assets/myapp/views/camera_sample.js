navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
window.URL = window.URL || window.webkitURL;

window.onload = function(){
    var video = document.querySelector('#video'),
    stream = null;

    if (!navigator.getUserMedia) {
        document.getElementById("error").innerHTML = "Your mobile is not supported";
        console.error('getUserMedia not supported');
    }
    navigator.getUserMedia({ video: true , audio: false}, function (s) {
        stream = s;
        video.src = window.URL.createObjectURL(stream);
        console.log('Started');
        document.getElementById("error").innerHTML = "Started";
    }, function (error) {
        document.getElementById("error").innerHTML = "Your mobile is not supported";
        console.error('Error starting camera. Denied.');
    });
}