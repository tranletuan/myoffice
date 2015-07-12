$(document).ready(function() {

    var URL = $('#calendar').attr('data');
    $('#calendar').fullCalendar({
        header : {
        	left : 'prev,next today',
        	center : 'title',
        	right : 'month,basicWeek'
        },
        eventLimit: true,
        events : {
        	url : URL,
        	success : function(data) {
        		$('#calendar').fullCalendar('removeEvents');
        	}
        }
        
    });
    
});