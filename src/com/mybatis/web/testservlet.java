package com.mybatis.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mybatis.dao.MybatisDao;
import com.mybatis.utils.ApplicationContextUtils;

public class testservlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
			System.out.println(ApplicationContextUtils.getContext());
		    MybatisDao mybatisDao = (MybatisDao) ApplicationContextUtils.getContext().getBean("mybatisDao");		
		    System.out.println(mybatisDao);
		    Map parameter=new HashMap();
		    
		    parameter.put("customerId", "cus17900000052003");
		    List<Object> list = mybatisDao.list("taxagency.service.CustomerService_getAccountMonth", parameter);
		    System.out.println(list);
		    
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}
