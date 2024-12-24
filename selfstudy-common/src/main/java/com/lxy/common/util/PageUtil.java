package com.lxy.common.util;

import java.util.List;

/**
 * @Description: 分页工具类
 * @author: jiacheng yang.
 * @Date: 2022/3/28 23:17
 * @Version: 1.0
 */

public class PageUtil<T> {
	/**
	 * 当前页码
	 */
	private Integer current = 1;
	/**
	 * 每页显示的信息条数
	 */
	private Integer size = 10;
	/**
	 * 总的信息条数
	 */
	private Integer total=0;
	/**
	 * 总的页数
	 */
	private Integer pages=0;
	/**
	 * 数据集合
	 */
	private List<T> records;

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public PageUtil() {
	}

	public PageUtil(Integer current, Integer size, Integer total, Integer pages, List<T> records) {
		this.current = current;
		this.size = size;
		this.total = total;
		this.pages = pages;
		this.records = records;
	}

	public PageUtil(Integer current, Integer size, Integer total, List<T> records) {
		this.current = current;
		this.size = size;
		this.total = total;
		this.records = records;

		this.pages = total % size == 0 ? ( total / size ) : ( total / size + 1 );

	}
}
