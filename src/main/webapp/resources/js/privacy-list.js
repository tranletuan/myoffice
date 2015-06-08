 $(document).ready(function() {
 	var privacyId = -1;
 	var privacyName;
 	var description;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

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