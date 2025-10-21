package com.lxy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.exception.ServiceException;
import com.lxy.common.util.DateCusUtil;
import com.lxy.common.util.EncryptUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.ThreadPoolUtil;
import com.lxy.common.util.excel.BaseSheetHandler;
import com.lxy.common.util.excel.ExcelUtil;
import com.lxy.system.dto.UserPageDTO;
import com.lxy.system.mapper.UserMapper;
import com.lxy.system.po.User;
import com.lxy.system.service.RedisService;
import com.lxy.system.service.UserService;
import com.lxy.system.vo.ExcelErrorInfoVO;
import com.lxy.system.vo.user.UserExportVO;
import com.lxy.system.vo.user.UserImportVO;
import com.lxy.system.vo.user.UserRankVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisService redisService;

    @Resource
    private UserMapper userMapper;

    /**
     * 校验解析的用户数据是否正确
     */
    private static List<ExcelErrorInfoVO> checkUserList(List<UserImportVO> userList) {
        List<ExcelErrorInfoVO> errorList = new ArrayList<>();
        if (CollUtil.isEmpty(userList)) {
            errorList.add(new ExcelErrorInfoVO("sheet", "数据为空", "请检查文件内容是否正确"));
            return errorList;
        }
        for (UserImportVO user : userList) {
            //具体哪一行
            int rowIndex = user.getRowIndex() + 1;
            //当前sheet编号
            int sheetIndex = user.getSheetIndex() != null ? user.getSheetIndex() : 1;
            //判断昵称是否为空
            if (StrUtil.isEmpty(user.getName())) {
                errorList.add(new ExcelErrorInfoVO("第" + sheetIndex + "sheet ,第" + rowIndex + "行", "昵称为空", "此行略过"));
            }
            //判断手机号是否为空,校验手机号
            if (StrUtil.isEmpty(user.getPhone())) {
                errorList.add(new ExcelErrorInfoVO("第" + sheetIndex + "sheet 第" + rowIndex + "行", "手机号为空", "此行略过"));
            } else {
                if (!PhoneUtil.isMobile(user.getPhone())) {
                    errorList.add(new ExcelErrorInfoVO("第" + sheetIndex + "sheet ,第" + rowIndex + "行", "手机号格式不正确", "此行略过"));
                }
            }
        }

        return errorList;
    }

    @Override
    public Page<User> getUserPageList(UserPageDTO dto) {
        Integer page = dto.getPage();
        Integer limit = dto.getLimit();
        Page<User> pg = new Page<>(page, limit);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getId);
        if (StrUtil.isNotBlank(dto.getName())) {
            wrapper.like(User::getName, dto.getName());
        }
        if (StrUtil.isNotBlank(dto.getPhone())) {
            wrapper.like(User::getPhone, dto.getPhone());
        }

        if (StrUtil.isNotEmpty(dto.getStartTime())) {
            wrapper.ge(User::getCreateTime, dto.getStartTime());
        }

        if (StrUtil.isNotEmpty(dto.getEndTime())) {
            wrapper.le(User::getCreateTime, dto.getEndTime());
        }

        pg = this.page(pg, wrapper);

        List<User> records = pg.getRecords();
        for (User user : records) {
            user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
            user.setCoverPath(ImgConfigUtil.joinUploadUrl(user.getCoverPath()));
        }
        pg.setRecords(records);
        return pg;
    }

    @Override
    public boolean saveUser(User user) {
        boolean flag = false;
        Integer id = user.getId();
        if (id == null) {
            User user2 = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()));
            if (user2 != null) {
                return false;
            }
            user.setRegistType(2);
            flag = this.save(user);
        } else {
            User user2 = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()).ne(User::getId, user.getId()));
            if (user2 != null) {
                return false;
            }
            user.setUpdateTime(new Date());
            flag = this.updateById(user);
            //删缓存
            this.removeUserInfoCache(user.getId());
        }
        return flag;
    }

    @Override
    public User getUserByPhoneAndPassword(String phone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone).eq(User::getPassword, password);
        return this.getOne(wrapper);
    }

    @Override
    public boolean submitStudyDuration(Integer userId, Integer duration) {
        boolean flag = false;
        User user = this.getById(userId);
        if (user != null) {
            //总时长累加
            Integer totalDuration = user.getTotalDuration() == null ? 0 : user.getTotalDuration();
            totalDuration += duration;
            user.setTotalDuration(totalDuration);
            //更新用户
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId, user.getId())
                    .set(User::getTotalDuration, totalDuration)
                    .set(User::getUpdateTime, new Date());
            flag = this.update(wrapper);
            //删缓存
            this.removeUserInfoCache(userId);
        }
        return flag;
    }

    @Override
    public User getUserInfo(Integer userId) {
        User user = this.getUserInfoCache(userId);
        if (user != null) {
            return user;
        }
        user = this.getById(userId);
        if (user != null) {
            user.setPassword("");
            user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
            user.setCoverPath(ImgConfigUtil.joinUploadUrl(user.getCoverPath()));
            //加缓存
            this.insertUserInfoCache(user);
        }
        return user;
    }

    @Override
    public User getUserInfoCache(Integer userId) {
        String key = RedisKeyConstant.getUserInfo(userId);
        User user = redisService.getObject(key, User.class);
        return user;
    }

    @Override
    public void removeUserInfoCache(Integer userId) {
        String key = RedisKeyConstant.getUserInfo(userId);

        redisService.deleteKey(key);

    }

    @Override
    public void removeUserInfoCacheByIds(List<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            Set<String> keys = new HashSet<>(ids.size());

            ids.forEach(id -> {
                keys.add(RedisKeyConstant.getUserInfo(id));

            });
            redisService.deleteKeys(keys);

        }
    }

    @Override
    public void insertUserInfoCache(User user) {
        String key = RedisKeyConstant.getUserInfo(user.getId());
        //缓存 7天
        redisService.setObject(key, user, 604800L, TimeUnit.SECONDS);
    }

    @Override
    public List<UserRankVO> getRankingsTotalDuration() {
        List<UserRankVO> list = this.getRankingsTotalDurationCache();
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        list = this.getRankingsTotalDurationInDb();
        return list;
    }

    @Override
    public List<UserRankVO> getRankingsTotalDurationInDb() {
        List<UserRankVO> list = userMapper.getRankingsTotalDuration();
        if (CollUtil.isNotEmpty(list)) {
            //移除0学习时长
            list.removeIf(user -> user.getTotalDuration() == null || user.getTotalDuration() == 0);
            //按时长倒序
            list.sort((t1, t2) -> t2.getTotalDuration().compareTo(t1.getTotalDuration()));
            //设置用户头像和排名
            UserRankVO rankVO = null;
            for (int i = 0; i < list.size(); i++) {
                rankVO = list.get(i);
                rankVO.setProfilePath(ImgConfigUtil.joinUploadUrl(rankVO.getProfilePath()));
                rankVO.setRanking(i + 1);
                list.set(i, rankVO);
            }
        }
        return list;
    }

    @Override
    public void insertRankingsCache(List<UserRankVO> users) {
        //缓存到今天结束
        long endByDay = DateCusUtil.getEndByDay();
        String key = RedisKeyConstant.getRankings();
        redisService.setObject(key, users, endByDay, TimeUnit.SECONDS);
    }

    @Override
    public UserRankVO getUserRankingById(Integer userId) {
        String studyDay = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
        UserRankVO user = userMapper.getUserRankingById(userId, studyDay);
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatchUser(List<User> userList) {
        List<List<User>> batchList = CollUtil.split(userList, 2000);
        for (List<User> batch : batchList) {
            userMapper.insertBatchUser(batch);
        }
    }

    @Override
    public List<UserExportVO> exportUserInExcel(UserPageDTO dto) {
        return userMapper.exportUserInExcel(dto);
    }

    @Override
    public List<ExcelErrorInfoVO> importUsersInExcel(MultipartFile file) {
        // 方式1：使用注解驱动（推荐）
//        List<UserImportVO> usersVo = ExcelUtil.importExcel(file, UserImportVO.class);

        // 方式2：使用自定义处理器（兼容原有方式）
        List<UserImportVO> usersVo = ExcelUtil.importExcel(file, UserSheetHandler::new);

        List<ExcelErrorInfoVO> errorList = checkUserList(usersVo);
        if (CollUtil.isNotEmpty(errorList)) {
            return errorList;
        }
        List<User> users = BeanUtil.copyToList(usersVo, User.class);
        UserServiceImpl proxy = (UserServiceImpl) AopContext.currentProxy();
        proxy.insertBatchUser(users);
        return errorList;
    }

    private List<UserRankVO> getRankingsTotalDurationCache() {
        String key = RedisKeyConstant.getRankings();
        List<UserRankVO> list = redisService.getObject(key, ArrayList.class);
        return list;
    }

    @Override
    public void test() {
        UserServiceImpl proxy = (UserServiceImpl) AopContext.currentProxy();
        ThreadPoolUtil.execute(() -> {
//            transactionTemplate.execute(status -> {
//                c();
//                return null;
//            });

            int attempt = 0;
            int maxRetries = 2;
            int maxDelay = 5000;
            String methodName = "test";
            boolean success = false;

            while (attempt <= maxRetries && !success) {
                try {
                    if (attempt > 0) {
                        log.error("{}：第{}次重试", methodName, attempt);
                    }
                    attempt++;
                    proxy.c();
                    success = true;  // 如果没有抛异常，表示操作成功
                } catch (Exception e) {
                    log.error("{}：操作异常,数据：{}", methodName, "11");
                    log.error(e.getMessage(), e);
                    if (attempt <= maxRetries) {
                        long delay = (long) (Math.pow(2, attempt) * 1000);  // 指数退避
                        long finalDelay = Math.min(delay, maxDelay);
                        log.error("{}：延迟{}毫秒后重试", methodName, finalDelay);
                        ThreadUtil.sleep(finalDelay, TimeUnit.MILLISECONDS);
                    }
                }
            }

            log.error("{}：重试{}次, 执行结果：{}", methodName, (attempt - 1), success);

//            RetryUtil.retryOperation(3,5000,() -> {
//                proxy.c();
//                return null;
//            },"test","11");

        });


    }

    //    @Transactional(rollbackFor = Exception.class)
    public void c() {
        User user = new User();
        user.setId(108);
        user.setName("test");
        this.updateById(user);
        throw new RuntimeException("test");
    }

    public void b(UserServiceImpl proxy) {
        proxy.c();
    }

    /**
     * 用户excel处理器 - 使用BaseSheetHandler
     * 展示如何使用注解驱动的方式处理Excel导入
     */
    public static class UserSheetHandler extends BaseSheetHandler<UserImportVO> {

        public UserSheetHandler(int sheetIndex) {
            super(UserImportVO.class, sheetIndex);
        }

        /**
         * 重写单元格处理逻辑
         * 处理密码加密等特殊逻辑
         */
        @Override
        protected void handleCell(UserImportVO obj, Field field, String cellValue) {
            try {
                String fieldName = field.getName();

                // 处理换行符
                if (StrUtil.isNotEmpty(cellValue)) {
                    cellValue = cellValue.replace("\n", "<br>").replace("\r", "<br>");
                }

                // 密码字段需要加密
                if ("password".equals(fieldName)) {
                    if (StrUtil.isNotEmpty(cellValue)) {
                        field.set(obj, EncryptUtil.encryptSha256(cellValue));
                    }
                } else {
                    // 其他字段直接赋值
                    field.set(obj, cellValue);
                }
            } catch (IllegalAccessException e) {
                throw new ServiceException("设置字段值失败: " + field.getName());
            }
        }

        /**
         * 重写行结束处理逻辑
         * 设置默认值、时间戳等
         */
        @Override
        protected void handleRowEnd(UserImportVO obj) {
            // 设置默认头像和封面
            String defPath = "/upload/defPath.jpg";
            String defCover = "/upload/defCover.jpg";
            obj.setProfilePath(defPath);
            obj.setCoverPath(defCover);

            // 设置注册类型为后台添加
            obj.setRegistType(2);

            // 设置总学习时长
            obj.setTotalDuration(0);

            // 设置时间戳
            Date now = new Date();
            obj.setCreateTime(now);
            obj.setUpdateTime(now);

            // 设置Sheet索引
            obj.setSheetIndex(sheetIndex);

            // 如果密码为空，设置默认密码
            if (StrUtil.isEmpty(obj.getPassword())) {
                obj.setPassword(EncryptUtil.encryptSha256("123456"));
            }
        }
    }

}
