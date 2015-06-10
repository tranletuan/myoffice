 $(document).ready(function() {
 	var tenureId = -1;
 	var tenureName;
 	var timeStart;
 	var timeEnd;

 	/*$("table.sort_table").sort_table({
 		"action" : "init"
 	});*/

 $("#table-body").remove();

 $('.table tr').click(function() {
 	$('.table tr').attr('class', ' ');
 	$(this).attr('class', 'active');

 	tenureId = $(this).find('.tenureId').html();
 	tenureName = $(this).find('.tenureName').html();
 	timeStart = $(this).find(".timeStart").html();
 	timeEnd = $(this).find(".timeEnd").html();

 	if(tenureId > 0){
 		$("#btn-change").removeAttr('disabled');
 	}
 });


 $('#btn-change').click(function(){
 	$('#tenureId').val(tenureId);
 	$('#tenureName').val(tenureName);
 	$('#timeStart').val(timeStart);
 	$('#timeEnd').val(timeEnd);

 });

 $('#btn-add').click(function(){
 	$('#tenureId').val("-1");
 	$('#tenureName').val("");
 	$('#timeStart').val("");
 	$('#timeEnd').val("");

 });

 $(".form_datetime").datetimepicker({
        format: "dd MM yyyy",
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
    });

});