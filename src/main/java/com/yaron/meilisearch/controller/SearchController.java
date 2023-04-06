package com.yaron.meilisearch.controller;

import com.jvm123.meilisearch.json.SearchResult;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;

import com.yaron.meilisearch.index.Movies;
import com.yaron.meilisearch.repository.MoviesMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/03/31
 * @description
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private MoviesMapper moviesMapper;

    /**
     * 首页的搜索-根据关键字查询文章
     *
     * @param q
     * @return
     * @throws MeilisearchException
     */
    @PostMapping("/q")
    public SearchResult<Movies> searchQ(@RequestParam("q")String q) throws MeilisearchException {

        return searchByCondition(
                SearchRequest.builder()
                .q(q)
                .filter(new String[]{"status=1 AND isDelete=0"})
                .limit(10)
                .build()
        );
    }

    @PostMapping("/topAndRecommend")
    public SearchResult<Movies> searchTopAndRecommend() throws MeilisearchException{

        return searchByCondition(
                SearchRequest.builder()
                .filter(new String[]{"(isFeatured =1 AND (status=1 AND isDelete=0) ) OR ( isTop=1 AND (status=1 AND isDelete=0)) "})
                .limit(10)
                .build()
        );
    }

    /**
     * 分页查询全部文章并创建时间倒序
     *
     * @return
     * @throws MeilisearchException
     */
    @PostMapping("/search2")
    public SearchResult<Movies> search2(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize) throws MeilisearchException{

        pageNum = pageNum < 1 ? 1 : pageNum;
        if (pageSize < 1) pageSize = 1;


        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = Math.min(pageSize, 100);


        return searchByCondition(
                SearchRequest.builder()
                        .offset(offset)
                        .limit(limit)
                        .filter(new String[]{"status =1 AND isDelete=0"})
                        .sort(new String[]{"createTime:desc"})
                        .build()
        );
    }

    /**
     * 根据类目分页查询文章
     *
     * @return
     */
    @PostMapping("/search3")
    public SearchResult<Movies> search3(@RequestParam("pageNum")Integer pageNum,
                                        @RequestParam("pageSize")Integer pageSize,
                                        @RequestParam("categoryId")Integer categoryId) throws MeilisearchException{

        pageNum = pageNum < 1 ? 1 : pageNum;
        if (pageSize < 1) pageSize = 1;


        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = Math.min(pageSize, 100);

        return searchByCondition(
                SearchRequest.builder()
                        .filter(new String[]{" status =1 AND isDelete=0 AND categoryId = " + categoryId})
                        .sort(new String[]{"createTime:desc"})
                        .offset(offset)
                        .limit(limit)
                        .build()
        );
    }

    /**
     * 根据类目分页查询文章
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     * @throws MeilisearchException
     */
    @PostMapping("/search4")
    public SearchResult<Movies> search4(@RequestParam("pageNum")Integer pageNum,
                                        @RequestParam("pageSize")Integer pageSize,
                                        @RequestParam("tagId")Integer tagId) throws MeilisearchException{

        pageNum = pageNum < 1 ? 1 : pageNum;
        if (pageSize < 1) pageSize = 1;


        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = Math.min(pageSize, 100);

        return searchByCondition(
                SearchRequest.builder()
                        .filter(new String[]{" tags.id=" + tagId + " AND status=1 AND isDelete=0 "})
                        .sort(new String[]{"createTime:desc"})
                        .offset(offset)
                        .limit(limit)
                        .build()
        );

    }





    @PostMapping("/searchByCondition")
    public SearchResult<Movies> searchByCondition(@RequestBody SearchRequest req)throws MeilisearchException {
        return moviesMapper.search(req);
    }

}
