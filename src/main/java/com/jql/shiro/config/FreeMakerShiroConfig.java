package com.jql.shiro.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

@Component
public class FreeMakerShiroConfig {

	@Autowired
	private FreeMarkerConfigurer config;

	@PostConstruct
	public void setShiroTags() {
		config.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}

}
