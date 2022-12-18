/**
 * Copyright 2018-2020 yonyou.com.
 * All rights reserved.
 */
package com.cc.conststar.wall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * <p>
 * 使用方法举例如下：</br>
 * 
 * <pre>
 * Pagenation<User> pagenation = new Pagenation<User>(currentPage, pageSize);
 *
 * pagenation.setTotalCount(countUseList(user));// 将总的记录数赋值给分页对象
 * pagenation.setResultList(getUserListBy(user, pagenation));// 将查询出来的记录赋值给分页对象
 *
 * return pagenation;
 * </pre>
 *
 */
public class Pagenation<T> implements Serializable {

	/** serialVersionUID */
	private static final long	serialVersionUID	= -7088024517966170820L;

	/** 每页展示多少条 */
	private int					pageSize			= 10;

	/** currentPage 当前第几页 */
	private int					currentPage			= 1;

	/** begin */
	private int					begin;

	/** end */
	private int					end;

	/** totalCount 总的记录数 */
	private int					totalCount;

	/** resultList 查询结果列表 */
	private List<T>				resultList;

	public Pagenation() {

	}

	/**
	 * 构造方法
	 * 
	 * @param currentPage 当前第几页
	 * @param pageSize 每页显示多少条记录
	 */
	public Pagenation(int currentPage, int pageSize) {

		this.currentPage = currentPage > 0 ? currentPage : 1;
		this.pageSize = pageSize;
		this.begin = pageSize * (getCurrentPage() - 1);
		this.end = pageSize;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {

		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {

		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {

		this.currentPage = currentPage;
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {

		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(int begin) {

		this.begin = begin;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {

		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {

		this.totalCount = totalCount;
	}

	/**
	 * @return the resultList
	 */
	public List<T> getResultList() {

		return resultList;
	}

	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<T> resultList) {

		this.resultList = resultList;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {

		return end;
	}
}
