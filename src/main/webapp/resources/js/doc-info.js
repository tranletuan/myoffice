 $(document).ready(function() {

 	$('#btnChange').click(function(){
 		var docId = $('.docId').html();
 		var title = $('.title').html();
 		var docName = $('.docName').html();
 		var epitome = $('.epitome').html();
 		var docType = $('.docType').attr('data');
 		var tenure = $('.tenure').attr('data');
 		var number = $('.number').html();
 		var privacyLevel = $('.privacyLevel').attr('data');
 		var emergencyLevel = $('.emergencyLevel').attr('data');
 		var comment = $('.comment').html();

 		$('#docId').val(docId);
 		$('#title').val(title);
 		$('#docName').val(docName);
 		$('#epitome').val(epitome);
 		$('#docType').val(docType);
 		$('#tenure').val(tenure);
 		$('#number').val(number);
 		$('#privacyId').val(privacyLevel);
 		$('#emeId').val(emergencyLevel);
 		$('#comment').val(comment);

 		var docType = $('#docType option:selected').attr('class');
 		var organ = $('#organ').attr('class');

 		var numberSign =  '-' + docType + '/' + organ + '-';
  		$('#numberSign').val(numberSign);
 	});

 	$('#btnAutoGeneratedNumber').click(function (){

 		
 	});



});
