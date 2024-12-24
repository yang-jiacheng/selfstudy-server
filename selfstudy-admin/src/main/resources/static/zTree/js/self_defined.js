var layer;
var tempId;
var setting = {
	view : {
		addHoverDom : addHoverDom,
		removeHoverDom : removeHoverDom,
		selectedMulti : false
	},
	edit : {
		enable : true,
		editNameSelectAll : true,
		showRemoveBtn : showRemoveBtn,
		showRenameBtn : showRenameBtn,
		removeTitle:'删除该专业',
		renameTitle:'重命名专业'
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		beforeDrag : beforeDrag,
		beforeEditName : beforeEditName,
		beforeRemove : beforeRemove,
		beforeRename : beforeRename,
		onRemove : onRemove,
		onRename : onRename,
		onClick:getClazzAndLesson
	}
};

var zNodes = [ {
	id : 1,
	pId : 0,
	name : "请添加专业",
	open : true
} ];
var log, className = "dark";
function beforeDrag(treeId, treeNodes) {
	return false;
}
function beforeEditName(treeId, treeNode) {
	className = (className === "dark" ? "" : "dark");
	showLog("[ " + getTime() + " beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.selectNode(treeNode);
	zTree.editName(treeNode);
	return false;
}
function beforeRemove(treeId, treeNode) {
	var tempId = treeNode.id;
	if((typeof tempId=='string')&&tempId.constructor==String){
		//长度为2的时候说明该节点已经持久化了,需要到后端删除
		layer.confirm('您确定要删除该专业和其相关数据么?', {icon: 3, title:'确认提示'}, function(index){
			$.ajax({
				method : "post",
				url : '../professionManager/delProfessions',
				dataType : "json",
				data:{"tempId":treeNode.id},
				success : function(result) {
					layer.close(index);
					if(result.code == -1){
						layer.msg(result.msg,{'icon':0});
					}else{
						layer.msg('删除成功!',{'icon':1});
						refreshTreeNodes();
						return true;
					}
					
				},
				error : function() {
					alert("异步失败");
				}
			});
		});
	}else if(/^[0-9]+.?[0-9]*$/.test(tempId)){
		return true;
	}
	return false;
}
function onRemove(e, treeId, treeNode) {
	className = (className === "dark" ? "" : "dark");
	showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; "
			+ treeNode.name);
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.selectNode(treeNode);
}
function beforeRename(treeId, treeNode, newName, isCancel) {
	className = (className === "dark" ? "" : "dark");
	showLog((isCancel ? "<span style='color:red'>" : "") + "[ " + getTime()
			+ " beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name
			+ (isCancel ? "</span>" : ""));
	if (newName.length == 0) {
		setTimeout(function() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.cancelEditName();
			alert("节点名称不能为空.");
		}, 0);
		return false;
	}
	return true;
}
function onRename(e, treeId, treeNode, isCancel) {
	var node = treeNode.getParentNode();
	var pId = node.id;
	$.ajax({
		method : "post",
		url : '../professionManager/addOrEditProfessions',
		dataType : "json",
		data:{"tempId":treeNode.id,"name":treeNode.name,"pId":pId},
		success : function(result) {
			layer.msg('编辑操作成功!',{'icon':1});
			refreshTreeNodes();
		},
		error : function() {
			alert("异步失败");
		}
	});
}
function showRemoveBtn(treeId, treeNode) {
	// return !treeNode.isFirstNode; 等级为0不显示编辑和删除按钮
	return (treeNode.level != 0 )? true:false;
}
function showRenameBtn(treeId, treeNode) {
	// return !treeNode.isLastNode;
	return (treeNode.level != 0 )? true:false;
}
function showLog(str) {
	if (!log)
		log = $("#log");
	log.append("<li class='" + className + "'>" + str + "</li>");
	if (log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}
function getTime() {
	var now = new Date(), h = now.getHours(), m = now.getMinutes(), s = now
			.getSeconds(), ms = now.getMilliseconds();
	return (h + ":" + m + ":" + s + " " + ms);
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0)
		return;
	if(treeNode.level == 0){
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='新增专业' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_" + treeNode.tId);
		if (btn)
			btn.bind("click", function() {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				zTree.addNodes(treeNode, {
					id : (100 + newCount),
					pId : treeNode.id,
					name : "new node" + (newCount++)
				});
				return false;
			});
	}
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
};
function selectAll() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}

$(document).ready(function() {
	refreshTreeNodes();
	
	layui.use('layer', function(){
		layer = layui.layer;
    });
});
//更新节点
function refreshTreeNodes(){
	$.ajax({
		method : "post",
		url : '../professionManager/getProfessions',
		dataType : "json",
		success : function(result) {
			zNodes = result.allJobs;
			tempId = result.firstMajorId;
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("#selectAll").bind("click", selectAll);
			initPaging(0);
		},
		error : function() {
			alert("异步失败");
		}
	});
}

