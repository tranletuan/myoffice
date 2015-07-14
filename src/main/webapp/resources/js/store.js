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

 	$('.form_date').datetimepicker("setDate", new Date());


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
 				console.log("ok");
 				$('.btn-search').button('reset');
 			},
 			error : function(){
 				console.log('error');
 				$('.btn-search').button('reset');
 			}
 		});

 		ev.preventDefault();
 	});

 	$('#docType').change(function(){
 		var value = $(this).val();
 		if(value > 0) {
	 		var shortName = $('#docType :selected').attr('class');
	 		var organType = $('#organType').html();
	 		$('#numberSign').val(shortName + '/' + organType);
 		} else {
 			$('#numberSign').val('');
 		}

 		$(this).next().focus();
 	});

 });
