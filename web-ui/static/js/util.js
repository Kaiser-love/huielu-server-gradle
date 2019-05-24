// 服务器地址
var serverPath = "http://127.0.0.1:9999";

function getFullUrl(url) {
    return serverPath + url;
}

// 登出
function logout() {
    $.ajax({
        url: getFullUrl('/logout'),
        type: 'post',
        data: {},
        dataType: 'json',
        success: function (json) {
            if (json.code === 0) {
                showMessage('登出成功');
                removeStorage("user");
                routerTo("/ui/login.html");
            }
            else {
                showMessage(json.msg);
            }
        },
        error: function (json) {

        }
    });
}

// 判空
function isEmpty(data) {
    if (data != null && data != "")
        return false;
    return true;
}

// 显示信息
function showMessage(mes) {
    if (mes != null) {
        alert(mes);
    }
}

// 设置本地存储obj
function setStorage(name, obj) {
    if (name && obj) {
        var obj = JSON.stringify(obj);
        window.localStorage.setItem(name, obj);
    }
}

// 获取本地存储obj
function getStorage(name) {
    if (typeof name != 'string')
        return;
    var obj = window.localStorage.getItem(name);
    if (obj) return JSON.parse(obj)
    return null;
}

// 删除本次存储
function removeStorage(name) {
    if (typeof name === 'string')
        window.localStorage.removeItem(name);
}

// 获取url查询参数
function getParams() {
    var arr = window.location.search.substring(1).split('&')
    params = {},
        temp,
        name,
        value;
    for (var i = 0; i < arr.length; ++i) {
        temp = arr[i].split('=');
        name = temp[0];
        value = temp[1];
        params[name] = value;
    }
    return params;
}

function checkLogin() {
    $.ajax({
        url: getFullUrl('/logout'),
        type: 'post',
        data: {},
        dataType: 'json',
        success: function (json) {
            if (json.code === 0) {
                showMessage('登出成功');
                removeStorage("user");
                routerTo("/index.html");
            }
            else {
                showMessage(json.msg);
            }
        },
        error: function (json) {

        }
    });
}

// 路由
function routerTo(url, params) {
    let href = url;
    if (params) {
        href = href + '?';
        let first = true;
        for (let p in params) {
            if (first) first = false;
            else href = href + '&';
            href = href + p + '=' + params[p];
        }
    }
    oldhref = window.location.href;
    console.log('from [' + oldhref + '] to [' + href + ']');
    window.location.href = href;
}

$(document).ready(function () {

});

function showToast(msg,type) {
    new NoticeJs({
        text: 'Notification message',
        position: 'topLeft',
    }).show();
}