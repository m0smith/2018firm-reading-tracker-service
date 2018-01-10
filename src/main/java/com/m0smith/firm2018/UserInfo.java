package com.m0smith.firm2018;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class UserInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer user_info_id;

    private String userId;

    private String ward;

    private String user_type;


    public Integer getUserInfoId() {
	return user_info_id;
    }
    
    public void setUserInfoId(Integer id) {
	this.user_info_id = id;
    }
    
    public String getWard() {
	return ward;
    }
    
    public void setWard(String ward) {
	this.ward = ward;
    }

    public String getUserId() {
	return userId;
    }
    
    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getUserType() {
	return user_type;
    }
    
    public void setUserType(String userType) {
	this.user_type = userType;
    }

}
