/**
 * 
 */

 $(document).ready(function() {

 	var userId = -1;
 	var userName;
 	var unitId;
 	var organId;
 	var enabled;

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		userId = $(this).attr('id');
 		userName = $(this).find('.userName').html();
 		unitId = $(this).find('.unit').attr('data');
 		organId = $(this).find('.organ').attr('data');
 		enabled = $(this).find('.enabled').html();

 		if(userId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});

 	$('#btn-change').click(function(){
 		$('#userId').val(userId);
 		$('#userName').val(userName);
 		$('#enabled').attr('checked', enabled);

 		if(unitId > 0) {
 			$('#unit').val(unitId);
 		}

 		if(organId > 0){
 			$('#organ').val(organId);
 		}
 		
 		$('.change-required').removeAttr('required');
 	});

 	$('#btn-add').click(function(){
 		$('#userId').val('-1');
 		$('#userName').val('');
 		$('#enabled').attr('checked', 'true');
 		$('#unit').val(1);
 		$('#organ').val(1);
 	
 		$('.change-required').attr('required', 'true');
 	});

 	/*$("table.sort_table").sort_table({
        "action" : "init"
    });
*/
    $("#table-body").remove();
 });