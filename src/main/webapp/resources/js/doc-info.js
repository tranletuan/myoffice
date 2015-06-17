 $(document).ready(function() {

 	$('#btnChange').click(function(){
 		var docId;
 		var title;
 		var docName;
 		var epitome;
 		var docType;
 		var tenure;
 		var numberSign;
 		var privacyLevel;
 		var emergencyLevel;
 		var docPath;
 		var comment;
 	});

 	$('#btnDownload').click(function (){

 		var docPath = $('#docPath').html();
 		var docName = $('#docName').html();

 		$.ajax({
 			type : "PUT",
 			url : "download_document",
 			mimeType:"text/html; charset=UTF-8",
 			data : {
 				"docPath" : docPath,
 				"docName" : docName
 			},
 			success: function(response){
 				
 				console.log(response);
            }
        });
 	});



});

