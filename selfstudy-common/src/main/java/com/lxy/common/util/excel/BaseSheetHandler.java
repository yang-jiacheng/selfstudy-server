package com.lxy.common.util.excel;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.annotation.ExcelHeader;
import com.lxy.common.constant.ExcelConstant;
import com.lxy.common.exception.ServiceException;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导入处理器 使用注解驱动字段映射（@ExcelHeader）+ 钩子方法处理特殊逻辑 支持不同实体类的导入，每个实体类可重写 handleCell 和 handleRowEnd 方法
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/09/29 17:30
 */

public abstract class BaseSheetHandler<T> implements SheetHandlerResult<T> {

    /**
     * 解析后的结果
     */
    protected final List<T> resultList = new ArrayList<>();
    protected final Class<T> clazz;
    /**
     * 表头名称 -> 对应字段的映射，通过注解 @ExcelHeader 构建
     */
    protected final Map<String, Field> headerFieldMap = new HashMap<>();
    /**
     * 表头列字母 -> 表头名称的映射，比如 A -> "手机号"
     */
    protected final Map<String, String> titleLetterMap = new HashMap<>();
    /**
     * 当前正在处理的行对象
     */
    protected T currentObj;
    /**
     * 当前处理的 sheet 索引
     */
    protected int sheetIndex;

    public BaseSheetHandler(Class<T> clazz, int sheetIndex) {
        this.clazz = clazz;
        this.sheetIndex = sheetIndex;
        initHeaderFieldMap();
    }

    /**
     * 初始化实体字段映射：将 @ExcelHeader 注解的字段加入 headerFieldMap
     */
    private void initHeaderFieldMap() {
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            ExcelHeader excelHeader = field.getAnnotation(ExcelHeader.class);
            if (excelHeader != null) {
                field.setAccessible(true);
                headerFieldMap.put(excelHeader.title(), field);
            }
        }
    }

    @Override
    public List<T> getResultList() {
        return resultList;
    }

    @Override
    public void startRow(int rowIndex) {
        if (rowIndex == 0) {
            currentObj = null;
            titleLetterMap.clear();
            return;
        }
        try {
            // 每一行创建新的实体对象
            currentObj = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ServiceException("创建对象失败", e);
        }
    }

    @Override
    public void cell(String cellName, String cellValue, XSSFComment xssfComment) {
        // 获取列字母（例如 "A"、"B"）
        String letter = cellName.substring(0, 1);
        // 如果 currentObj == null，则认为是表头行（rowIndex == 0）
        if (currentObj == null) {
            // 保护：忽略空表头单元格
            if (StrUtil.isBlank(cellValue)) {
                return;
            }
            // 记录列字母 -> 表头名称（覆盖同列的重复定义）
            titleLetterMap.put(letter, cellValue);
            return;
        }
        // 数据行：根据列字母找到对应的表头名称，再找到对应的字段，最后赋值
        String title = titleLetterMap.get(letter);
        if (title == null) {
            // 无对应表头，忽略此单元格（可能是多余列）
            return;
        }
        Field field = headerFieldMap.get(title);
        if (field == null) {
            // 实体没有映射到这个表头，忽略
            return;
        }
        // 调用钩子方法，子类可以重写实现特殊逻辑
        handleCell(currentObj, field, cellValue);
    }

    @Override
    public void endRow(int rowIndex) {
        if (currentObj != null) {
            // 导入对象必有 rowIndex 和 sheetIndex 字段，用反射赋值
            ReflectUtil.setFieldValue(currentObj, ExcelConstant.ROW_INDEX, rowIndex);
            ReflectUtil.setFieldValue(currentObj, ExcelConstant.SHEET_INDEX, sheetIndex);
            // 钩子方法，处理默认值等
            handleRowEnd(currentObj);
            resultList.add(currentObj);
            currentObj = null; // 防止意外复用
        }
    }

    /**
     * 钩子方法：处理单元格值 默认实现：直接赋值 子类可重写，如密码加密、日期格式转换等
     *
     * @param obj 当前行对象
     * @param field 当前字段
     * @param cellValue 单元格值
     */
    protected void handleCell(T obj, Field field, String cellValue) {
        try {
            field.set(obj, cellValue);
        } catch (IllegalAccessException e) {
            throw new ServiceException("设置字段值失败: " + field.getName(), e);
        }
    }

    /**
     * 钩子方法：行结束处理 默认实现为空，子类可重写处理： - 默认值 - sheetIndex - 时间字段填充 - 特殊字段逻辑
     */
    protected void handleRowEnd(T obj) {}

}
