package com.biswas.arun.redis.redisapi.dao;

import com.biswas.arun.redis.redisapi.common.utils.Utils;
import com.biswas.arun.redis.redisapi.payload.CustomerSearchCriteria;
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

    public void search(CustomerSearchCriteria customerSearchCriteria) {
//        private Long id;
//        private String fullName;
//        private String email;
//        private String mobile;
//        private String startCreateAt;
//        private String endCreateAt;

        Long totalResults = 0l;

        StringBuilder queryBuilder = new StringBuilder();

        if(Utils.isOk(customerSearchCriteria) && Utils.isOk(customerSearchCriteria.getId())) {
            queryBuilder.append("@id:" + customerSearchCriteria.getId());
        }

        if(Utils.isOk(customerSearchCriteria) && Utils.isOk(customerSearchCriteria.getFullName())) {
            queryBuilder.append("@fullName:" + customerSearchCriteria.getFullName());
        }

        if(Utils.isOk(customerSearchCriteria) && Utils.isOk(customerSearchCriteria.getEmail())) {
            queryBuilder.append("@email:" + customerSearchCriteria.getEmail());
        }

        if(Utils.isOk(customerSearchCriteria) && Utils.isOk(customerSearchCriteria.getStartCreateAt())) {
            if(!Utils.isOk(customerSearchCriteria.getEndCreateAt())){
                customerSearchCriteria.setEndCreateAt(customerSearchCriteria.getStartCreateAt());
            }



            queryBuilder.append("@email:" + customerSearchCriteria.getEmail());
        }



        String queryCriteria = queryBuilder.toString();
        Query query = null;

        if(queryCriteria.isEmpty()) {
            query = new Query();
        } else {
            query = new Query(queryCriteria);
        }

//        query.limit(PAGE_SIZE * (page-1), PAGE_SIZE);
        query.limit(PAGE_SIZE * (1-1), PAGE_SIZE);
        SearchResult searchResult
                = jedis.ftSearch("customer",query);
        totalResults = searchResult.getTotalResults();
        int numberOfPages =
                (int) Math.ceil((double)totalResults/PAGE_SIZE);

        List<Document> postList = searchResult.getDocuments();

        System.out.println(new Gson().toJson(postList));
    }
}
