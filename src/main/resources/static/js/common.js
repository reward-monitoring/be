
function comInputCheck(type, str){
	//type 값 체크 : radio/ select/ text / checkbox
	//str : 체크할 값에 정의된 ID or Name or Class
	if(type == "text"){
		chkText = $("#"+str).val();
	}
	else if(type == "select"){
		chkText = $("#"+str+" option:selected").val();
	}
	else if(type == "radio"){
		chkText = $("." + str).is(":checked"); //라디오는 class로 넘김
	}
	else if(type == "checkbox"){
		chkText = $('input:checkbox[name="'+ str + '"]').is(":checked"); //체크박스는 name으로 넘김
	}

	if(chkText == "" || chkText === false){
		return true;
	}else{
		return false;
	}
}

function winPopupCenter(url, winName, pwidth, pheight, scrollYN, resizeYN) {
    var win = null;
    var spec = 'toolbar=no,';
    spec += 'status=no,';
    spec += 'scrollbars='+(scrollYN == undefined ? "no" : scrollYN)+',';
    spec += 'resizable='+(resizeYN == undefined ? "no" : resizeYN);

    var mtWidth		= document.body.clientWidth;
    var mtHeight	= document.body.clientHeight;
    var scX			= window.screenLeft;
    var scY			= window.screenTop;
    var popX		= scX + (mtWidth - pwidth) / 2 ;
    var popY = scY + (screen.availHeight - pheight) / 2 - 20;

    win = window.open(url, winName, "width=" + pwidth + ", height=" + pheight + ", left=" + popX + ", top=" + popY + ',' + spec);

    if(parseInt(navigator.appVersion) >= 4) {
        win.window.focus();
    }
}


$(function() {
	$('.btn-datetimepicker').datetimepicker({
		format: "yyyy-mm-dd HH:ii",	//데이터 포맷 형식
		autoclose : true,	//사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
		//calendarWeeks : false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
		//clearBtn : false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
		//datesDisabled : ['2019-06-24','2019-06-26'],//선택 불가능한 일 설정 하는 배열 위에 있는 format 과 형식이 같아야함.
		//daysOfWeekDisabled : [0,6],	//선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
		//daysOfWeekHighlighted : [3], //강조 되어야 하는 요일 설정
		disableTouchKeyboard : false,	//모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
		immediateUpdates: false,	//사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
		//multidate : false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
		//multidateSeparator :",", //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
		templates : {
			leftArrow: '&laquo;',
			rightArrow: '&raquo;'
		}, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
		showWeekDays : true ,// 위에 요일 보여주는 옵션 기본값 : true
		//title: "테스트",	//캘린더 상단에 보여주는 타이틀
		todayHighlight : true ,	//오늘 날짜에 하이라이팅 기능 기본값 :false
		toggleActive : true,	//이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
		weekStart : 0 ,//달력 시작 요일 선택하는 것 기본값은 0인 일요일
		//language : "ko"	//달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.

	});//datetimepicker end

	$('.btn-datepicker').datepicker({
		format: "yyyy-mm-dd",	//데이터 포맷 형식
		todayHighlight : true	//오늘 날짜에 하이라이팅 기능 기본값 :false
	});//datepicker end

	$('.btn-timepicker').timepicker({
		timeformat: "HH:mm:ss",	//데이터 포맷 형식
	});//timepicker end

});//ready end
