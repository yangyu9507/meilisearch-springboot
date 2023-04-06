package com.yaron.meilisearch.index;

import com.google.gson.annotations.SerializedName;
import com.yaron.meilisearch.annotation.MSFiled;
import com.yaron.meilisearch.annotation.MSIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/03/31
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MSIndex(uid = "movies", primaryKey = "id")
public class Movies implements Serializable {

    @MSFiled(openFilter = true,key="id", description = "主键")
    private Integer id;

    @MSFiled(openFilter = true,key="articleTitle", description = "标题")
    private String title;

    @MSFiled(openFilter = true,key="articleTitle", description = "概述")
    private String overview;

    @MSFiled(openFilter = true, key = "genres", description = "流派")
    private String[] genres;

    @MSFiled(openFilter = true,key="poster", description = "海报")
    private String poster;

    @MSFiled(openFilter = true,key="release_date", description = "上映时间")
    @SerializedName("release_date") // Gson Decode时 将DB中的 release_date 映射到 java的 releaseDate字段
    private Integer releaseDate;

}
