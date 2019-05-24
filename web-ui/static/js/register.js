$(document).ready(function() {
	function checkInfo(username, password, confirmpassword, phone, authcode, check) {
		if (isEmpty(username)) {
			showMessage("用户名不能为空");
			return false;
		}
		if (isEmpty(password)) {
			showMessage("密码不能为空");
			return false;
		}
		if (isEmpty(phone)) {
			showMessage("手机号不能为空");
			return false;
		}
		if (isEmpty(authcode)) {
			showMessage("手机验证码不能为空");
			return false;
		}
		if (confirmpassword !== password) {
			showMessage("两次输入的密码不相同");
			return false;
		}

		if (!check) {
			showMessage("请同意注册协议");
			return false;
		}
		return true;
	}
		
	$("#username").on('change', function() {
		var username = $("#username").val()
		if (isEmpty(username)) {
			$.ajax({
				url: '/checkusername',
				type: 'post',
				data: {
					"username": username
				},
				dataType: "json",
				success: function(json) {
					if (json.code === 0) {
						if (json.body === 1) {
							// username exists
							$(".btnRegister").attr("disabled", "disabled");
							showMessage("用户名已经存在！")
						}
						else {
							$(".btnRegister").removeAttr("disabled");
						}
					}
				}
			})
		}
	})
	
	$(".btnSendCode").on('click', function() {
			$(".btnSendCode").hide();
			$(".btnWait").show();
			var phone = $("#phone").val();

        	var phonePattern = /^\d{11}$/;
        	if(!phonePattern.test(phone)){
                showMessage('手机号不能为空，且为11位整数');
        		return false;
			}
			var waittime = 5;
			$.ajax({
				url: '/requestauthcode',
				type: 'post',
				data: {
					"phone": phone
				},
				dataType: 'json',
				success: function(json) {
					if (json.code == 0) {
						showMessage('验证码发送成功');
						waittime = json.body.waittime;
					}
					else {
						showMessage(json.msg);
					}
				},
				error: function(json) {
					
				}
			});
			$("#lefttime").text(waittime);
			var timer = null;
			timer = setInterval(function(){
				waittime -= 1;
				if (waittime > 0) {
					$('#lefttime').html(waittime);
				}
				else {
					clearInterval(timer);
					$(".btnSendCode").show();
					$(".btnWait").hide();
				}
			},1000);
		})
	
	$(".btnRegister").on('click', function() {
		var username = $("#username").val(),
			password = $("#password").val(),
			confirmpassword = $("#passwordrepeat").val()
			phone = $("#phone").val(),
			authcode = $("#authcode").val(),
			check = $("#agreement").prop("checked");
		// password 需要md加密
		if (!checkInfo(username, password, confirmpassword, phone, authcode, check)) {
			return false;
		}
		
		$.ajax({
			url: '/register',
			type: 'post',
			data: {
				"username": username,
				"password": md5(password),
				"phone": phone,
				"authcode": authcode
			},
			dataType: 'json',
			success: function(json) {
				if (json.code == 0) {
					console.log(json);
					showMessage('注册成功');
					setStorage("user", json.body);
                    routerTo("/ui/userinfo.html");

                }
				else {
					showMessage('注册失败，' + json.msg);
				}
			},
			error: function(json) {
				showMessage('注册失败');
				console.log(json);
			}
		});
	});
});