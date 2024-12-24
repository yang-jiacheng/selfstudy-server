const ossPrefix = "https://selfstudy-server.oss-cn-hangzhou.aliyuncs.com"
const tinyPlugins = 'fullscreen,code,lists,advlist,image,link,kityformula-editor'
const tinyToolbar = 'fullscreen| code | kityformula-editor | fontsizeselect | undo redo | image link| bold italic underline | forecolor backcolor | alignleft aligncenter alignright alignjustify | subscript superscript | bullist numlist '

function removeTinyMce(tinyId){
    // 获取对编辑器实例的引用
    var editor = tinyMCE.get(tinyId);
    // 检查编辑器是否存在
    if (editor) {
        // 使用 remove 方法销毁编辑器
        editor.remove();
    }
}

function getTinyContent(tinyId){
    var mce = tinyMCE.editors[tinyId]
    var str = ''
    if (mce){
        str = mce.getContent();
    }
    return str
}

function getTinyText(tinyId){
    var mce = tinyMCE.editors[tinyId]
    var str = ''
    if (mce){
        str = mce.getContent({ format: 'text' });
    }
    return str
}

function initTiny(tinyId,content) {
    var edit = tinyMCE.get(tinyId)
    if (edit) {
        tinyMCE.editors[tinyId].setContent(content);
        return
    }

    tinymce.init({
        selector: '#' + tinyId,
        branding: false,
        height: 300,
        max_height: 300,
        language: 'zh_CN',
        content_style: "img {max-width:100%;}",
        plugins: tinyPlugins,
        toolbar: tinyToolbar,
        images_upload_handler: function (blobInfo, succFun, failFun, progress) {
            var xhr, formData;
            var file = blobInfo.blob();//转化为易于理解的file对象
            xhr = new XMLHttpRequest();
            xhr.withCredentials = false;
            xhr.open('POST', '../resources/upload');

            xhr.upload.onprogress = function (e) {
                progress(e.loaded / e.total * 100);
            }

            xhr.onload = function () {
                var json;
                if (xhr.status !== 200) {
                    failFun('HTTP Error: ' + xhr.status);
                    return;
                }
                json = JSON.parse(xhr.responseText);
                if (json.code !== 0) {
                    failFun('请求上传接口出现异常');
                    return;
                }
                var urlPath = ossPrefix + json.result[0]
                succFun(urlPath);
            };
            formData = new FormData();
            formData.append('file', file, blobInfo.filename());
            xhr.send(formData);
        },
        menubar: false,
        resize: false,
        statusbar: true,
        init_instance_callback: function (editor) {
            // var content = $("#"+tinyId).html()
            tinyMCE.editors[tinyId].setContent(content);
        }

    });

}