package com.lxy.common.service;

import com.github.pagehelper.PageInfo;
import com.lxy.common.dto.ObjectStorageDTO;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.po.ObjectStorage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.vo.ObjectStorageVO;

/**
 * <p>
 * 对象存储 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2024-12-14
 */
public interface ObjectStorageService extends IService<ObjectStorage> {

    PageInfo<ObjectStorageVO> getObjectStoragePageList(PageDTO pageDTO);

    void deleteObjectStorage(Integer id);

    void saveObjectStorage(ObjectStorageDTO objectStorageDTO,Integer adminId);
}
