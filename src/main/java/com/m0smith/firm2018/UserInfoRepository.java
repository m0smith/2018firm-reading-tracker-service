package com.m0smith.firm2018;

import org.springframework.data.repository.CrudRepository;

import com.m0smith.firm2018.UserInfo;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    List<UserInfo> findByUserId(String id);

}

 
