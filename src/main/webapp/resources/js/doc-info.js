 $(document).ready(function() {

 	$('#doc-form').toggle();
	
	function toggleDocForm(){
 		$('#doc-info').toggle();
 		$('#doc-form').toggle();
 	}

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

