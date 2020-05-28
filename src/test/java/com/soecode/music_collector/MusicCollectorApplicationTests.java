package com.soecode.music_collector;

import com.soecode.music_collector.gecco.spring.SpringGeccoEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.geccocrawler.gecco.GeccoEngine;

@SpringBootApplication
@Configuration
public class MusicCollectorApplicationTests {

	@Bean
	public SpringGeccoEngine initGecco() {
		return new SpringGeccoEngine() {
			@Override
			public void init() {
				GeccoEngine.create()
						.pipelineFactory(springPipelineFactory)
						.classpath("com.geccocrawler.gecco.test")
						.start("https://weixin.sogou.com/weixin?type=2&query=%E5%91%A8%E6%9D%B0%E4%BC%A6")
						.interval(3000)
						.loop(true)
						.start();
			}
		};
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MusicCollectorApplicationTests.class, args);
	}

}
