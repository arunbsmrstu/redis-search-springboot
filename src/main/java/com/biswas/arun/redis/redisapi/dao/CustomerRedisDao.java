package com.biswas.arun.redis.redisapi.dao;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.List;

@Repository
public class CustomerRedisDao {
    @Autowired
    private UnifiedJedis jedis;

    private static final Integer PAGE_SIZE = 2;

    public void search(String content, Integer page) {
        Long totalResults = 0l;

        StringBuilder queryBuilder = new StringBuilder();

        if(content !=null && !content.isEmpty()) {
            queryBuilder.append("@fullName:" + content);
        }


        String queryCriteria = queryBuilder.toString();
        Query query = null;

        if(queryCriteria.isEmpty()) {
            query = new Query();
        } else {
            query = new Query(queryCriteria);
        }

        query.limit(PAGE_SIZE * (page-1), PAGE_SIZE);
        System.out.println(query);
        SearchResult searchResult
                = jedis.ftSearch("customer",query);
        totalResults = searchResult.getTotalResults();
        int numberOfPages =
                (int) Math.ceil((double)totalResults/PAGE_SIZE);

        List<Document> postList = searchResult.getDocuments();

        System.out.println(new Gson().toJson(postList));
    }
}
