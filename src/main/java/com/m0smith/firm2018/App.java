package com.m0smith.firm2018;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.beans.factory.annotation.Autowired;

import com.m0smith.firm2018.UserInfo;
import com.m0smith.firm2018.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    @Autowired 
    private UserChaptersRepository userChaptersRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name) {
	
	return "Hi " + name + " !";
	
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/reg")
    String register(@RequestParam("ward") String ward,
		    @RequestParam("user_type") String user_type
		    ) {
	UserInfo ui = new UserInfo();
	ui.setWard(ward);
	ui.setUserType(user_type);
	userInfoRepository.save(ui);
	return "Saved " + user_type;
	
    }
    @RequestMapping(method = RequestMethod.PUT, value="/read")
    String chapterRead(@RequestParam("chapter") String chapter) {
	UserChapters uc = new UserChapters();
	uc.setChapter(chapter);
	//uc.setUserInfoId(id);
	userChaptersRepository.save(uc);
	return "Saved " + chapter;
	
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/read/{chapter}")
    void chapterNotRead(@PathVariable("chapter") String chapter) {
	logger.error("chapterNotRead: " + chapter);
	Long rtnval = userChaptersRepository.deleteByChapter(chapter);
	logger.error("Delete count:" + rtnval);

	
    }

    @RequestMapping(path="/read")
    public @ResponseBody Iterable<String> getAllUsers() {
	// This returns a JSON or XML with the users

	List<String> rtnval = new ArrayList<String>();
	for( UserChapters uc :userChaptersRepository.findAll()) {
	    rtnval.add(uc.getChapter());
	}
	return rtnval;
    }
}
