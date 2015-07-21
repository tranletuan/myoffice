/**
 * 
 */

 $(document).ready(function() {

 	var hexDigits = new Array
 	("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 

	//Function to convert hex format to a rgb color
	function rgb2hex(rgb) {
		rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
		return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
	}

	function hex(x) {
		return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
	}

 	$('.modal').on('shown.bs.modal', function() {
	  $(this).find('[autofocus]').focus();
	});
	
	
	//form
    var successLabel = "glyphicon-ok";
    var errorLabel = "glyphicon-remove";
 	var userId = -1;
 	var userName;
 	var organId;
 	var enabled;
 	var levelId;
 	var userColor;

 	$('.table tr').click(function() {
 		$('.table tr').attr('class', ' ');
 		$(this).attr('class', 'active');

 		userId = $(this).attr('id');
 		userName = $(this).find('.userName').html();
 		organId = $(this).find('.organ').attr('data');
 		enabled = $(this).find('.enabled').html();
 		levelId = $(this).find('.level').attr('data');
 		userColor = rgb2hex($(this).find('.userColor').css('background-color'));
 

 		if(userId > 0){
 			$("#btn-change").removeAttr('disabled');
 		}
 	});

 	$('#btn-change').click(function(){
 		$('#userId').val(userId);
 		$('#userName').val(userName);
 		$('#enabled').attr('checked', enabled);
 		$('#level').val(levelId);
 		$('#myColor').val(userColor);
 		$('#myColor').colorpicker('setValue', userColor);
 		$('#showColor').css('background-color', userColor);
 
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

    $("#table-body").remove();
    $('#password').focusin(function(){ $('.checkNewPassword').empty();});
    $('#password').focusout(function(){  
        var URL = $(this).attr('data');
        var newPassword = $(this).val();
        $('.checkNewPassword').empty();

        $.ajax({
            type : "GET",
            url : URL,
            data : {
                "newPassword" : newPassword 
            },
            success: function(response){
                if(response) {
                    $('.checkNewPassword').append("<span style='color:green;' class='glyphicon " + successLabel + "'></span>");
                } else {
                    $('.checkNewPassword').append("<span style='color:red;' class='glyphicon " + errorLabel + "'></span>");
                }
            }
        });
    });

    //color picker
    $('#myColor').colorpicker({
    	customClass: 'colorpicker-2x',
    	sliders: {
    		saturation: {
    			maxLeft: 200,
    			maxTop: 200
    		},
    		hue: {
    			maxTop: 200
    		},
    		alpha: {
    			maxTop: 200
    		}
    	}
    });

    $('#myColor').colorpicker().on('changeColor.colorpicker', function(event){
    	$('#showColor').css('background-color', event.color.toHex());
    });

 });