var form;
var layer;
var laydate;
var upload;

$(function(){
    layui.use(['layer', 'form' ,'laydate','upload'], function () {
        layer = layui.layer;
        form = layui.form;
        laydate = layui.laydate;
        upload = layui.upload;



        getTree(null);
    });
});

var setting = {
    view: {
        showTitle: false,
        showLine: true,
        showIcon: false,//不展示图标
        selectedMulti: false,
        dblClickExpand: false,
        nameIsHTML: true,
        addDiyDom: addDiyDom,
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom
    },
    edit: {
        enable: true,
        editNameSelectAll: true,
        showRemoveBtn: showRemoveBtn,
        showRenameBtn: showRenameBtn,
        removeTitle: '删除',
        renameTitle: '重命名'
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeEditName: beforeEditName,
        beforeRemove: beforeRemove,
        beforeRename: beforeRename,
        onClick: zTreeOnClick,
        onRemove: onRemove,
        onRename: onRename
    }
};
var tree = [{
    id: 1,
    pId: 0,
    name: "编辑",
    open: true
}];

var log, className = "dark";

function beforeDrag(treeId, treeNodes) {
    return false;
}

function beforeEditName(treeId, treeNode) {
    className = (className === "dark" ? "" : "dark");
    showLog("[ " + getTime() + " beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; "
        + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("treeArea");
    zTree.selectNode(treeNode);
    zTree.editName(treeNode);
    return false;
}

function beforeRemove(treeId, treeNode) {
    console.log(treeNode)
    className = (className === "dark" ? "" : "dark");
    var zTree = $.fn.zTree.getZTreeObj("treeArea");
    zTree.selectNode(treeNode);
    var id = transId(treeNode.id);
    var level = treeNode.level;
    layer.confirm('确定删除吗?', {icon: 2, title: '提示'}, function (index) {
        var reqUrl = ''
        if (level === 0){
            reqUrl = './removeClassify'
        }else {
            reqUrl = './removeCatalog'
        }

        var reqPam = {"id":id}
        ajaxPost(reqUrl,reqPam,function (data){
            if (data.code===0){
                layer.msg("删除成功！",{icon : 1})
                zTree.removeNode(treeNode, false);
                return;
            }
            layer.msg(data.msg,{icon : 2})
        })

        // $.ajax({
        //     url: reqUrl,
        //     type:'POST',
        //     cache:false,//关缓存
        //     async:false,//关闭异步
        //     data:{"id":id},
        //     dataType:"json", //期望服务端返回的数据类型
        //     success:function (data) {
        //         if (data.code===0){
        //             layer.msg("删除成功！",{icon : 1})
        //             zTree.removeNode(treeNode, false);
        //             return;
        //         }
        //         layer.msg(data.msg,{icon : 2})
        //     },error:function (jqXHR, textStatus, errorThrown) {
        //         layer.msg("HTTP状态码:"+jqXHR.status,{icon: 2})
        //     }
        // });
        layer.close(index);
    }, function (index) {
        layer.close(index);
        return false;
    });
    return false;
}

function onRemove(e, treeId, treeNode) {
}

function beforeRename(treeId, treeNode, newName, isCancel) {
    className = (className === "dark" ? "" : "dark");
    showLog((isCancel ? "<span style='color:red'>" : "") + "[ " + getTime()
        + " beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name
        + (isCancel ? "</span>" : ""));
    if (newName.length == 0) {
        setTimeout(function () {
            var zTree = $.fn.zTree.getZTreeObj("treeArea");
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
    var level = treeNode.level;
}

function showRemoveBtn(treeId, treeNode) {
    return true;
}

function showRenameBtn(treeId, treeNode) {
    return false;
}

function showLog(str) {
}

function getTime() {
    var now = new Date(), h = now.getHours(), m = now.getMinutes(), s = now
        .getSeconds(), ms = now.getMilliseconds();
    return (h + ":" + m + ":" + s + " " + ms);
}

function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if ($("#addBtn_" + treeNode.tId).length > 0)
        return;
    if (treeNode.level < 2) {
        var pid = transId(treeNode.id);  //transId(treeNode.id);
        var strt = "新增";
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='" + strt + "' onfocus='this.blur();'></span>";       //点击事件这在这里会被外层覆盖
        sObj.after(addStr);
        var btn = $("#addBtn_" + treeNode.tId);     //这里要把增加节点的操作移到别的按钮
        if (btn) {
            if (treeNode.level < 2) {
                btn.bind("click", function () {
                    // if (treeNode.level === 0) {
                    //     addClassify(Number(pid), treeNode.level + 1, 1,null,belongCourseId);       //ztree的leve从0开始
                    // }else {
                    //
                    // }

                    addCatalog(pid, treeNode.level + 1);
                    return false;//ztree的leve从0开始
                });

            }
        }
    }
}

function removeHoverDom(treeId, treeNode) {
    $("#addBtn_" + treeNode.tId).unbind().remove();
};

function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeArea");
    zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}

//tree点击事件
function zTreeOnClick(event, treeId, treeNode) {
    var commonLevel = treeNode.level;
    var commonId = treeNode.id;
    var commomPid = treeNode.pId;
    changeOpenById(commonId);
    if (commonLevel === 0){
        getClassifyById(transId(commonId))
    }else {
        getCatalogById(transId(commonId),commonLevel + 1)
    }
}

function getCatalogById(id,level){
    $("#catalogArea").html("");
    var str = "";
    ajaxPost('./getCatalogById',{"id":id},function (data) {
        var result = data.result;

        str += getInput("name", "text", "名称", result.name);
        var catalogLevel = result.level
        if (catalogLevel === 2){
            str += getInput("personCount", "number", "容纳人数", result.personCount);
        }
        str += getInput("sort", "number", "排序", result.sort);
        str += getInput("createTime", "text", "创建时间", result.createTime,"",true,"创建时间,不可修改");
        str += getInput("id", "hidden", "", result.id);

        str += getInput("parentId", "hidden", "", result.parentId);
        str += getInput("classifyId", "hidden", "", result.classifyId);
        str += getInput("level", "hidden", "", result.level);
        var buttonStr = "";
        buttonStr += getCatalogBtn()
        str += getCourseEditArea(buttonStr);
        $("#catalogArea").html(str);
        form.render();
    })

}

//获取图书馆根据id
function getClassifyById(id){
    $("#catalogArea").html("");
    var str = "";

    ajaxPost('./getClassifyById',{"id": id},function (data){
        var result = data.result;
        str += getInput("name", "text", "名称", result.name);
        str += getTextarea("description", "描述", result.description);
        str += getInput("sort", "number", "排序", result.sort);
        str += getInput("createTime", "text", "创建时间", result.createTime,"",true,"创建时间,不可修改");
        str += getPic("iconPathBtn","iconPath","iconPathHid","头像",result.iconPath)
        str += getPic("coverPathBtn","coverPathPath","coverPathHid","封面",result.coverPath)
        str += getInput("id", "hidden", "", result.id);
        var buttonStr = "";
        buttonStr += getBtn()
        str += getCourseEditArea(buttonStr);
        $("#catalogArea").html(str);
        initLayUpload("iconPathBtn","iconPath","iconPathHid",upload)
        initLayUpload("coverPathBtn","coverPathPath","coverPathHid",upload)
        form.render();
    })


}

function addClassify() {
    $("#catalogArea").html("");
    var str = '';
    str += getInput("name", "text", "名称");
    str += getTextarea("description", "描述");
    str += getInput("sort", "number", "排序");
    str += getPic("iconPathBtn","iconPath","iconPathHid","头像","")
    str += getPic("coverPathBtn","coverPathPath","coverPathHid","封面","")

    var buttonStr = "";
    buttonStr += getBtn()
    str += getCourseEditArea(buttonStr);
    $("#catalogArea").html(str);
    initLayUpload("iconPathBtn","iconPath","iconPathHid",upload)
    initLayUpload("coverPathBtn","coverPathPath","coverPathHid",upload)
    form.render();
}

function addCatalog(pid,level){

    $("#catalogArea").html("");
    var str = '';
    str += getInput("name", "text", "名称");
    str += getInput("sort", "number", "排序");

    if (level === 1){
        str += getInput("classifyId", "hidden", "", pid);
    }else if(level === 2){
        str += getInput("parentId", "hidden", "", pid);
        str += getInput("personCount", "number", "容纳人数", 0);
    }
    str += getInput("level", "hidden", "", level);
    var buttonStr = "";
    buttonStr += getCatalogBtn()
    str += getCourseEditArea(buttonStr);
    $("#catalogArea").html(str);
    form.render();
}

function updateClassify() {
    var name = $("#name").val();
    var id = $("#id").val();
    var description = $("#description").val();
    var sort = $("#sort").val();
    var iconPathHid = $("#iconPathHid").val();
    var coverPathHid = $("#coverPathHid").val();

    if (isEmpty(name)) {
        layer.msg("请填写名称", {icon: 8})
        return;
    }
    if (sort === ''){
        sort = 0
    }

    var jsonObj = {"name": name, "id": id,"description":description, "sort": sort,"iconPath":iconPathHid,"coverPath":coverPathHid}
    var reqUrl = './updateClassify'
    var reqPam = {"mainJson": JSON.stringify(jsonObj)}

    ajaxPost(reqUrl,reqPam,function (result){
        if (result.code === -1) {
            layer.msg(result.msg, {icon: 2});
        } else {
            layer.msg("修改成功！", {icon: 1})
            getTree(null)
        }
    })

}

function updateCatalog(){
    var name = $("#name").val();
    var id = $("#id").val();
    var sort = $("#sort").val();
    var level = $("#level").val();
    var classifyId = $("#classifyId").val();
    var parentId = $("#parentId").val();
    var personCount = $("#personCount").val();

    var obj = {
        "name": name,"id": id,"sort": sort,"level": level,"classifyId": classifyId,"parentId": parentId,"personCount": personCount
    }

    ajaxPost('./saveCatalog',obj,function (result) {
        if (result.code === 0) {
            layer.msg("修改成功！", {icon: 1})
            getTree(null)
        } else {
            layer.msg(result.msg, {icon: 2});
        }
    })

}

function getBtn() {
    var str = '';
    str += '<div>'
        + '<button onclick="updateClassify()" class="layui-btn comBackCol-brightCyan"  >完成编辑</button>'
        + '</div>'
    return str
}

function getCatalogBtn() {
    var str = '';
    str += '<div>'
        + '<button onclick="updateCatalog()" class="layui-btn comBackCol-brightBrown" >完成编辑</button>'
        + '</div>'
    return str
}

function addDiyDom(treeId, treeNode) {
    var spaceWidth = 5;
    var switchObj = $("#" + treeNode.tId + "_switch"),
        checkObj = $("#" + treeNode.tId + "_check"),
        icoObj = $("#" + treeNode.tId + "_ico");
    switchObj.remove();
    checkObj.remove();
    icoObj.parent().before(switchObj);
    icoObj.parent().before(checkObj);

    var spantxt = $("#" + treeNode.tId + "_span").html();
    if (spantxt.length > 17) {
        spantxt = spantxt.substring(0, 15) + "..."; //最多显示15个字
        $("#" + treeNode.tId + "_span").html(spantxt);
    }
}

function changeOpenById(id) {
    // var tn = $.fn.zTree.getZTreeObj("treeArea");
    // var node = tn.getNodeByParam("id", id, null);
    // node = getGrandNode(node);
    // var flag = false;//node.open?false:true;
    // tn.expandNode(node, flag, true, true, false);
}

//////////////////////


var commonLevel;
var commonTreeId = "treeArea";

function getTree(id) {

    ajaxPost('./getClassifyTree',{},function (result){
        var tree = result.result;
        var r = $.fn.zTree.init($("#" + commonTreeId), setting, tree);
        var nodes = r.getNodes();//getNodesByFilter(filterLevel0);
        $("#selectAll").bind("click", selectAll);
        if (id != null && typeof id != 'undefined') {
            changeOpenById(id);
        }
    })

}

function impoInExcel(level, parentId) {
    var str = "<div class='layui-form' style='text-align: center;'>";
    str += '			<div class="flex-box-endwise corss-center">';

    str += "				<div id='container' style='margin-top: 20px;'>";
    str += "					<button id='startload' class='layui-btn layui-btn-primary'>选择EXCEL</button>";
    str += "				</div>";
    str += '			</div>';
    str += '			<div class="flex-box-crossrange crossrange-center" style = "margin-top:8px;">';
    // str += '				<div class="layui-btn layui-btn-xs cm-colorbtn1" id="stLO" >开始上传</div>';
    // str += '				<div class="layui-btn layui-btn-xs cm-colorbtn2" onclick="downloadFile(1)">下载模板</div>';
    str += '			</div>';
    str += '			<div class="flex-box-crossrange crossrange-center" style = "margin-top:8px;">';
    str += '				<P style="color: red">请先下载模板&nbsp;<button class="layui-btn layui-btn-xs" onclick="uploadExcel()">点击下载</button></P>';
    str += '			</div>';
    str += '	</div>';
    str = addDivClass(str, "layer-edit-content");
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '180px'], //宽高
        content: str
    });
    startload(level, parentId)
    form.render();
}

function uploadExcel() {
    //下载指定文件
    window.location.href = "../collectInfoManage/downloadTemplate?fileName=采集信息类型导入模板.xlsx"
}

function startload(level, parentId) {

    var uploadInst3 = upload.render({
        elem: '#startload'
        , url: '../collectInfoManage/insertInExcelJobType?level=' + level + "&parentId=" + parentId
        // ,auto: false
        // ,bindAction: '#stLO'
        , accept: 'file'//允许上传的文件类型，有：images（图片）、file（所有文件）、video（视频）、audio（音频）
        , acceptMime: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'//打开文件选择框时，筛选出的文件类型
        , before: function (obj) {
            layer.load();
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                console.log(file)
            });
        }
        //上传成功
        , done: function (res) {
            layer.closeAll('loading');
            if (res.code === 0) {

                layer.msg('导入成功', {icon: 6});
                setTimeout(data => {
                    layer.closeAll('page')
                    getTree(null)
                }, 500)
                return
            }
            layer.msg(res.msg, {icon: 5});
        },error: function(index, upload){
            layer.closeAll('loading');
        }
    });

}