/*专业点击事件*/
//初始化分页组件
function initPaging(type){
	$.ajax({
		method : "post",
		url : '../professionManager/getSelectedClazzOrLessonCounts',
		data:{"type":type,"tempId":tempId},
		dataType : "json",
		success : function(result) {
			layui.use('laypage', function(){
				  var laypage = layui.laypage;
				  switch(type)
				  {
				      case 1:
				    	  laypage.render({
							  elem: 'paging-div-clazz'
							  ,limit:4
							  ,layout: ['prev', 'page', 'next','skip']
							  ,count: result.selectedClazzCounts //数据总数，从服务端得到
							  ,jump: function(obj, first){
								  getClazzOrLesson(obj.curr,obj.limit,1);
							  }
						  });
				          break;
				      case 2:
				    	  laypage.render({
							  elem: 'paging-div-lesson'
							  ,limit:4
							  ,layout: ['prev', 'page', 'next','skip']
							  ,count: result.selectedLessonCounts //数据总数，从服务端得到
							  ,jump: function(obj, first){
								  getClazzOrLesson(obj.curr,obj.limit,2);
							  }
						  });
				          break;
				      default:
				    	  laypage.render({
							  elem: 'paging-div-clazz'
							  ,limit:4
							  ,layout: ['prev', 'page', 'next','skip']
							  ,count: result.selectedClazzCounts //数据总数，从服务端得到
							  ,jump: function(obj, first){
								  if(first){
									  getClazzOrLesson(obj.curr,obj.limit,0);
								  }else{
									  getClazzOrLesson(obj.curr,obj.limit,1);
								  }
								  
							  }
						  });
					      laypage.render({
							  elem: 'paging-div-lesson'
							  ,limit:4
							  ,layout: ['prev', 'page', 'next','skip']
							  ,count: result.selectedLessonCounts //数据总数，从服务端得到
							  ,jump: function(obj, first){
								  if(!first){
									  getClazzOrLesson(obj.curr,obj.limit,2);
								  }
							  }
						  });
					      break;
				  }
				  
			});
		},
		error : function() {
			alert("异步失败");
		}
	});
}
//获取跟据知识点和标签获取资源
function getClazzOrLesson(current,size,type){
	$.ajax({
		method : "post",
		url : '../professionManager/getSelectedClazzAndLesson',
		dataType : "json",
		data:{"current":current,"size":size,"tempId":tempId,"type":type},
		success : function(result) {
			var selectedClasses;
			var selectedLessones;
			var htmlClazz = "";
			var htmlLesson = "";
			if(type == 0){
				selectedClasses = result.selectedClasses;
				selectedLessones = result.selectedLessones;
				
				for(var idx=0,len=selectedClasses.length;idx < len;idx++){
					htmlClazz += '<div class="col-sm-6 col-md-3">'
									+'<div class="thumbnail">'
										+'<img src="'+selectedClasses[idx].coverPath+'" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
										+'<div class="caption">'
											+'<h3>'+selectedClasses[idx].name+'</h3>'
											+'<p class="bg-info">'+selectedClasses[idx].particpantsNum+'人参与</p>'
										+'</div>'
									+'</div>'
								   +'</div>';
				}
				$('#ls-selected-clazz').html(htmlClazz);
				for(var idx=0,len=selectedLessones.length;idx < len;idx++){
					htmlLesson += '<div class="col-sm-6 col-md-3">'
						+'<div class="thumbnail">'
							+'<img src="'+selectedLessones[idx].coverPath+'" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
							+'<div class="caption">'
								+'<h3>'+selectedLessones[idx].name+'</h3>'
								+'<p class="bg-info">'+selectedLessones[idx].particpantsNum+'人参与</p>'
							+'</div>'
						+'</div>'
					   +'</div>';
				}
				$('#ls-selected-lesson').html(htmlLesson);
			}else if(type == 1){
				selectedClasses = result.selectedClasses;
				for(var idx=0,len=selectedClasses.length;idx < len;idx++){
					htmlClazz += '<div class="col-sm-6 col-md-3">'
									+'<div class="thumbnail">'
										+'<img src="'+selectedClasses[idx].coverPath+'" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
										+'<div class="caption">'
											+'<h3>'+selectedClasses[idx].name+'</h3>'
											+'<p class="bg-info">'+selectedClasses[idx].particpantsNum+'人参与</p>'
										+'</div>'
									+'</div>'
								   +'</div>';
				}
				$('#ls-selected-clazz').html(htmlClazz);
			}else if(type == 2){
				selectedLessones = result.selectedLessones;
				for(var idx=0,len=selectedLessones.length;idx < len;idx++){
					htmlLesson += '<div class="col-sm-6 col-md-3">'
						+'<div class="thumbnail">'
							+'<img src="'+selectedLessones[idx].coverPath+'" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
							+'<div class="caption">'
								+'<h3>'+selectedLessones[idx].name+'</h3>'
								+'<p class="bg-info">'+selectedLessones[idx].particpantsNum+'人参与</p>'
							+'</div>'
						+'</div>'
					   +'</div>';
				}
				$('#ls-selected-lesson').html(htmlLesson);
			}
			
			if(selectedClasses.length == 0){
				for(var idx = 0,len=4;idx<len;idx++){
					htmlClazz += '<div class="col-sm-6 col-md-3">'
						+'<div class="thumbnail">'
							+'<img src="../images/no-data.jpg" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
							+'<div class="caption">'
								+'<p class="bg-info">暂无数据</p>'
							+'</div>'
						+'</div>'
					   +'</div>';
				}
				$('#ls-selected-clazz').html(htmlClazz);
			}
			if(selectedLessones.length == 0){
				for(var idx = 0,len=4;idx<len;idx++){
					htmlLesson += '<div class="col-sm-6 col-md-3">'
						+'<div class="thumbnail">'
							+'<img src="../images/no-data.jpg" alt="拼命加载中..." style="width: 280px;height: 150.5px">'
							+'<div class="caption">'
								+'<p class="bg-info">暂无数据</p>'
							+'</div>'
						+'</div>'
					   +'</div>';
				}
				$('#ls-selected-lesson').html(htmlLesson);
			}
		},
		error : function() {
			alert("异步失败");
		}
	});
}

//专业节点点击回调
function getClazzAndLesson(event, treeId, treeNode){
	//未持久化节点点击无效
	if(/^[0-9]+.?[0-9]*$/.test(treeNode.id)){
		return;
	}
	
	if(treeNode.level == 0){
		//职称节点不跳转
		return;
	}
	tempId = treeNode.id;
	initPaging(0);
}