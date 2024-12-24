var layer;
var form;
var table;
var laydate;
$(function() {
	commonMajor=0;
	commonJob=0;

	layui.use([ 'form','layer','table' ,'laydate' ], function() {
		form = layui.form;
		layer = layui.layer;
		table = layui.table;
		laydate = layui.laydate;

	});
});


function changeJob(id){
	commonJob=id;
	var returnId = getMajorOption(id);
	// if( isEmpty(returnId) || returnId < 1){
	// 	layer.msg('该项目下无科目');
	// }
	changeMajor(returnId);
}

function changeMajor(id){
	commonMajor =id;
	getTree();
}

function getJobOption(){
	var id=-1;
	$.ajax({
		url:"../commonData/getJobList",
		type:"post",
		async:false,
		dataType:"json",
		success:function(data){
			if(data.code==-1){
				lsyer.msg(data.msg);
				return id;
			}
			var arr=data.result;
			var str= "";
			if(arr==null||arr.length==0){
				$("#job_select").html(str);
				return id;
			}
			//允许查全部
			var str="<div class='layui-form'>";
			str+='<div class="layui-form-item"><label class="layui-form-label">项目</label>';
			str+='<div class="layui-input-block">';
			str+='<select id="jobListSelect" lay-filter="jobList" onchange="changeJob(this.value)";>';
			str+='<option value=""></option>';
			str+='<option value="0" selected="selected">全部</option>';
			for(let i=0;i<arr.length;i++){
				if(i==0){
					str+='<option value="'+arr[i].id+'">'+arr[i].name+'</option>';
				}else{
					str+='<option value="'+arr[i].id+'">'+arr[i].name+'</option>';
				}
			}
			str+='</select>';
			str+='</div></div>';
			str+='</div>';

			//将选中对象调整到集合的前面
			// arr = sortListById(selectIds,arr);
			// var index = selectIds.length;
			// str=getMultipleSelectInput(id,title,list,index);
			// str+=	"<div class='layui-form'>";
			// str+=		getSelectInput("jobList","项目",arr,0);
			// str+=	"</div>";
			$("#job_select").html(str);
			form.render();
			form.on('select(jobList)', function(data){
				changeJob(data.value);
			});
			id= arr[0].id;
		}
	});
	return id;
}

function getMajorOption(jobId){
	var id = -1;
	if(isEmpty(jobId) || jobId <1){   //选择所有 清空选项
		$("#major_select").html("");
		form.render();
		return id;
	}
	$.ajax({
		url:"../commonData/getMajorList",
		type:"post",
		async:false,
		data:{"jobId":jobId},
		dataType:"json",
		success:function(data){
			if(data.code==-1){
				lsyer.msg(data.msg);
				return id;
			}
			var arr=data.result;
			var str= "";
			if(arr==null||arr.length==0){
				$("#major_select").html(str);
				return id;
			}
			//将选中对象调整到集合的前面
			// arr = sortListById(selectIds,arr);
			// var index = selectIds.length;
			// str=getMultipleSelectInput(id,title,list,index);
			str+=	"<div class='layui-form'>";
			str+=		getSelectInput("majorList","科目",arr,0);
			str+=	"</div>";
			$("#major_select").html(str);
			form.render();
			form.on('select(majorList)', function(data){
				changeMajor(data.value);
			});
			id= arr[0].id;
		}
	});
	return id;
}


function transId(id){
	id+="";
	var f=id.indexOf("|");
	if(f==-1){
		return 0;
	}
	var ids = id.split("|");
	return ids[0];
}

function changeOpenById(id){
	var tn=$.fn.zTree.getZTreeObj("treeArea");
	var node = tn.getNodeByParam("id",id,null);
	node=getGrandNode(node);
	/* 	if(node.level==0){
	tn.expandAll(false);
} */
	var flag=true;//node.open?false:true;
	tn.expandNode(node, flag, true, true, false);
}

function getGrandNode(node){
	if(node==null){
		return null;
	}
	if(node.level==0){
		return node;
	}else if(node.level>0){
		var pNode = node.getParentNode();
		return getGrandNode(pNode);
	}else{
		return null;
	}
}

//这个有点问题 还是后端排好序再说
function sortTree(treeObj,nodes){
	if(nodes==null||nodes.length==0){
		return ;
	}
	for(var i=0;i<nodes.length;i++){
		for(var j=0;j<i;j++){
			if(nodes[i].sort<nodes[j].sort&&nodes[i].level-nodes[j].level==0){
				var returnNode=treeObj.moveNode(nodes[i], nodes[j], "next",true);
			}
		}
		var sons=nodes[i].children;
		sortTree(treeObj,sons);
	}
}

function filterLevel0(node) {
	return (node.level == 0);
}


/////////////////////////////////////////////////////////////////表单相关


function getRadio(name,title,titleArr,valueArr,checkedVal){
	var str="";
	str+='<div class="layui-form-item">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	for(var i=0;i<valueArr.length;i++){
//		if(i!=index){
		if(isEmpty(checkedVal)||checkedVal != valueArr[i]){
			str+='<input type="radio" lay-filter="'+name+'" name="'+name+'" value="'+valueArr[i]+'" title="'+titleArr[i]+'"><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><div>'+titleArr[i]+'</div></div>';
		}else{
			str+='<input type="radio" lay-filter="'+name+'" name="'+name+'" value="'+valueArr[i]+'" title="'+titleArr[i]+'" checked="checked"><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon layui-anim-scaleSpring"></i><div>'+titleArr[i]+'</div></div>';
		}
	}
	str+='</div>';
	str+='</div>';
	return str;
}

function getCheckBox(name,title,titleArr,valueArr,index){
	var str='';
	str += '<div class="layui-form-item">';
	str += '<label class="layui-form-label">'+title+'</label>';
	str += '<div class="layui-input-block">';
	for(var i=0;i<valueArr.length;i++){
		if(i!=index){
			str+='<input type="checkbox" name="'+name+'" lay-skin="primary" value="'+valueArr[i]+'" title="'+titleArr[i]+'">';
		}else{
			str+='<input type="checkbox" name="'+name+'" lay-skin="primary" value="'+valueArr[i]+'" title="'+titleArr[i]+'" checked="checked">';
		}
	}
	str += '</div>';
	str += '</div>';
	return str ;
}
function getAttrById(id,attr){
	var dom = $("#" + id);
	var val = dom.attr(attr);
	return val;
}
function getTextarea(id,title,content){
	if(content==null){
		content="";
	}
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	str+='<textarea onclick="clickTextArea(\''+id+'\')" id="'+id+'" placeholder="请输入'+title+'" class="layui-textarea">'+content+'</textarea>';
	str+='</div>';
	str+='</div>';
	return str;
}

