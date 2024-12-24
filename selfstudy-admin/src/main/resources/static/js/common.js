var storage=window.localStorage;
var tokenName = 'selfStudyAdminToken'

function strIsEmpty(str){
    return str === null || str === undefined || str === '' || str === 'null' ||
        str === 'undefined';
}

function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var array = window.location.search.substr(1).match(reg);
    if (array != null) {
        return decodeURIComponent(array[2])
    }
    return ""
}

function ajaxPost(url,data,callback){
    layer.load();
    $.ajax({
        url:url,
        type:'POST',
        cache:false,//关缓存
        // async:false,//关闭异步
        headers: {"token": ""},
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        data: data,
        dataType:"json", //期望服务端返回的数据类型
        success:function (res) {
            layer.closeAll('loading');
            callback(res);
        },error:function (jqXHR, textStatus, errorThrown) {
            layer.closeAll('loading');
            // layer.msg("HTTP状态码:"+jqXHR.status,{icon: 5})
            alert("HTTP状态码:"+jqXHR.status)
        }
    });
}

function ajaxPostJson(url,data,callback){
    layer.load();
    $.ajax({
        url:url,
        type:'POST',
        cache:false,//关缓存
        // async:false,//关闭异步
        headers: {"token": ""},
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        dataType:"json", //期望服务端返回的数据类型
        success:function (res) {
            layer.closeAll('loading');
            callback(res);
        },error:function (jqXHR, textStatus, errorThrown) {
            layer.closeAll('loading');
            // layer.msg("HTTP状态码:"+jqXHR.status,{icon: 5})
            alert("HTTP状态码:"+jqXHR.status)
        }
    });
}

function getTokenUrl(url){
    var token = getToken()
    return url+"?"+tokenName+"="+token
}

function getToken(){
    return storage.getItem(tokenName)
}

function setToken(token){
    storage.setItem(tokenName,token)
}

function removeToken(){
    storage.removeItem(tokenName)
}

//获取当前日期 格式 yyyy-MM-dd
function getNowFormatDate() {
    let date = new Date(),
        year = date.getFullYear(), //获取完整的年份(4位)
        month = date.getMonth() + 1, //获取当前月份(0-11,0代表1月)
        strDate = date.getDate() // 获取当前日(1-31)
    if (month < 10) month = `0${month}` // 如果月份是个位数，在前面补0
    if (strDate < 10) strDate = `0${strDate}` // 如果日是个位数，在前面补0

    return `${year}-${month}-${strDate}`
}

// date 代表指定的日期，格式：yyyy-MM-dd
// day 传-1表始前一天，传1表始后一天
// JS获取指定日期的前一天，后一天
function getNextDate(date, day) {
    var dd = new Date(date);
    dd.setDate(dd.getDate() + day);
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
    var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
    return y + "-" + m + "-" + d;
}

//判断正整数
function positiveInt(sort){
    return (/(^[1-9]\d*$)/.test(sort))
}

//判断正数
function isPositiveNumber(str){
    return /^\d+(\.\d+)?$/.test(str) && parseFloat(str) > 0;
}

//大于或等于 0 的整数
function isNonNegativeInt(value) {
    return /^(0|[1-9]\d*)$/.test(value);
}