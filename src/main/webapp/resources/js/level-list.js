 $(document).ready(function() {
 	var levelId = -1;
 	var fullName;
 	var shortName;
 	var value;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 $("#table-body").remove();

 $('.table tr').click(function() {
 	$('.table tr').attr('class', ' ');
 	$(this).attr('class', 'active');

 	levelId = $(this).find('.levelId').html();
 	fullName = $(this).find('.fullName').html();
 	shortName = $(this).find(".shortName").html();
 	value = $(this).find(".value").html();

 	if(levelId > 0){
 		$("#btn-change").removeAttr('disabled');
 	}
 });


 $('#btn-change').click(function(){
 	$('#levelId').val(levelId);
 	$('#fullName').val(fullName);
 	$('#shortName').val(shortName);
 	$('#value').val(value);

 });

 $('#btn-add').click(function(){
 	$('#levelId').val("-1");
 	$('#fullName').val("");
 	$('#shortName').val("");
 	$('#value').val("");

 });

});