function getCoverShowArea(id,title,name,path,size){
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-upload">';
	str+='<div style = "display: flex;flex-direction: row; align-items: center">';
	str+='<button type="button" class="layui-btn layui-btn-primary" onclick=clickImgUploadInput(\''+id+'\')>上传文件</button><input id="'+id+'" class="layui-upload-file" type="file" accept="undefined" name="'+name+'">';
	if(!isEmpty(size)){
		str+= '<div style = "margin-left:5px">' + size + '</div>';
	}
	str+='</div>';
	str+='<div class="layui-upload-list">';
	if(path!=null&&path!=""&&path!="null"){
		str+='<img style="width:360px;height:240px;margin-left:110px;" class="layui-upload-img" id="covershowupload" src="'+path+'">';
		//str+='<p id="demoText"></p>';
	}
	str+='</div>';
	str+='</div>';
	str+='</div>';
	return str;
}

function getPicArea(title,path){
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-upload">';
	str+='<div class="layui-upload-list">';
	if(path!=null&&path!=""&&path!="null"){
		str+='<img style="height:width:360px;height:240px" class="layui-upload-img" src="'+path+'">';
	}
	str+='</div>';
	str+='</div>';
	str+='</div>';
	return str;
}

function getFileShowArea(id,title,name,path){
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-upload">';
	str+='<button type="button" class="layui-btn" onclick=clickImgUploadInput(\''+id+'\')>上传文件</button><input id="'+id+'" class="layui-upload-file" type="file" accept="undefined" name="'+name+'">';
	str+='<button type="button" style="margin-left:10px" class="layui-btn" onclick="toUrl(\''+path+'\')">查看</button>';
	str+='<div class="layui-upload-list">';
	str+='</div>';
	str+='</div>';
	str+='</div>';
	str+='</div>';
	return str;
}


function clickImgUploadInput(id){
	$("#"+id).click();
}

function toForm(id,formStr){
	var str="";
	str += '<form class="layui-form" method="post" id="'+id+'" enctype="multipart/form-data">';
	str += formStr;
	str += '</form>';
	return str;
}

function addDivClass(addStr,className){
	var str="";
	str += '<div class="'+className+'" >';
	str += addStr;
	str += '</div>';
	return str;
}

function toFormClass(id,formStr){
	var str="";
	str += '<div class="layui-form" id="'+id+'">';
	str += formStr;
	str += '</div>';
	return str;
}

function getSelectInput(id,title,arr,tid){
	var str='';  //<div class="layui-form">
	str+='<div class="layui-form-item"><label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	str+='<select id="'+id+'" name="interest" lay-filter="'+ id +'"  lay-verify=" " lay-search>';
	str+='<option value=""></option>';
	for(let i=0;i<arr.length;i++){
		if(i==0){
			str+='<option value="'+arr[i].id+'" selected="selected">'+arr[i].name+'</option>';
		}else{
			str+='<option value="'+arr[i].id+'">'+arr[i].name+'</option>';
		}
	}
	str+='</select>';
	str+='</div></div>';
	/*str+='</div>';*/
	return str;
}

function getMultipleSelectInput(id,title,arr,index){
	var str='';  //<div class="layui-form">
	str+='<div class="layui-form-item"><label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	str+='<select id="'+id+'" lay-filter="'+ id +'" multiple="multiple">';
	str+='<option value=""></option>';
	for(let i=0;i<arr.length;i++){
		if(i<=index){
			str+='<option value="'+arr[i].id+'" selected="selected">'+arr[i].name+'</option>';
		}else{
			str+='<option value="'+arr[i].id+'">'+arr[i].name+'</option>';
		}
	}
	str+='</select>';
	str+='</div></div>';
	/*str+='</div>';*/
	return str;
}

function getMultipleTeacherSelect(id,title,tids){
	if(tids==null||tids.length==0){
		tids=[-1];
	}
	var str="";
	$.ajax({
		method : "post",
		url : '../commonData/getAllTeacherList',
		async:false,
		data:{"teacherIds":tids},
		dataType : "json",
		success : function(data) {
			var result=data.result;
			var list=result.list;
			var index=result.index;
			str=getMultipleSelectInput(id,title,list,index);
		}
	});
	return str;
}

function getMajorSelect(id,title,mids){
	if(mids==null||mids.length==0){
		mids=[-1];
	}
	var str="";
	var jobId = -1;
	if(!isEmpty(commonJob) && commonJob>0){
		jobId = commonJob;
	}
	$.ajax({
		method : "post",
		url : '../commonData/getAllMajor',
		async:false,
		data:{"majorIds":mids,"jobId":jobId},
		dataType : "json",
		success : function(data) {
			var result=data.result;
			var list=result.list;
			var index=result.index;
			str=getMultipleSelectInput(id,title,list,index);
		}
	});
	return str;
}

function getMajorSingleSelect(id,title,mids){
	if(mids==null||mids.length==0){
		mids=[-1];
	}
	var str="";
	var jobId = -1;
	if(!isEmpty(commonJob) && commonJob>0){
		jobId = commonJob;
	}
	$.ajax({
		method : "post",
		url : '../commonData/getAllMajor',
		async:false,
		data:{"majorIds":mids,"jobId":jobId},
		dataType : "json",
		success : function(data) {
			var result=data.result;
			var list=result.list;
			var index=result.index;
			str=getSelectInput(id,title,list,index);
		}
	});
	return str;
}

function getCourseSelect(id,title,selectIds,type,multiple){
	if(selectIds==null||selectIds.length==0){
		selectIds=[-1];
	}
	var str="";
	$.ajax({
		method : "post",
		url : '../commonData/getAllCourseByType',
		async:false,
		data:{"selectIds":selectIds,"type":type},
		dataType : "json",
		success : function(data) {
			var result=data.result;
			var list=result.list;
			var index=result.index;
			if(multiple!=null&&multiple==1){
				str=getMultipleSelectInput(id,title,list,index);
			}else{
				str=getSelectInput(id,title,list,index);
			}
		}
	});
	return str;
}

