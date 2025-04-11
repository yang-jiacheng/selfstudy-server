const stsUrl = `../resources/getStsToken`;
let ossPath = ""
let compressionSize = 0

// 获取STS Token的函数
async function getStsToken() {
    try {
        const response = await $.ajax({
            url: stsUrl,
            type: 'POST',
            cache: false, // 关闭缓存
            dataType: "json", // 期望服务端返回的数据类型
        });

        if (response.code === 0) {
            ossPath = response.result.ossPath
            compressionSize = response.result.compressionSize
            return response.result.credentials; // 返回有效的凭证
        } else {
            throw new Error(`获取STS Token失败，服务器返回错误 code: ${response.code}`);
        }
    } catch (error) {
        // 捕获到网络请求错误或者业务逻辑错误
        console.error("获取STS Token时发生异常:", error);
        throw error;
    }

}

// 初始化OSS客户端
async function initOssClient() {
    try {
        const stsResponse = await getStsToken(); // 获取临时凭证
        const ossClient = new OSS({
            authorizationV4: true,
            region: 'oss-cn-hangzhou', // OSS区域
            accessKeyId: stsResponse.accessKeyId, // 临时凭证AccessKeyId
            accessKeySecret: stsResponse.accessKeySecret, // 临时凭证AccessKeySecret
            stsToken: stsResponse.securityToken, // 临时凭证SecurityToken
            secure: true, // 使用HTTPS
            refreshSTSToken: async () => {
                const stsResponse = await getStsToken(); // 获取新凭证
                return {
                    accessKeyId: stsResponse.accessKeyId,
                    accessKeySecret: stsResponse.accessKeySecret,
                    stsToken: stsResponse.securityToken,
                };
            },
            refreshSTSTokenInterval: 3500000, // 刷新间隔时间，单位毫秒
            bucket: 'selfstudy-server', // 存储桶名称
        });

        return ossClient;
    } catch (error) {
        console.error("初始化OSS客户端失败", error);
        throw error;
    }
}

//分片上传
async function multipartUploadFile(file,type) {
    const fileName = file.name;

    const partSize = 5 * 1024 * 1024; // 分片大小：5MB


    // 生成文件路径
    var filePath = ''
    if (type === 1){
        filePath = generateFilePath(fileName);
    }else if (type === 2){
        filePath = `android/studyroom/apk/${fileName}`
    }
    const options = {
        partSize: partSize, // 设置分片大小
        parallel: 4, // 设置并发上传的分片数量
        progress: (p) => {
            // console.log(`上传进度：${(p * 100).toFixed(2)}%`);
            updateProgress(p); // 更新进度
        },
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    };
    try {
        showProgressOverlay(); // 显示进度条
        const client = await initOssClient(); // 获取OSS客户端
        // 执行MultipartUpload
        const result = await client.multipartUpload(filePath, file, options);
        // 上传成功后移除进度条
        setTimeout(data => {
            removeProgressOverlay();
            $msg.success('上传成功！');
        }, 500)
        return result;
    } catch (error) {
        removeProgressOverlay(); // 上传失败后移除进度条
        $msg.error(`上传文件失败: ${error.message}`);
        console.error("上传文件失败", error);
        return null;
    }
}

function getOssFileInput(nodeId, id, type, title, value) {
    return `
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">${title}</label>
                <div class="layui-input-inline" >
                    <input id="${id}" type="${type}" value="${value}" autocomplete="off" class="layui-input">
                </div>
                <input style="position: absolute;top: 50px;display: none" id="${nodeId}-file" type="file" accept=".pdf,.docx,.pptx,.xlsx" name="file">
                <button type="button" id="${nodeId}-button" class="layui-btn layui-btn-primary ">选择文件</button>
            </div>
        </div>
    `;
}


/**
 *
 * @param id 文件选择器的id
 * @param btnId 按钮id
 * @param inpId 上传成功后显示的input的id
 * @param type 1 文件 2 APK
 * @param allowedTypes 文件类型数组
 * @param maxSizeMB 文件大小限制 MB
 */
function listeningFileInput(id,btnId,inpId,type,allowedTypes,maxSizeMB) {
    // 点击按钮时，触发隐藏的文件选择器的click事件
    $(`#${btnId}`).on('click', function () {
        $(`#${id}`).click();
    });
    $(`#${id}`).on('change', async function (e) {
        let file = e.target.files[0];
        if (file) {
            const fileSize = file.size;
            //默认5MB
            if (!maxSizeMB){
                maxSizeMB = 5;
            }
            const maxSize = maxSizeMB * 1024  * 1024;
            // 检查文件大小
            if (fileSize > maxSize) {
                $(this).val(''); // 清空文件选择器
                $msg.warning(`文件大小不能超过${maxSizeMB}MB！`);
                return;
            }
            // // 校验文件类型
            if (allowedTypes && allowedTypes.length > 0){
                const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();
                if (!allowedTypes.includes(fileExtension)) {
                    $(this).val(''); // 清空文件选择器
                    $msg.warning(`只允许上传 ${allowedTypes.join(', ')} 文件！`);
                    return;
                }
            }

            //压缩图片
            if (fileSize >= compressionSize && file.type.startsWith('image/')){
                file = await compressImageFile(file);
            }

            const result = await multipartUploadFile(file,type);
            $(this).val(''); // 清空文件选择器
            if (result){
                var path=result.res.requestUrls[0]
                var num = path.lastIndexOf("?")
                if(num>0){
                    path = path.substring(path.indexOf("aliyuncs.com/") + 12,num)
                }else {
                    path = path.substring(path.indexOf("aliyuncs.com/") + 12)
                }
                const inpDom = $(`#${inpId}`)
                inpDom.val(ossPath+path)
                inpDom.attr('src',ossPath+path)
            }
        }
    });
}

