package com.m0smith.firm2018;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class UserChapters {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer user_chapters_id;

    private String userId;

    private String chapter;


	public Integer getUserChaptersId() {
		return user_chapters_id;
	}

	public void setUserChaptersId(Integer id) {
		this.user_chapters_id = id;

	}

    	public String getUserId() {
		return userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

}