var sEditor;
function clickTextArea(textAreaId){
	if(typeof(sEditor) != "undefined"&&sEditor!=null){
		sEditor.destroy();
		sEditor=null;
	}
	openEditor(textAreaId);
	var con=$("#"+textAreaId).html();
	sEditor=getUEditorWithId("editorArea");
	sEditor.ready(function() {
		if(con != null && con !=""){
			sEditor.setContent(con, true);
		}
	});
}

function openEditor(id){
	var width = 720;
	var height = 430;
	var editAreaWidth = width - 3;
	var editAreaHeight = height - 115;  //減去底部長度
	var str="";
	str += '	<div id="editorArea" type="text/plain" style = "width:'+editAreaWidth+'px;height:'+editAreaHeight+'px;"></div>';
	layer.open({
		type: 1,
		title: false,
		closeBtn: 0,
		area: [width+"px",height+"px"],
		shadeClose: true,  //开启遮罩关闭
		skin: 'layui-layer-rim', //加上边框
		content: str ,
		zIndex: 120 ,
		btn: ['确定','关闭'],
		yes: function(index){
			getTextInEditor(id);
			layer.close(layer.index);
		},
		cancel : function( ){
			layer.close(layer.index);
		}
	});
}


function getEditorTextarea(id,title,content){
	if(content==null){
		content="";
	}
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	str+='<div onclick="clickTextArea(\''+id+'\')" id="'+id+'" class="layui-textarea" style="overflow:auto;">'+content+'</div>';
	str+='</div>';
	str+='</div>';
	return str;
}

//覆盖上面同名方法
function getTextarea(id,title,content){
	if(content==null){
		content="";
	}
	var str="";
	str+='<div class="layui-form-item layui-form-text">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-block">';
	str+='<textarea id="'+id+'" class="layui-textarea">'+content+'</textarea>';
	str+='</div>';
	str+='</div>';
	return str;
}

function resetNotice(){
	$.ajax({
		method : "post",
		url : '../soliveManage/resetAllSolVieNotice',
		async:false,
		dataType : "json",
		success : function(data) {
			layer.msg(data.msg);
		}
	});
}

function uploadFile(formId,name){
	var subform = $("form[id="+formId+"]");   //有空给表单加上name
	var path="";
	var options = {
		url : "../resources/uploadByName",
		type : "post",
		async : false,
		data : {"name":name},
		dataType : "json",
		success : function(data) {
			path =data.result;
		},
		error: function (data){
		}
	};
	subform.ajaxSubmit(options);
	if(isEmpty(path)){
		return null;
	}
	return path;
}


function getInput(id,type,title,value,placeholder,readonly,tip){
	if(value==null){
		value="";
	}
	if(isEmpty(placeholder)){
		placeholder="";
	}
	var disabledStr = "";
	if(!isEmpty(readonly) && readonly){
		disabledStr = 'readonly' ;
	}
	var str='';
	str+='<div class="layui-form-item"><div class="layui-inline">';
	str+='<label style="width: 60px;" class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-inline">';
	if(isEmpty(tip)){
		str+='<input class="layui-input" autocomplete="off" id="'+id+'" type="'+type+'" value="'+value+'" placeholder="'+placeholder+'" '+disabledStr+'>';
	}else{
		str+='<input class="layui-input" autocomplete="off" id="'+id+'" type="'+type+'" value="'+value+'" placeholder="'+placeholder+'" '+disabledStr+' onmouseover="formTips(\''+id+'\',\''+tip+'\')">';
	}
	str+='</div>';
	str+='</div></div>';
	return str;
}

function formTips(id,content){
	layer.tips(content, "#"+id, {
		tips: [2, '#1677FF'],  //1234对应上右下左
		tipsMore: false,
		time: 2000
	});
}

function getOpenUrlButton(id,title,name,url){
	if(url==null){
		url="";
	}
	var str='';
	str+='<div class="layui-form-item"><div class="layui-inline">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-inline">';
	str+='<button onclick="toUrl(\''+url+'\')" class="layui-btn layui-btn-primary layui-btn-sm">'+name+'</button>';
	str+='</div>';
	str+='</div></div>';
	return str;
}

function toUrl(url){
	if(url==null||url=="null"||url==""){
		layer.msg("无文件查看");
		return;
	}
	//window.location.href = url;
	window.open(url);
}

function getTeacherSelect(id,title,tid){
	if(tid==null){
		tid=0;
	}
	var str="";
	$.ajax({
		method : "post",
		url : '../commonData/getTeacherList',
		async:false,
		data:{"teacherId":tid},
		dataType : "json",
		success : function(result) {
			var list=result.result;
			str=getSelectInput(id,title,list,tid);
		}
	});
	return str;
}

function getVideoInput(id,type,title,value){
	var str='';
	str+='<div class="layui-form-item"><div class="layui-inline">';
	str+='<label class="layui-form-label">'+title+'</label>';
	str+='<div class="layui-input-inline" style = "	display: flex;flex-direction: row;align-items:center;">';
	/*	if(value==null){  //onclick="getUpVArea()"
            str+='<input id="'+id+'" type="'+type+'" value="" lay-verify="email" autocomplete="off" class="layui-input">';
            str+='<button type="button" onclick="getUpVArea()" class="layui-btn layui-btn-sm">上传视频</button>';
        }else{ //onclick="getPlayer(\''+value+'\')"
            str+='<input  id="'+id+'" type="'+type+'" value="'+value+'" lay-verify="email" autocomplete="off" class="layui-input">';
            str+='<button type="button" onclick="getPlayer(\''+value+'\')" class="layui-btn layui-btn-sm">查看或修改视频</button>';
        }*/
	str+='<input  id="'+id+'" type="'+type+'" value="'+value+'" lay-verify="email" autocomplete="off" class="layui-input">';
	str+='<button type="button" onclick="getPlayer(\''+value+'\')" class="layui-btn layui-btn-primary layui-btn-sm" style = "margin-left:10px;">查看视频</button>';
	str+='</div>';
	str+='</div></div>';
	return str;
}

