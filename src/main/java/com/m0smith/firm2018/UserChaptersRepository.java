package com.m0smith.firm2018;

import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

import com.m0smith.firm2018.UserChapters;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserChaptersRepository extends CrudRepository<UserChapters, Long> {

    @Transactional
    Long deleteByChapter(String chapter);
}

 
