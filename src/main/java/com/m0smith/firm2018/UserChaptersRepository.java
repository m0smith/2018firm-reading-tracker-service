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

    @Query(value="select i.ward, count(c.chapter) from (select user_id, chapter from user_chapters) c inner join (select user_id, ward from user_info where user_type = 'youth') i WHERE i.user_id = c.user_id group by i.ward", nativeQuery = true)
    public List<?> findTally();

}

 
