package com.yaron.meilisearch.service;

import com.jvm123.meilisearch.json.SearchResult;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.*;

import java.util.List;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/04/03
 * @description
 */
public interface DocumentOperations <T>{

     T get(String identifier) throws MeilisearchException ;

     Results<T> list() throws MeilisearchException;

     Results<T> list(int limit) throws MeilisearchException;

     Results<T> list(int offset, int limit) throws MeilisearchException;

     Results<T> list(DocumentsQuery query) throws MeilisearchException;

     Results<T> list(DocumentsQuery query, Class<T> entityClass) throws MeilisearchException;

     TaskInfo add(T document) throws MeilisearchException;

     int update(T document) throws MeilisearchException;

     TaskInfo add(List<T> documents) throws MeilisearchException;

     int update(List<T> documents) throws MeilisearchException;

     int delete(String identifier) throws MeilisearchException;

     int deleteBatch(String... documentsIdentifiers) throws MeilisearchException;

     int deleteAll() throws MeilisearchException;

     SearchResult<T> search(String q) throws MeilisearchException;

     SearchResult<T> search(String q, int offset, int limit) throws MeilisearchException;

     SearchResult<T> search(SearchRequest sr) throws MeilisearchException;

     Settings getSettings() throws MeilisearchException;

     TaskInfo updateSettings(Settings settings) throws MeilisearchException;

     TaskInfo resetSettings() throws MeilisearchException;

     Task getUpdate(int updateId) throws MeilisearchException;


}
