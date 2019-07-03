package com.mybatis.web;

import com.mybatis.dao.MybatisDao;
import com.mybatis.utils.ApplicationContextUtils;



public class test {
	public static void main(String[] args) {
		
		System.out.println(ApplicationContextUtils.getContext());
	    MybatisDao mybatisDao = (MybatisDao) ApplicationContextUtils.getContext().getBean("mybatisDao");
	    
	    System.out.println(mybatisDao);
	}

}
