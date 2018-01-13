package com.m0smith.firm2018;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

import java.util.List;

import com.m0smith.firm2018.UserChapters;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserChaptersRepository extends CrudRepository<UserChapters, Long> {

    @Transactional
    Long deleteByChapterAndUserId(String chapter, String id);

    @Transactional
    List<UserChapters> findByUserId(String id);

    @Query(value="select c.user_id, c.chapter, i.ward from  (select user_id, count(chapter) as chapter from user_chapters group by user_id) c INNER JOIN (select ward, user_id from user_info) i WHERE i.user_id = c.user_id;", nativeQuery = true)
    public List<?> findTally();

}

 
