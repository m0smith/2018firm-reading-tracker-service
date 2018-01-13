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

    @Query(value="select ward, count(user_id), sum(chapter) from (select i.ward, i.user_id, count(c.chapter) as chapter from user_info as i inner join user_chapters as c on i.user_id = c.user_id where user_type ='youth' group by i.ward, i.user_id) as v group by ward;", nativeQuery = true)
    public List<?> findTally();
    //
}

 