function getPlayer(vid){
	if(isEmpty(vid)){
		lsyer.msg("未保存视频id");
		return;
	}
	$('#J_prismPlayer').empty();//id为html里指定的播放器的容器id
	layer.open({
		type: 1,
		title: false,
		closeBtn: 0,
		area: ['840px','480px'],
		shadeClose: true,
		skin: 'layui-layer-nobg', //加上边框
		content: getShowVArea()
	});
	if(vid==null||vid==""){
		return;
	}
	$.ajax({
		url:"../commonData/getVideoPlayAuth",
		type:"post",
		data:{"vid":vid},
		dataType:"json",
		success:function(result){
			$('#videoTitle').html("视频状态:"+result.Status);
			var auth=result.auth;
			if(auth==null||auth==""){
				return;
			}
			//播放凭证播放
			var player = new Aliplayer({
				id: 'J_prismPlayer',
				width: '100%',
				height: '100%',
				barMode: 0,
				autoplay: false,
				useFlashPrism:false,
				useH5Prism:true,
				encryptType:1,
				vid : vid,
				playauth : auth,
				components: [
					{
						name: 'QualityComponent',
						type: AliPlayerComponent.QualityComponent
					},
					{
						name: 'RateComponent',
						type: AliPlayerComponent.RateComponent
					}
				]
			},function(player){
				/*console.log('播放器创建好了。')*/
				/* Register the sourceloaded of the player, query the resolution of the video, invoke the resolution component, and call the setCurrentQuality method to set the resolution. */
				player.on('sourceloaded', function(params) {
					var paramData = params.paramData
					var desc = paramData.desc
					var definition = paramData.definition
					player.getComponent('QualityComponent').setCurrentQuality(desc, definition)
				})
			});
		}
	});
}

function getShowVArea(){
	var str="";
	str+='<div style="display: flex;justify-content:center;align-items:center;margin:0px;" id="J_prismPlayer"></div> ';
	/*str+='<button type="button" onclick="updateVideo()" style="margin-top:30px;margin-left: 880px;margin-right: 50px;" class="layui-btn"> 上传视频 </button>';*/
	return str;
}

function updateVideo(){
	layer.close(layer.index);
	getUpVArea();
}

function getUpVArea(){
	var str="";
	str+='<input id="uploadAuth" type="hidden"/>';
	str+='<input id="uploadAddress" type="hidden"/>';
	str+='<input id="VideoId" type="hidden"/>';

	str+='<div style="padding: 30px;">';
	str+='<div class="layui-form-item"><div class="layui-inline"><div class="layui-input-inline"><input id="videoFiles" type="file" name="videoFiles"/></div></div></div>';
	str+='<div class="layui-form-item" id="upProgress"><div class="layui-inline"><div class="layui-input-inline"><div class="layui-progress layui-progress-big" lay-showpercent="true"><div class="layui-progress-bar" lay-percent="0%" style="width: 0%;"><span class="layui-progress-text">0%</span></div></div></div></div></div>';
	//str+='<div class="layui-form-item"><div class="layui-inline"><div class="layui-input-inline"><select multiple="multiple" id="textareaUpload" style="position:relative; width:700px; height:200px; vertical-align:top; border:1px solid #cccccc;"></select></div></div></div>';
	str+='<div class="layui-form-item"><div class="layui-input-inline"><button type="button" class="layui-btn" id="startvideoUpload" onclick="getUploadParaAndStart()">开始上传</button></div></div>';
	//str+='<div class="layui-form-item"><div class="layui-input-inline"><button class="layui-btn" type="button" id="clearUploadLog" onclick="clearLog()">清空信息</button></div></div>';
	str+='</div>';
	layer.open({
		type: 1,
		skin: 'layui-layer-rim', //加上边框
		area: ['360px', '280px'], //宽高
		content: str
	});
	getVideoUploader();
}

function getProgress(per,title){
	var str ="<div class='layui-inline'>"+title+"<div class='layui-input-inline'>";
	str+='<div class="layui-progress layui-progress-big" lay-showpercent="true">';
	str+='<div class="layui-progress-bar" lay-percent="'+per+'" style="width: '+per+';"><span class="layui-progress-text">'+per+'</span></div>';
	str+='</div></div></div>';
	$("#upProgress").html(str);
}


//////////////////////////////////点播相关

function clickAndSynchAllsection(){
	var str ="";
	str+="同步最近更新过的<input id='synchSectionCount' value='5' type='number' style='width:40px;'/>个点播节(写0个则更新全部)";
	str+='<button type="button" onclick="updateAllSectionVideoInfo()" style="margin-top:10px;margin-left: 270px;margin-right: 10px;" class="layui-btn layui-btn-xs"> 开始 </button>';
	layer.open({
		type: 1,
		skin: 'layui-layer-rim', //加上边框
		area: ['360px', '160px'], //宽高
		content: str
	});
}

function updateAllSectionVideoInfo(){
	var limit=$("#synchSectionCount").val();
	layer.close(layer.index);
	layer.open({
		type: 1,
		skin: 'layui-layer-rim', //加上边框
		area: ['420px', '220px'], //宽高
		content: '<div id="layerDemol" class="layui-layer-content"><div style="padding: 50px 100px;">正在同步视频数据，请稍等</div></div>'
	});
	$.ajax({
		url:"updateAllSectionVideoInfo",
		type:"post",
		data:{"limit":limit},
		dataType:"json",
		success:function(data){
			layer.close(layer.index);
			var result =data.result;
			var sCount=result.sectionCount;
			var eCount=result.errorCount;
			var list=result.errorList;
			var ids=result.errorIdList;
			openError(ids);
			var str="";
			if(eCount>0){
				str+='<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;"><legend>同步信息</legend></fieldset>';
				str+='<div class="layui-form">';
				str+='<table class="layui-table">';
				str+='<colgroup>';
				str+='<col width="150">';
				str+='<col width="150">';
				str+='<col width="200">';
				str+='<col>';
				str+='</colgroup>';
				str+='<thead>';
				str+='<tr>';
				str+='<th>节名称</th>';
				str+='<th>失败原因</th>';
				str+='</tr> ';
				str+='</thead>';
				str+='<tbody>';
				for(var i=0;i<list.length;i++){
					var sta=list[i].Status;
					if(sta=='异常'){
						sta="异常，建议重新上传";
					}
					str+='<tr>';
					str+='<td>'+list[i].sectionName+'</td>';
					str+='<td>'+sta+'</td>';
					str+='</tr>';
				}
				str+='</tbody>';
				str+='</table>';
				str+='</div>';
				layer.open({
					type: 1,
					skin: 'layui-layer-rim', //加上边框
					area: ['520px', '600px'], //宽高
					content: str
				});
				form.render();
			}else{
				var str='';
				str+='<div id="layerDemol" class="layui-layer-content"><div style="padding: 50px 100px;"><h3>共计'+sCount+'个节,全部更新成功</h3></div></div>';
				layer.open({
					type: 1,
					skin: 'layui-layer-rim', //加上边框
					area: ['300px', '200px'], //宽高
					content: str
				});
			}
		}
	});
}

