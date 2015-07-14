 $(document).ready(function() {

 	$('.modal').on('shown.bs.modal', function() {
 		$(this).find('[autofocus]').focus();
 	});

 	$('.form_date').datetimepicker({
 		format : 'dd-mm-yyyy',
 		weekStart: 1,
 		todayBtn:  1,
 		autoclose: 1,
 		todayHighlight: 1,
 		startView: 2,
 		minView: 2,
 		forceParse: 0,
 		initialDate : new Date()
 	});

 	/*$('.form_date').datetimepicker("setDate", new Date());*/


 	$('.menu').click(function(){
 		if($(this).hasClass('disabled') == false) {
 			$('.menu').removeClass('disabled');
 			$(this).addClass('disabled');

 			$('.menu-content.in').addClass('hidden');
 			$('.menu-content.in').collapse('hide');

 			$('.collapse').on('show.bs.collapse', function(){
 				$(this).removeClass('hidden');
 			});
 		}
 	});

 	$('.search-form').submit(function(ev){

 		$('.btn-search').button('loading');

 		$.ajax({
 			type : $(this).attr('method'),
 			url : $(this).attr('action'),
 			data : $(this).serialize(),
 			success : function(response) {
 				$('#show-content').html(response);
 				$('.btn-search').button('reset');
 			},
 			error : function(){
 				$('.btn-search').button('reset');
 			}
 		});

 		ev.preventDefault();
 	});

 	$('.store-link').click(function(ev){

 		$.ajax({
 			type : 'GET',
 			url : $(this).attr('href'),
 			data : $(this).serialize(),
 			success : function(response) {
 				$('#show-content').html(response);
 			}
 		});

 		ev.preventDefault();
 	}); 
 	// Search Doc Out

 	$('#docTypeOut').change(function(){
 		var value = $(this).val();
 		if(value > 0) {
	 		var shortName = $('#docTypeOut :selected').attr('class');
	 		var organType = $('#organTypeOut').html();
	 		$('#numberSignOut').val(shortName + '/' + organType);
 		} else {
 			$('#numberSignOut').val('');
 		}
 	});

 	$('#btn-reset-out').click(function(){
 		$('#docNameOut').val('');
 		$('#epitomeOut').val('');
 		$('#numberOut').val('');
 		$('#docTypeOut').val(-1);
 		$('#numberSignOut').val('');
 		$('#departmentOut').val('');
 		$('#minDayOut').val('');
 		$('#maDayOut').val('');
 		$('#docNameOut').focus();
 	});


 	// Search Doc In

 	$('#docTypeIn').change(function(){
 		var value = $(this).val();
 		if(value > 0) {
	 		var shortName = $('#docTypeIn :selected').attr('class');
	 		var organType = $('#organTypeIn').html();
	 		$('#numberSignIn').val(shortName + '/' + organType);
 		} else {
 			$('#numberSignIn').val('');
 		}
 	});


 	

 });
