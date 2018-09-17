/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.service.admin.users;

import com.smattnews.config.Constants;
import com.smattnews.daos.UserRepository;
import com.smattnews.models.User;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@Service
public class UsersService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private EntityManager entityManager;

    public UsersService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    /**
     * This will get all the users that are registered on the system
     * @return Map
     */
    public Map<String, Object> allUsers(Optional<String> limit,  Optional<String > offset) {

        Map<String, Object> response = new HashMap<>();

        Query usersQuery = entityManager.createNativeQuery("SELECT u.id_string as id, u.email, u.first_name, " +
                "u.last_name, u.bio, u.created_at as registered_on FROM users u")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        int realLimit = Integer.parseInt(limit.orElse(Constants.MAX_RECORD_PER_PAGE + ""));
        int realOffset = 0;

        //the offset behaves as a page
        //i.e. offset of 1 means send me page 1, 2 means send me page 2 and so on...
        if(Integer.parseInt(offset.orElse("0")) > 0) {
            realOffset = Integer.parseInt(offset.orElse("0")) * realLimit;
        }

        usersQuery.setFirstResult(realOffset);
        usersQuery.setMaxResults(realLimit);
        List users = usersQuery.getResultList();

        //run query to get total result
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM users");
        Long total = Long.parseLong(countQuery.getSingleResult().toString());

        response.put("status", true);
        response.put("users", users);
        Map<String, Object> meta = new HashMap<>();
        meta.put("total", total);
        meta.put("limit", realLimit);
        meta.put("offset", Integer.parseInt(offset.orElse("0")));
        response.put("meta", meta);
        return  response;
    }


}