function openError(ids){
	if(ids==null||ids.length==0){
		return ;
	}
	var tn=$.fn.zTree.getZTreeObj("treeArea");
	tn.expandAll(false);
	for(var i=0;i<ids.length;i++){
		var node = tn.getNodeByParam("id",ids[i],null);
		tn.expandNode(node, true, true, true, false);
	}
}


function initPlayer(id,vid,auth,source ,isH5){
	var useFlashPrism=false;
	var useH5Prism =false;
	if(isH5){
		useH5Prism =true;
	}else{
		useFlashPrism = true;
	}

	//播放凭证播放
	var player = new Aliplayer({
		id: id,
		width: '80%',
		height: '80%',
		barMode: 0,
		autoplay: false,
		useFlashPrism :useFlashPrism,
		useH5Prism :useH5Prism,
		source :source,
		vid : vid,
		playauth : auth
	},function(player){
		/*console.log('播放器创建好了。')*/
	});
}

function toPlayBack(url){
	if(url==null||url==""){
		layer.msg("暂无回放");
		return ;
	}
	window.open(url);
}


//加载层
//var index = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2

////loading层
//var index = layer.load(1, { shade: [0.1,'#fff'] });    //0.1透明度的白色背景

//layer.closeAll(); //疯狂模式，关闭所有层

//layer.closeAll('dialog'); //关闭信息框

//layer.closeAll('page'); //关闭所有页面层

//layer.closeAll('iframe'); //关闭所有的iframe层

//layer.closeAll('loading'); //关闭加载层

//layer.closeAll('tips'); //关闭所有的tips层
function openTipPage(msg){
	var str = '';
	str += '<div id="" class="layui-layer-content"><div style="padding:20px;">';
	str += msg;
	str += '</div></div>';
	layer.open({
		type: 1,
		skin: 'layui-layer-demo', //样式类名
		closeBtn: 0, //不显示关闭按钮
		anim: 2,
		shadeClose: true, //开启遮罩关闭
		content: str
	});
}

//下载指定文件
function downloadFile(type){
	var url = "../commonData/downloadFile?";
	url += "type="+type;
// 	window.open(url);
	window.location.href = url;
}

//嵌套折叠面板  开课
function renderNodeData(list,areaId,method){
	var str = '';
	if(list==null||list.length==0){
		$("#"+areaId).html("-");
		return ;
	}
	for(var i=0;i<list.length;i++){
		var obj = list[i];
		str += getCard(obj,method);
	}
	$("#"+areaId).html(str);
//		form.on('switch(questionSwitch)', function(data){
//	  		var code = unlockQuestionGoods(this);
//	  		if(code==0){
//		    		layer.msg((this.checked ? '已开通' : '已取消'), {
//		      			offset: '6px'
//		    		});
//		    		//layer.tips(data.msg, data.othis);
//	  			}
//			});
//	form.render();
	renderDateInput();
}

function renderDateInput(){
	var arr=document.getElementsByName('overDate');
	for(var i=0;i<arr.length;i++){
		laydate.render({
			elem: arr[i] ,
			type: 'datetime' ,
			format: 'yyyy-MM-dd HH:mm:ss' ,
			trigger: 'click'
		});
	}
}

function getCard(obj,method){
	var str = '';
	if(obj ==null){
		return str;
	}
	if(method ==null||method==""){
		str += '<div class="layui-collapse" lay-accordion="">';
		str += 		getBox(obj);
		str += '	<div class="layui-colla-content">';
		str += '	</div>';
		str += '</div>';
	}else{
		str += '<div class="layui-collapse" lay-accordion="">';
//		str += '	<h2 class="layui-colla-title flex-box-crossrange" style="cursor:default">';
		str += '	<h2 class="layui-colla-title flex-box-crossrange corss-center" style="cursor:default;padding:0;">';
		//不多加一层引号  此处传参会由字符串变为对象function
//		str += '		<i class="layui-icon layui-colla-icon" style="cursor:pointer" onclick="getNodeListByParent(\''+obj.id+'\',\''+method+'\')"></i>';
		str += '		<i class="layui-icon layui-colla-icon-new" style="cursor:pointer" onclick="getNodeListByParent(\''+obj.id+'\',\''+method+'\')"></i>';
		str += 				getBox(obj);
		str += '	</h2>';
		str += '	<div class="layui-colla-content" id="'+method+obj.id+'">';
		str += '	</div>';
		str += '</div>';
	}
	return str;
}

function getBox(obj){
	var str = '';
	if(obj ==null){
		return str;
	}
	str += '<div class="layui-inline flex-box-crossrange goods-info">';
	str += '	<div class="goods-name">'+obj.name+'</div>';
	var unlockId = obj.id+"-"+obj.goodsId+"-"+obj.goodsType+"-"+obj.isBuy;
	var userOverDate = obj.overDate=="null" || obj.overDate==null ? "" : obj.overDate;
	str += '	<div class="goods-date-input-area">';
	str += '		<input class="goods-date-input" autocomplete="off" value = "'+userOverDate+'" type="text" name = "overDate" id = '+unlockId+'>';
	str += '		<button type="button" onclick="unlockGoods(\''+unlockId+'\',1)" class="layui-btn layui-btn-xs cm-colorbtn1">开通</button>';
	str += '		<button type="button" onclick="unlockGoods(\''+unlockId+'\',0)" class="layui-btn layui-btn-xs cm-colorbtn2">取消</button>';
	str += '	</div>';
	str += '</div>';
	return str;
}

function getNodeListByParent(pid,method){
	if(pid==null){
		layer.msg("无数据");
	}
	var name = '';
	//防止传递过程中类型变化
	if(method !=null && typeof(method) === 'function'){
		name = method.name;
	}else if(method !=null && typeof(method) === 'string'){
		name = method;
	}else{
		layer.msg("无数据");
	}
	var area = $("#"+name+pid);
	if(area.hasClass("layui-show")){
		area.removeClass("layui-show");
	}else{
//		$.ajax({
//			url:"./getQuestionNodeData",
//			type:"post",
//			async:false,
//			data:{"majorId": commonMajorId,"userId": commonUserId,"nodeType": questionNodeType,"parentId":pid},
//			dataType:"json",
//			success:function(data){
//				var list = data.result;
//				renderNodeData(list,"getNodeListByParent"+pid);
//				area.addClass("layui-show");
//			}
//		});
//		var list = method(pid);
		var list = eval(name+"(\""+pid+"\")");
		renderNodeData(list,name+pid,name);
		area.addClass("layui-show");
	}
}

