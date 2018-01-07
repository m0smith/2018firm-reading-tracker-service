package com.m0smith.firm2018;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.beans.factory.annotation.Autowired;

import com.m0smith.firm2018.UserInfo;
import com.m0smith.firm2018.UserInfoRepository;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(applicationClass, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

	private static Class<App> applicationClass = App.class;
}

@RestController
class HelloController {
    @Autowired 
    private UserInfoRepository userInfoRepository;
    private UserChaptersRepository userChaptersRepository;
    
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name) {
	
	return "Hi " + name + " !";
	
    }
    
    @RequestMapping(method = RequestMethod.PUT, value="/chapter/{chapter}")
    String chapterRead(@PathVariable String chapter) {
	UserChapters uc = new UserChapters();
	uc.setChapter(chapter);
	//uc.setUserInfoId(id);
	    
	userChaptersRepository.save(uc);
	return "Saved " + chapter;
	
    }
}
