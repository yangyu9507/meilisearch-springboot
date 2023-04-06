package com.yaron.meilisearch.controller;

import com.alibaba.fastjson2.JSONObject;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.DocumentsQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import com.yaron.meilisearch.index.Movies;
import com.yaron.meilisearch.repository.MoviesMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/03/31
 * @description
 */
@RestController
@RequestMapping("/doc")
public class DocumentController {
    @Resource
    private Client client;

    @Resource
    private MoviesMapper moviesMapper;

    @GetMapping("/getDocuments")
    public Results<Movies> getDocuments() throws MeilisearchException {
        return moviesMapper.list(2);
    }

    @GetMapping("/getOne")
    public Results<Movies> getOne(@RequestParam("document_id")String document_id) throws MeilisearchException {

        DocumentsQuery documentsQuery = new DocumentsQuery();
        documentsQuery.setFields(new String[]{"id","title","poster","release_date"});

        return moviesMapper.list(documentsQuery);
    }

    @PostMapping("/addOrReplace")
    public TaskInfo addOrReplaceDoc(@RequestBody Movies movies) throws MeilisearchException {
        return moviesMapper.add(movies);
    }

    @PutMapping("/updateDocuments")
    public TaskInfo updateDocuments(@RequestBody Movies movies) throws MeilisearchException {

        TaskInfo taskInfo = getIndex().updateDocuments(JSONObject.toJSONString(movies));
        return taskInfo;
    }

    @DeleteMapping("/deleteOneDoc")
    public TaskInfo deleteOneDoc(@RequestParam("documentId")String documentId) throws MeilisearchException{
        return getIndex().deleteDocument(documentId);
    }

    @DeleteMapping("/deleteAllDoc")
    public TaskInfo deleteAllDoc() throws MeilisearchException{
        return getIndex().deleteAllDocuments();
    }

    @PostMapping("/deleteBatch")
    public TaskInfo deleteBatch(@RequestParam("documentId") List<String> documentIds) throws MeilisearchException{
        return getIndex().deleteDocuments(documentIds);
    }

    private Index getIndex() throws MeilisearchException {
        return client.index("movies");
    }


}
