package com.biswas.arun.redis.redisapi.controller;

import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.dao.CustomerRedisDao;
import com.biswas.arun.redis.redisapi.payload.CustomerRequest;
import com.biswas.arun.redis.redisapi.payload.CustomerResponse;
import com.biswas.arun.redis.redisapi.payload.CustomerUpdateRequest;
import com.biswas.arun.redis.redisapi.service.CustomerService;
import com.google.gson.Gson;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.SearchOptions;
import com.redislabs.lettusearch.SearchResults;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(path = "/api/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private StatefulRediSearchConnection<String, String> searchConnection;

    @Autowired
    CustomerRedisDao customerRedisDao;

    @Value("${app.customerSearchIndexName}")
    private String searchIndexName;

    @PostMapping("create")
    public CustomerResponse customerCreate(@Valid @RequestBody CustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest);
    }

    @PostMapping("update")
    public CustomerResponse customerUpdate(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        return customerService.updateCustomer(customerUpdateRequest);
    }

    @DeleteMapping("delete")
    public GenericResponse customerDelete(@Valid @RequestParam(name = "id") Long id) {
        return customerService.deleteCustomer(id);
    }

    @GetMapping("get")
    public CustomerResponse getCustomerDetails(@Valid @RequestParam(name = "id") Long id) {
        CustomerResponse customerResponse=customerService.getCustomerDetails(id);
        return customerResponse;
    }

    @GetMapping("/search")
    public SearchResults<String,String> search(@RequestParam(name="q")String query) {
        RediSearchCommands<String, String> commands = searchConnection.sync();
//        new SearchOptions(final boolean noContent, final boolean verbatim, final boolean noStopWords, final boolean withScores, final boolean withPayloads, final boolean withSortKeys, final List<K> inKeys, final List<K> inFields, final List<K> returnFields, final SearchOptions.Highlight<K> highlight, final SearchOptions.Language language, final SearchOptions.SortBy<K> sortBy, final SearchOptions.Limit limit)


        SearchResults<String, String> results = commands.search(searchIndexName, query);

        customerRedisDao.search(query,1);

//        System.out.println(new Gson().toJson(results));
//        results.forEach(test -> {
//            System.out.println(new Date(new Long(test.get("registeredOn"))));;
////            System.out.println(new Date(stringStringDocument.));
//        });
        return results;
    }

}

//    CreateOptions<String, String> options = CreateOptions.<String, String>builder()//
//            .prefix(String.format("%s:", Book.class.getName())).build();
