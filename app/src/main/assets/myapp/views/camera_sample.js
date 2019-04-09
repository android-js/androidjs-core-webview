navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
window.URL = window.URL || window.webkitURL;

//var stream = null;

window.onload = function(){
//      console.log(doc)
      androidjs.camera.init(document.querySelector('#video'), {video:true, audio:false});
//    var video = document.querySelector('#video');
//    console.log(video);
//    //stream = null;
//
//    if (!navigator.getUserMedia) {
//        document.getElementById("error").innerHTML = "Your mobile is not supported";
//        console.error('getUserMedia not supported');
//    }
//    navigator.getUserMedia({ video: true , audio: false}, function (s) {
//        window.stream = s;
//        video.src = window.URL.createObjectURL(s);
//        console.log('Started');
//        document.getElementById("error").innerHTML = "Started";
//    }, function (error) {
//        document.getElementById("error").innerHTML = "Error Starting camera. Denied";
//        console.error('Error starting camera. Denied.');
//    });
}

//var recordedChunks = []
//
//
//function startRecording() {
//
//  var options = {mimeType: 'video/webm;codecs=vp9', bitsPerSecond: 100000};
//  recorder = new MediaRecorder(window.stream, options);
//  recorder.ondataavailable = handleDataAvailable;
//  recorder.start();
//}
//
//function handleDataAvailable(event) {
//  if (event.data.size > 0) {
//    recordedChunks.push(event.data);
//  } else {
//    // ...
//  }
//}
//
//ul = document.getElementById('ul');
//preview = document.getElementById('preview')
//function stopRecording() {
////  recorder.ondataavailable = e => {
////    ul.style.display = 'block';
////    var a = document.createElement('a'),
////      li = document.createElement('li');
////    a.download = ['video_', (new Date() + '').slice(4, 28), '.webm'].join('');
////    a.href = URL.createObjectURL(e.data);
////    preview.src = URL.createObjectURL(e.data);
////    preview.play();
////    a.textContent = a.download;
////    li.appendChild(a);
////    ul.appendChild(li);
////  };
//  recorder.stop();
//  var superBuffer = new Blob(recordedChunks, {type: 'video/webm'});
//    preview.src = window.URL.createObjectURL(superBuffer);
//}