function isEmpty(obj){
	if (obj === 0){
		return false;
	}
	var f = obj === null || obj === "" || typeof obj === 'undefined';
	return f;
}

function getInputValue(inputId){
	var val = $("#"+inputId).val();
	if(isEmpty(val)){
		return null;
	}
	return val;
}

function getRadioValue(name){
	var val = $('input[name="'+name+'"]:checked').val();
	if(isEmpty(val)){
		return null;
	}
	return val;
}

function getCourseEditArea(addStr){
	var str = '';
	str += '<div style="position:fixed;bottom:0;margin:20px 0 10px 0;width:50%;">';  //background:rgba(255, 255, 255, 1);
	str += '	<div style="display:flex;justify-content:center;align-items:center;">';
	str += addStr;
	str += '	</div>';
	str += '</div>';
	return str;
}

function getSubmitButtonArea(submitFunctionName,cancelFunctionName){
	var str = '';
	str += '<div style="position:fixed;bottom:0;margin:20px 0 20px 0;width:100%;z-index:101">';  //background:rgba(255, 255, 255, 1);
	str += '	<div style="display:flex;justify-content:center;align-items:center;">';
	str += '		<button style="display:flex;" class="layui-btn layui-btn-normal cm-colorbtn1" type="button" onclick="'+ submitFunctionName +'()">保存</button>';
	str += '		<button style="display:flex;" class="layui-btn layui-btn-primary" type="button" onclick="'+ cancelFunctionName +'()">取消</button>';
	str += '	</div>';
	str += '</div>';

	return str;
}

function pageBack(){
//	history.go(-1);
	window.history.back();
}


function getInputValueByName(name){
	var val = $("input[name='"+name+"']").val();
	if(isEmpty(val)){
		return null;
	}
	return val;
}

function getInputValueArrayByName(name){
	var list = $("input[name='"+name+"']");
	if(isEmpty(list)){
		return null;
	}
	var arr = new Array();
	for(var i = 0 ; i < list.length ; i ++){
		arr.push(list[i].value);
	}
	return arr;
}

//成对的范围input
function getScopeInput(type,title,idL,idR,nameL,nameR,valueL,valueR,placeholderL,placeholderR){
	var str = '';
	str+='  <div class="layui-form-item">';
	str+='    <div class="layui-inline">';
	str+='      <label class="layui-form-label">'+title+'</label>';
	str+='      <div class="layui-input-inline" style="width: 100px;">';
	str+='        <input id="'+idL+'" type="'+type+'" name="'+nameL+'" value="'+valueL+'" placeholder="'+placeholderL+'" autocomplete="off" class="layui-input">';
	str+='      </div>';
	str+='      <div class="layui-form-mid">-</div>';
	str+='      <div class="layui-input-inline" style="width: 100px;">';
	str+='        <input id="'+idR+'" type="'+type+'" name="'+nameR+'" value="'+valueR+'" placeholder="'+placeholderR+'" autocomplete="off" class="layui-input">';
	str+='      </div>';
	str+='    </div>';
	str+='  </div>';
	return str;
}

//没有用公共变量的传参  不好看但是好处是同一页面可以用多组
function getPairingInputArea(areaId,multiple,title,arrL,arrR){
	var str = '';
	str += '<div class="layui-inline" style="margin:0 0 16px 0;">';
	var objL = arrL[0];
	var objR = arrR[0];
	if(multiple == 0){   		//只有一对
		str += '<div style="margin:0 6px 10px 0;">'+title+'</div>';
		objL.inputId = objL.inputId + "-" + 0;
		objR.inputId = objR.inputId + "-" + 0;
		str += getPairingInput(multiple,objL,objR);
	}else if(multiple == 1){    //多对可加减
		str += '<div class="layui-inline" style="display:flex;align-items:center;margin:0 0 10px 0;">';
		str += '	<div style="margin:0 12px 0 0">'+title+'</div>';
		str += '	<button type="button" onclick="addPairingInput(\''+areaId+'\',\''+objL.inputId+'\',\''+objR.inputId+'\',\''+objL.type+'\',\''+objR.type+'\',\''+objL.name+'\',\''+objR.name+'\',\''+objL.placeholder+'\',\''+objR.placeholder+'\')" class="layui-btn layui-btn-xs layui-btn-normal">增加</button>';
		str += '</div>';
		str += '<div class="layui-inline" id = "'+areaId+'">';
		for(var i = 0; i < arrL.length ; i ++){
			arrL[i].inputId = arrL[i].inputId + "-" + i;
			arrR[i].inputId = arrR[i].inputId + "-" + i;
			str += getPairingInput(multiple,arrL[i],arrR[i]);
		}
		str += '</div>';
	}
	str += '</div>';
	return str;
}

//成对的范围input
function getPairingInput(multiple,objL,objR){
	var inputIdL = objL.inputId,typeL = objL.type,nameL = objL.name,valueL = objL.value,placeholderL = objL.placeholder;
	var inputIdR = objR.inputId,typeR = objR.type,nameR = objR.name,valueR = objR.value,placeholderR = objR.placeholder;
	var pairingInputId = inputIdL+'_'+inputIdR;
	var str = '';
	str+='  <div class="layui-form-item" style="margin:0 0 0 16px" id="'+pairingInputId+'">';
	str+='    <div class="layui-inline">';
	str+='      <div class="layui-input-inline" style="width: 100px;">';
	str+='        <input id="'+inputIdL+'" type="'+typeL+'" name="'+nameL+'" value="'+valueL+'" placeholder="'+placeholderL+'" class="layui-input">';
	str+='      </div>';
	str+='      <div class="layui-form-mid">-</div>';
	str+='      <div class="layui-input-inline" style="width: 100px;">';
	str+='        <input id="'+inputIdR+'" type="'+typeL+'" name="'+nameR+'" value="'+valueR+'" placeholder="'+placeholderR+'" class="layui-input">';
	str+='      </div>';
	if(multiple == 1){
		str+='      <div style="display:flex;justify-content:center;align-items:center;">';
		str+='        <button type="button" class="layui-btn layui-btn-primary layui-btn-xs" onclick="removePairingInput(\''+pairingInputId+'\')">删除</button>';
		str+='      </div>';
	}
	str+='    </div>';
	str+='  </div>';
	return str;
}

