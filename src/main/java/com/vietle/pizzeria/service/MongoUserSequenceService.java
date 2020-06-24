package com.vietle.pizzeria.service;

import com.vietle.pizzeria.domain.MongoUserSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MongoUserSequenceService {
    @Autowired
    private MongoOperations mongo;

    public int getNextSequence(String seqName) {
        MongoUserSequence counter = mongo.findAndModify(
                query(where("id").is(seqName)),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                MongoUserSequence.class);
        return counter.getSeq();
    }
}
