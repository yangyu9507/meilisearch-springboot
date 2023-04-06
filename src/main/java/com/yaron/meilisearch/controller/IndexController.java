package com.yaron.meilisearch.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.SwapIndexesParams;
import com.meilisearch.sdk.model.TaskInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/03/31
 * @description
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    private Client client;

    @GetMapping("/all")
    public Map<String,Object> all() throws MeilisearchException {
        return transformRes(client.getIndexes());
    }



    @GetMapping("/limit/{limit}")
    public Map<String,Object> indexLimit(@PathVariable("limit")Integer limit) throws MeilisearchException   {

        IndexesQuery query = new IndexesQuery().setLimit(limit);

        return transformRes(client.getIndexes(query));

    }

    @GetMapping("/getOneIndex/{uid}")
    public Map<String,Object> getOneIndex(@PathVariable("uid")String limit) throws MeilisearchException   {
        return transFormIndex(client.getIndex(limit));
    }

    @PostMapping("/createIndex")
    public TaskInfo createIndex(@RequestParam("uid")String uid,@RequestParam("primaryKey")String primaryKey) throws MeilisearchException {
        TaskInfo index = client.createIndex(uid, primaryKey);
        return index;
    }

    @PatchMapping("/updateIndex")
    public TaskInfo updateIndex(@RequestParam("uid")String uid,@RequestParam("primaryKey")String primaryKey) throws MeilisearchException {
        TaskInfo index = client.updateIndex(uid, primaryKey);
        return index;
    }


    @DeleteMapping("/deleteIndex")
    public TaskInfo updateIndex(@RequestParam("uid")String uid) throws MeilisearchException {
        TaskInfo index = client.deleteIndex(uid);
        return index;
    }

    @GetMapping("/swapIndex")
    public TaskInfo swapIndex() throws MeilisearchException {
        SwapIndexesParams[] params =
                new SwapIndexesParams[] {
                        new SwapIndexesParams().setIndexes(new String[] {"indexA", "indexB"}),
                        new SwapIndexesParams().setIndexes(new String[] {"indexX", "indexY"})
                };
        return  client.swapIndexes(params);
    }



    private Map<String,Object> transformRes(Results<Index> results){

        Map<String,Object> resMap = Maps.newLinkedHashMap();
        int limit = results.getLimit();
        int offset = results.getOffset();
        int total = results.getTotal();
        Index[] indices
                = results.getResults();
        resMap.put("limit",limit);
        resMap.put("offset",offset);
        resMap.put("total",total);
        List<Map<String,Object>> res = Lists.newArrayListWithCapacity(indices.length);

        for (Index index: indices){
            res.add(transFormIndex(index));
        }
        resMap.put("results",res);
        return resMap;
    }

    private Map<String,Object> transFormIndex(Index index){
        Map<String,Object> map = Maps.newLinkedHashMap();
        map.put("uid", index.getUid());
        map.put("primaryKey", index.getPrimaryKey());
        map.put("createdAt", index.getCreatedAt());
        map.put("updatedAt", index.getUpdatedAt());
        return map;
    }

}
