 $(document).ready(function() {
 	var organId = -1;
 	var organName;
 	var address;
 	var shortName;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		organId = $(this).find('.organId').html();
 		organName = $(this).find('.organName').html();
 		address = $(this).find('.address').html();
 		shortName = $(this).find('.shortName').html();

 		if(organId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#organId').val(organId);
 		$('#organName').val(organName);
 		$('#address').val(address);
 		$('#shortName').val(shortName);
 	});

 	$('#btn-add').click(function(){
 		$('#organId').val("-1");
 		$('#organName').val("");
 		$('#address').val("");
 		$('#shortName').val("");
 	});

 });