function addPairingInput(areaId,inputIdL,inputIdR,typeL,typeR,nameL,nameR,placeholderL,placeholderR){
	var list = $("input[name='"+nameL+"']");
	if(list.length>=5){
		layer.msg("超过上限");
		return;
	}
	var lastId = '';
	if(list.length>0){
		lastId = list[list.length-1].id;
	}else{
		inputIdL + "-0";
	}
	var lastIndex = Number(lastId.substring(lastId.indexOf("-")+1,lastId.length));
	lastIndex += 1;
	inputIdL = inputIdL + "-" + lastIndex;
	inputIdR = inputIdR + "-" + lastIndex;
	var objL = {"type":typeL,"name":nameL,"inputId":inputIdL,"placeholder":placeholderL,"value":null};
	var objR = {"type":typeR,"name":nameR,"inputId":inputIdR,"placeholder":placeholderR,"value":null};
//	var str = $("#"+areaId).html();
//	str += getPairingInput(1,objL,objR);
//	$("#"+areaId).html(str);
	$("#"+areaId).append(getPairingInput(1,objL,objR));
}

function removePairingInput(pairingInputId){
	$("#"+pairingInputId).remove();
}


function getAdminSelect(id,title,selectIds,multiple){
	if(selectIds==null||selectIds.length==0){
		selectIds=[-1];
	}
	var str="";
	$.ajax({
		method : "post",
		url : '../commonData/getAdminList',
		async:false,
		data:{"selectIds":selectIds},
		dataType : "json",
		success : function(data) {
			var result=data.result;
			var list=result.list;
			var index=result.index;
			if(multiple!=null&&multiple==1){
				str=getMultipleSelectInput(id,title,list,index);
			}else{
				str=getSelectInput(id,title,list,index);
			}
		}
	});
	return str;
}

//把ztre用的数据转为layuiTree的数据
function ztreeToLayuiTree(nodes){
	var nodeList = new Array();
	/* 将外部传入参数 set到本地集合中供渲染*/
	if(nodes != null &&nodes.length>0){
		for (let i in nodes) {
			var nodeId = nodes[i].id;
			nodes[i].title = nodes[i].name;
			if(nodes[i].level==1){
				nodeList.push(nodes[i]);
			}
			if(isEmpty(nodes[i].children)){
				nodes[i].children = new Array();
			}
			for (let j in nodes) {
				if(nodes[j]!=null){
					var pid = nodes[j].pid;
					if(pid==nodeId){
						nodes[i].children.push(nodes[j]);
					}
				}
			}
		}
	}
	return nodeList;
}

function openErrorList(list){
	if(list!=null&&list.length>0){
		var str = "";
		str	+=	'<table class="layui-table">';
		str	+=	'	<colgroup>';
		str	+=	'		<col width="500">';
		str	+=	'		<col width="600">';
		str	+=	'		<col width="200">';
		str	+=	'	</colgroup>';
		str	+=	'<thead>';
		str	+=	' <tr>';
		str	+=	'	<th>错误位置</th>';
		str	+=	'	<th>原因描述</th>';
		str	+=	'	<th>最终处理</th>';
		str	+=	' </tr> ';
		str	+=	'</thead>';
		str	+=	'<tbody>';
		for(var i = 0; i<list.length ; i++){
			str	+=	'<tr>';
			str	+=	'<td>'+list[i].position+'</td>';
			str	+=	'<td>'+list[i].reason+'</td>';
			str	+=	'<td>'+list[i].dispose+'</td>';
			str	+=	'</tr>';
		}
		str	+=	'</tbody>';
		str	+=	'</table>';
		layer.open({
			type: 1,
			skin: 'layui-layer-demo', //样式类名
			closeBtn: 1, //不显示关闭按钮
			anim: 1,
			area: ['810px', '500px'],
			//shadeClose: true, //开启遮罩关闭
			content: str
		});
	}
}


//根据总数及每页数据量计算总页数
function getPageTotal(rowCount, pageSize) {
	if (rowCount == null || rowCount == "") {
		return 0;
	} else {
		if (pageSize != 0 && rowCount % pageSize == 0) {
			return parseInt(rowCount / pageSize);
		}
		if (pageSize != 0 && rowCount % pageSize != 0) {
			return parseInt(rowCount / pageSize) + 1;
		}
	}
}

//去除集合中的值
function removeArrValue(list,removeVal){
	if(isEmpty(list) || list.length == 0 || isEmpty(removeVal) || removeVal.length == 0){
		return list;
	}
	for(let i = 0 ; i < removeVal.length ; i ++){
		var val = removeVal[i];
		var index = list.indexOf(val);
		if(index >= 0){
			// list.remove(index);
			list.splice(index,1);
		}
	}
	return list;
}

//集合去重 es6
function arrUnique(arr) {
	if(isEmpty(arr) || arr.length == 0){
		return ;
	}
	return Array.from(new Set(arr))
}

//把id与参数ids中相同的调整到集合前列
//ids可以为空  返回排好序的集合
function sortListById(ids,list) {
	if(isEmpty(list) || list.length == 0){
		return list;
	}
	if(isEmpty(ids) || ids.length==0){
		ids=[];
	}
	if(!Array.isArray(ids)){
		ids = [ids];
	}
	var arr = new Array();
	//将符合条件的对象
	for (let i = 0 ; i < list.length ; i ++){
		let obj = list[i];
		if(!isEmpty(obj) && ids.indexOf(obj.id) >=0 ){
			arr.push(obj);
			list.splice(i , 1);
		}
	}
	arr = arr.concat(list);
	return arr;
}

//添加或修改ztree的节点
function addOrUpdateTreeNode(treeId,node,isUpdate){
	if(isEmpty(node)){
		return ;
	}
	treeId = treeId.replace("#","");//这里不要#号
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	if(isUpdate){
		// var n = treeObj.getNodeByTId(node.id);
		var n = treeObj.getNodeByParam("id", node.id, null);
		Object.assign(n,node);
		treeObj.updateNode(n);
	}else{
		var pn = null;
		if(!isEmpty(node.pId)){
			pn = treeObj.getNodeByParam("id", node.pId, null);
		}
		node = treeObj.addNodes(pn, node);
	}
}

