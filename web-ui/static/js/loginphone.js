$(document).ready(function() {
	function checkPhone(phone){
		var pattern = /^1[0-9]{10}$/;
		if (phone == '') {
			showMessage('请输入手机号码');
			return false;
		}
		if (!pattern.test(phone)) {
			showMessage('请输入正确的手机号码');
			return false;
		}
		return true;
	}
	
	function resetCode(waittime){
		$("#spanTime").text(waittime);
		var timer = null;
		timer = setInterval(function(){
			waittime -= 1;
			if (waittime > 0) {
				$('#spanTime').html(waittime);
			}
			else {
				clearInterval(timer);
				$(".btnGetCode").show();
				$(".btnResetCode").hide();
			}
		},1000);
	}
	
	function checkInfo(phone, authcode) {
		if (isEmpty(phone)) {
			showMessage("手机号不能为空");
			return false;
		}
		if (isEmpty(authcode)) {
			showMessage("验证码不能为空");
			return false;
		}
		// TODO: check authcode 6 digits
		return true;
	}
	
	$(".btnGetCode").on('click', function() {
		var phone = $('#phone').val();
		if (!checkPhone(phone)) 
			return false;
		$('.btnGetCode').hide();
		$('.btnResetCode').show();
		var waittime = 60;
		$.ajax({
			url: getFullUrl('/requestauthcode'),
			type: 'post',
			data: {
				"phone": phone
			},
			dataType: 'json',
			success: function(json) {
				console.log(json);
				if (json.code == 0) {
					showMessage("验证码发送成功");
					waittime = json.body;
					resetCode(waittime);
				}
				else {
					showMessage(json.msg);
				}
			},
			error: function(json) {
				
			}
		});
	});

	
	$(".btnLogin").on('click', function() {
		var phone = $("#phone").val(),
			authcode = $("#authcode").val();
		
		if (!checkInfo(phone, authcode))
			return false;
		$.ajax({
			url: getFullUrl('/loginwithauthcode'),
			type: 'post',
			data: {
				"phone": phone,
				"authcode": authcode
			},
			dataType: 'json',
			success: function(json) {
				console.log(json);
				if (json.code == 0) {
					showMessage("登陆成功！！！");
					setStorage("user", json.body.user);
                    routerTo("/ui/userinfo.html");
				}
				else {
					showMessage("登陆失败：" + json.msg);
				}
			},
			error: function(json) {
				showMessage("登陆失败");
				console.log(json);
			}
		});
	});
});