package com.mybatis.mapper;


import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@SuppressWarnings("rawtypes")
public class MapperResourcesFactory implements FactoryBean {

	private String[] mapperPaths;

	public void setMapperPaths(String[] mapperPaths) {
		this.mapperPaths = mapperPaths;
	}

	@Override
	public Object getObject() throws Exception {
		Set<Resource> set = new LinkedHashSet<Resource>();
		for (String path : mapperPaths) {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(path);
			for (Resource resource : resources) {
				set.add(resource);
			}
		}
		return set.toArray(new Resource[set.size()]);
	}

	@Override
	public Class getObjectType() {
		return Resource[].class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}