package com.m0smith.firm2018;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class UserInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String user_info_id;

    private String ward;


	public String getUserInfoId() {
		return user_info_id;
	}

	public void setUserInfoId(String id) {
		this.user_info_id = id;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

}
