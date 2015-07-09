 $(document).ready(function() {
 	var levelId = -1;
 	var levelName;
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
 	levelName = $(this).find('.levelName').html();
 	shortName = $(this).find(".shortName").html();
 	value = $(this).find(".value").html();

 	if(levelId > 0){
 		$("#btn-change").removeAttr('disabled');
 	}
 });


 $('#btn-change').click(function(){
 	$('#levelId').val(levelId);
 	$('#levelName').val(levelName);
 	$('#shortName').val(shortName);
 	$('#value').val(value);

 });

 $('#btn-add').click(function(){
 	$('#levelId').val("-1");
 	$('#levelName').val("");
 	$('#shortName').val("");
 	$('#value').val("");

 });

});