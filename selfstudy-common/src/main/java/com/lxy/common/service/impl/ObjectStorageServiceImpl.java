package com.lxy.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxy.common.dto.ObjectStorageDTO;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.po.ObjectStorage;
import com.lxy.common.mapper.ObjectStorageMapper;
import com.lxy.common.service.ObjectStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.OssUtil;
import com.lxy.common.vo.ObjectStorageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 对象存储 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2024-12-14
 */
@Service
public class ObjectStorageServiceImpl extends ServiceImpl<ObjectStorageMapper, ObjectStorage> implements ObjectStorageService {

    @Resource
    private ObjectStorageMapper objectStorageMapper;

    @Override
    public PageInfo<ObjectStorageVO> getObjectStoragePageList(PageDTO pageDTO) {
        //开始分页
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getLimit(),"id desc");
        Page<ObjectStorageVO> pg = (Page<ObjectStorageVO>) objectStorageMapper.getObjectStorageList(pageDTO);
        return new PageInfo<>(pg);
    }

    @Override
    public void deleteObjectStorage(Integer id) {
        ObjectStorage objectStorage = this.getById(id);
        if(objectStorage == null){
            return;
        }
        String downloadUrl = objectStorage.getDownloadUrl();
        if (StrUtil.isNotEmpty(downloadUrl) && downloadUrl.contains(OssUtil.UPLOAD_FOLDER)){
            downloadUrl = downloadUrl.substring(downloadUrl.indexOf(OssUtil.UPLOAD_FOLDER));
            OssUtil.deleteFile(downloadUrl);
        }
        this.removeById(id);
    }

    @Override
    public void saveObjectStorage(ObjectStorageDTO objectStorageDTO,Integer adminId) {
        ObjectStorage objectStorage = new ObjectStorage();
        BeanUtil.copyProperties(objectStorageDTO, objectStorage);
        objectStorage.setCreateTime(new Date());
        objectStorage.setCreatorId(adminId);
        this.save(objectStorage);
    }

}
