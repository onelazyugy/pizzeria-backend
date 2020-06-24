package com.vietle.pizzeria.service;

import com.vietle.pizzeria.domain.MongoCartSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MongCartSequenceService {
    @Autowired
    private MongoOperations mongo;

    public int getNextSequence(String seqName) {
        MongoCartSequence counter = mongo.findAndModify(
                query(where("id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                MongoCartSequence.class);
        return counter.getSeq();
    }
}
