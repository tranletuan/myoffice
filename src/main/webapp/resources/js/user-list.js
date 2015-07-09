/**
 * 
 */

 $(document).ready(function() {

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});
	
 	var userId = -1;
 	var userName;
 	var organId;
 	var enabled;
 	var levelId;

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		userId = $(this).attr('id');
 		userName = $(this).find('.userName').html();
 		organId = $(this).find('.organ').attr('data');
 		enabled = $(this).find('.enabled').html();
 		levelId = $(this).find('.level'),attr('data');

 		if(userId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});

 	$('#btn-change').click(function(){
 		$('#userId').val(userId);
 		$('#userName').val(userName);
 		$('#enabled').attr('checked', enabled);
 		$('#level').val(levelId);

 		if(organId > 0){
 			$('#organ').val(organId);
 		}
 		
 		$('.change-required').removeAttr('required');
 	});

 	$('#btn-add').click(function(){
 		$('#userId').val('-1');
 		$('#userName').val('');
 		$('#enabled').attr('checked', 'true');
 		$('#organ').val(1);
 		$('#password').val('');
 		$('#level').val(1);
 		
 		$('.change-required').attr('required', 'true');
 	});

 	/*$("table.sort_table").sort_table({
        "action" : "init"
    });
*/
    $("#table-body").remove();
 });