//传入ztree节点集合  筛选出叶子节点的id
function getCheckedLeafNodesId(ztreeObject){
	var idList = new Array();
	if(isEmpty(ztreeObject)){
		return idList;
	}
	var nodes = ztreeObject.getCheckedNodes(true);
	if(isEmpty(nodes) || nodes.length == 0){
		return null;
	}
	for(var i = 0 ; i < nodes.length ; i++){
		if(!nodes[i].isParent){
			idList.push(nodes[i].id);
		}
	}
	return idList;
}

function getMultiplePicArea(name,title,list){
	var fileUrlPrefix = getFileUrlPrefix();  //获取前缀
	if(isEmpty(list) || list.length==0){
		list=[];
	}
	if(!Array.isArray(list)){
		list = [list];
	}
	var elemId = name + "_elem";
	var listId = name + "_img_list";
	var str = '';
	str += '<div class="layui-upload">';
	str += '	<button type="button" class="layui-btn" id="'+elemId+'">'+title+'</button>';
	str += '	<blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;width: 88%">';
	// str += '	预览图：';
	str += '	<div class="layui-upload-list uploader-list" style="overflow: auto;" id="'+listId+'">';
	if(!isEmpty(list) && list.length >0){
		for(let i = 0 ;i < list.length ; i ++){
			var relativeSrc = list[i];
			var src = fileUrlPrefix + relativeSrc;
			str += '<div id="" class="file-iteme">' ;
			str += '	<div class="handle"><i class="layui-icon layui-icon-delete"></i></div>' ;
			str += '		<img name = "'+name+'" style="width: 100px;height: 100px;" src="'+ src +'" relative-src ="'+ relativeSrc +'">' ;
			str += '	<div class="info">' + eval(i+1) + '</div>' ;
			str += '</div>';
		}
	}
	str += '	</div>';
	str += '	</blockquote>';
	str += '</div>';
	return str ;
}

function initLayMultipleUoload(name,uploadUrl,multiple,uploadModule){
	var fileUrlPrefix = getFileUrlPrefix();  //获取前缀
	var elemId = name + "_elem";
	var listId = name + "_img_list";
	var layUpload = uploadModule.render({
		elem: '#' + elemId
		,url: uploadUrl
		,multiple: multiple
		,before: function(obj){
			layer.msg('图片上传中...', {
				icon: 16,
				shade: 0.01,
				time: 0
			})
		}
		,done: function(res){
			//上传完毕
			layer.close(layer.msg());//关闭上传提示窗口
			//渲染要用全路径
			var relativeSrc = res.result;
			var src = fileUrlPrefix + relativeSrc;
			var str = 	'<div id="" class="file-iteme">' ;
			str += 	'<div class = "handle"><i class="layui-icon layui-icon-delete"></i></div>' ;
			str += 	'<img name = "'+name+'" style="width: 100px;height: 100px;" src = "'+ src +'" relative-src ="'+ relativeSrc +'">' ;
			str += 	'<div class = "info">' + "" + '</div>' ;
			str += 	'</div>';
			//渲染
			$('#'+listId).append(str);
		}
	});
	//点击删除操作
	$(document).on("mouseenter mouseleave", ".file-iteme", function(event){
		if(event.type === "mouseenter"){
			//鼠标悬浮
			$(this).children(".info").fadeIn("fast");
			$(this).children(".handle").fadeIn("fast");
		}else if(event.type === "mouseleave") {
			//鼠标离开
			$(this).children(".info").hide();
			$(this).children(".handle").hide();
		}
	});
	// 删除图片
	$(document).on("click", ".file-iteme .handle", function(event){
		$(this).parent().remove();
	});
	return layUpload;
}

function getAttrByName(type,name,attr){
	var val;
	var dom = $(type+'[name="'+name+'"]');
	if(dom!=null && dom.length == 0){
		val = dom.attr(attr);
	}else if(dom!=null && dom.length > 0){
		val = new Array();
		dom.each(function(){
			val.push($(this).attr(attr));
			// val.push($(this)[attr]);
		});
	}
	return val;
}

function getAttrById(id,attr){
	var dom = $("#" + id);
	var val = dom.attr(attr);
	return val;
}

var commonFileUrlPrefixValue = "";
function getFileUrlPrefix(){
	if(!isEmpty(commonFileUrlPrefixValue)){
		return commonFileUrlPrefixValue;
	}
	commonFileUrlPrefixValue = "";
	$.ajax({
		method : "post",
		type : "post",
		url : '../commonData/getFileUrlPrefix',
		async:false,
		data:{},
		dataType : "json",
		success : function(data) {
			commonFileUrlPrefixValue = data.result;
		}
	});
	return commonFileUrlPrefixValue;
}

function getPic(buttonId,picId,hiddenId,value,srcPic){
    var hiPic = ""
	if (srcPic !== null && srcPic !== undefined && srcPic !== ''){
		hiPic = srcPic.substring(srcPic.indexOf('/upload'))
	}else {
		srcPic = ''
	}
	var str = ''
	str +='<div class="layui-form-item">'
	str +='<label class="layui-form-label">'+value+'</label>'
	str +='<div class="layui-input-inline">'
	str +='<button id="'+buttonId+'" class="layui-btn layui-btn-primary layui-border-blue" style="margin-bottom: 10px;">点击上传</button></br>'
	str +='<img src="'+srcPic+'" id="'+picId+'" style="max-height: 150px;">'
	str +='</div></div><input id="'+hiddenId+'" value="'+hiPic+'" type="hidden" />'
	return str
}

function initLayUpload(buttonId,picId,hiddenId,uploadModule){

	var uplo =uploadModule.render({
		elem: '#'+buttonId
		,url: '../resources/upload'
		,accept: 'images'//允许上传的文件类型，有：images（图片）、file（所有文件）、video（视频）、audio（音频）
		,acceptMime: 'image/*'//打开文件选择框时，筛选出的文件类型
		,size: 2048 //KB
		,before: function(obj){
			layer.load();
			//预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result){
				$('#'+picId).attr('src', result); //图片链接（base64）
			});


		}
		//上传成功
		,done: function(res){
			layer.msg('上传成功',{icon:1});
			var imgPath=res.result[0];
			$("#"+hiddenId).val(imgPath)
			//如果上传失败
			if(res.code !== 0){
				return layer.msg('上传失败',{icon:2});
			}
			layer.closeAll('loading');
		},error: function(index, upload){
			layer.closeAll('loading');
		}
	});



}
