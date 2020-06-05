package com.soecode.music_collector.handler;


import com.soecode.music_collector.util.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigure {

	@Bean
	public SpringPipelineFactory springPipelineFactory() {
		return new SpringPipelineFactory();
	}

	@Bean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}

	@Bean(name="myPipeline")
	public MyPipeline consolePipeline() {
		return new MyPipeline();
	}
}
