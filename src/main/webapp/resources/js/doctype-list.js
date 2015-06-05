 $(document).ready(function() {
 	var typeId = -1;
 	var typeName;
 	var shortName;
 	var description;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 	$("#table-body").remove();

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		typeId = $(this).find('.typeId').html();
 		typeName = $(this).find('.typeName').html();
 		shortName = $(this).find('.shortName').html();
 		description = $(this).find(".description").html();

 		if(typeId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});


 	$('#btn-change').click(function(){
 		$('#typeId').val(typeId);
 		$('#typeName').val(typeName);
 		$('#shortName').val(shortName);
 		$('#description').val(description);

 	});

 	$('#btn-add').click(function(){
 		$('#typeId').val("-1");
 		$('#typeName').val("");
 		$('#shortName').val("");
 		$('#description').val(" ");

 	});

 });