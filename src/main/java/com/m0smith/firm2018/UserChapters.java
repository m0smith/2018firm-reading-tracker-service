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

    private String user_info_id;

    private String chapter;


	public Integer getUserChaptersId() {
		return user_chapters_id;
	}

	public void setUserChaptersId(Integer id) {
		this.user_chapters_id = id;

	}

    	public String getUserInfoId() {
		return user_info_id;
	}

	public void setUserInfoId(String id) {
		this.user_info_id = id;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

}
