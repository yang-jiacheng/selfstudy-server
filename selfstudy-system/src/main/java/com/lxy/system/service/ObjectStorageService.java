package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.domain.PageResult;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.dto.ObjectStorageDTO;
import com.lxy.system.po.ObjectStorage;
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

    PageResult<ObjectStorageVO> getObjectStoragePageList(PageDTO pageDTO);

    void deleteObjectStorage(Long id);

    void saveObjectStorage(ObjectStorageDTO objectStorageDTO, Long adminId);
}
