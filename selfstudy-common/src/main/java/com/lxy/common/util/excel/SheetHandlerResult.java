package com.lxy.common.util.excel;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;

import java.util.List;

public interface SheetHandlerResult<T>
        extends XSSFSheetXMLHandler.SheetContentsHandler {
    List<T> getResultList();
}
