 $(document).ready(function() {
 	var privacyId = -1;
 	var privacyName;
 	var description;

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		privacyId = $(this).find('.privacyId').html();
 		privacyName = $(this).find('.privacyName').html();
 		description = $(this).find(".description").html();

 		if(privacyId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#privacyId').val(privacyId);
 		$('#privacyName').val(privacyName);
 		$('#description').val(description);

 	});

 	$('#btn-add').click(function(){
 		$('#privacyId').val("-1");
 		$('#privacyName').val("");
 		$('#description').val(" ");

 	});

 });