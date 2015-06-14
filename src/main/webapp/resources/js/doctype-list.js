 $(document).ready(function() {
 	var docTypeId = -1;
 	var docTypeName;
 	var shortName;
 	var description;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		docTypeId = $(this).find('.docTypeId').html();
 		docTypeName = $(this).find('.docTypeName').html();
 		shortName = $(this).find('.shortName').html();
 		description = $(this).find(".description").html();

 		if(docTypeId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#docTypeId').val(docTypeId);
 		$('#docTypeName').val(docTypeName);
 		$('#shortName').val(shortName);
 		$('#description').val(description);

 	});

 	$('#btn-add').click(function(){
 		$('#docTypeId').val("-1");
 		$('#docTypeName').val("");
 		$('#shortName').val("");
 		$('#description').val(" ");

 	});

 });