/**
 * 压缩图片并返回新的 File 对象
 * @param {File} inputFile 输入的图片文件
 * @param {number} [scale=0.9] 缩放比例（0-1）
 * @param {number} [quality=0.6] 输出质量（0-1）
 * @returns {Promise<File>} 压缩后的 File 对象
 */
async function compressImageFile(inputFile, scale = 0.9, quality = 0.7) {
    let imageBitmap = null;
    let canvas = null;

    try {
        // 1. 解码图片为 ImageBitmap（独立内存区）
        imageBitmap = await createImageBitmap(inputFile);

        // 2. 创建 OffscreenCanvas 绘制缩放后图像
        canvas = new OffscreenCanvas(
            imageBitmap.width  * scale,
            imageBitmap.height  * scale
        );
        const ctx = canvas.getContext('2d');
        ctx.drawImage(imageBitmap,  0, 0, canvas.width,  canvas.height);



        // 3. 转换为压缩后的 Blob
        const compressedBlob = await canvas.convertToBlob({
            type: inputFile.type.startsWith('image/')  ? inputFile.type  : 'image/jpeg',
            quality
        });

        // 4. 生成新 File 对象（保留原始文件名+时间戳）
        return new File([compressedBlob], `compressed_${Date.now()}_${inputFile.name}`,  {
            type: compressedBlob.type,
            lastModified: Date.now()
        });

    } catch (error) {
        console.error(' 压缩失败:', error);
        throw error;
    } finally {
        if (imageBitmap) {
            // 释放 ImageBitmap 内存
            imageBitmap.close();
            imageBitmap = null;
        }
        if (canvas) {
            // 释放 Canvas 内存
            canvas = null;
        }
    }
}


function generateFilePath(fileName) {
    if (!fileName || fileName.trim()  === '') return '';
    const today = new Date().toISOString().split('T')[0]; // 获取当前日期 (yyyy-MM-dd)
    // 拆分文件名和扩展名
    const lastDotIndex = fileName.lastIndexOf('.');
    const namePart = lastDotIndex !== -1
        ? fileName.slice(0,  lastDotIndex)
        : fileName;
    const suffix = lastDotIndex !== -1
        ? fileName.slice(lastDotIndex)
        : '';

    // 截取前15个字符（兼容特殊符号）
    const truncatedName = namePart.replace(/[^\w-]/g,  '_') // 非字母数字转下划线
        .substring(0, 15)
        .trim() || 'file'; // 空文件名兜底

    const currentTime = Date.now(); // 获取当前时间戳

    return `upload/${today}/${truncatedName}_${currentTime}${suffix}`;
}

// 创建并显示进度条的方法
function showProgressOverlay() {
    // 检查是否已存在进度条，如果存在则不再重复创建
    if ($('#progress-overlay').length > 0) return;

    // 创建并添加蒙版和进度条
    const overlay = $('<div>', {
        id: 'progress-overlay',
        css: {
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            background: 'rgba(0, 0, 0, 0.7)',
            zIndex: '9999',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }
    });

    // 创建进度条容器
    const progressContainer = $('<div>', {
        css: {
            textAlign: 'center',
            color: '#fff',
        }
    });

    // 创建环形进度条
    const progressBar = $('<div>', {
        class: 'layui-progress',
        'lay-showpercent': 'true',
        css: {
            width: '240px',
            marginBottom: '10px',
        }
    });

    const progressBarInner = $('<div>', {
        class: 'layui-progress-bar',
        'lay-percent': '0%',
        css: {
            width: '0%',
            backgroundColor: '#818fee',
        }
    });

    // 创建上传进度文本
    const progressText = $('<p>', {
        id: 'progressText',
        css: {
            marginTop: '10px',
            fontSize: '14px',
        },
        text: '上传进度: 0%',
    });

    // 组装元素
    progressBar.append(progressBarInner);
    progressContainer.append(progressBar);
    progressContainer.append(progressText);
    overlay.append(progressContainer);

    // 将元素添加到页面
    $('body').append(overlay);
}

// 更新进度条的方法
function updateProgress(progress) {
    const progressBar = $('.layui-progress-bar');
    const progressText = $('#progressText');
    const progressValue = (progress * 100).toFixed(2); // 转化为百分比

    if (progressBar.length > 0 && progressText.length > 0) {
        progressBar.attr('lay-percent', `${progressValue}%`);
        progressBar.css('width', `${progressValue}%`);
        progressText.text(`上传进度: ${progressValue}%`);
    }

    // // 如果上传进度为100%，停留0.5秒再移除进度条
    // if (progress === 1) {
    //     setTimeout(removeProgressOverlay, 500); // 0.5秒后移除进度条
    // }
}

// 移除进度条和蒙版的方法
function removeProgressOverlay() {
    $('#progress-overlay').remove();
}

//使用示例
// function getCatalog(id) {
//     $("#catalogArea").html("");
//     commonNodeId = id;
//     var str = "";
//     $.ajax({
//         url:"./getTextbookMaterialById",
//         type:"post",
//         async:false,
//         data:{"id":id},
//         dataType:"json",
//         success:function(data){
//             str += getOssFileInput(id, "filePath", "text", "文件路径", catalogInfo.filePath);
//         }
//     });
//     $("#catalogArea").html(str);
//     form.render();
//     listeningFileInput(`${id}-file`,`${id}-button`, "filePath")
// }
