/**
 * 
 */

 $(document).ready(function() {

 	var userId = -1;
 	var userName;
 	var unit;
 	var enabled;

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		userId = $(this).attr('id');
 		userName = $(this).find('.userName').html();
 		unit = $(this).find('.unit').html();
 		enabled = $(this).find('.enabled').html();

 		if(userId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});

 	$('#btn-change').click(function(){
 		$('#userId').val(userId);
 		$('#userName').val(userName);
 		$('#enabled').attr('checked', enabled);


 		var unitList = $('#unit').find('option');
 		for(var i = 0; i < unitList.length; i++){

 			if(unitList[i].innerText == unit){
 				unitList[i].setAttribute('selected', 'true');
 				break;
 			}
 		}

 		$('.change-required').removeAttr('required');
 	});

 	$('#btn-add').click(function(){
 		$('#userId').val('-1');
 		$('#userName').val('');
 		$('#enabled').attr('checked', 'true');
 	
 		$('.change-required').attr('required', 'true');
 	});

 	/*$("table.sort_table").sort_table({
        "action" : "init"
    });
*/
    $("#table-body").remove();
 });