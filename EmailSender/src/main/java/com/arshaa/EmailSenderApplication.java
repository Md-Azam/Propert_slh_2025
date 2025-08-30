package com.arshaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
@EnableEurekaClient
public class EmailSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSenderApplication.class, args);
	}
	
	
//	@Bean
//	public ITemplateResolver thymeleafTemplateResolver() {
//	    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//	    templateResolver.setPrefix("mail-templates/");
//	    templateResolver.setSuffix(".html");
//	    templateResolver.setTemplateMode("HTML");
//	    templateResolver.setCharacterEncoding("UTF-8");
//	    return templateResolver;
//	}
	

}
