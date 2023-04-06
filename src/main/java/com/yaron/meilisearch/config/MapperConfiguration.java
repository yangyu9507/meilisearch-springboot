package com.yaron.meilisearch.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Settings;
import com.yaron.meilisearch.annotation.MSFiled;
import com.yaron.meilisearch.annotation.MSIndex;
import com.yaron.meilisearch.repository.MeilisearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/04/03
 * @description
 */
@Slf4j
@Component
public class MapperConfiguration implements InitializingBean {

    @Resource
    private Client client;

    private Map<Class, MeilisearchRepository> meiliSearchMapperMap = Maps.newConcurrentMap();

    public MapperConfiguration(ObjectProvider<MeilisearchRepository> objectProvider){

        List<MeilisearchRepository> collect = objectProvider.stream().collect(Collectors.toList());

        for(MeilisearchRepository repository: collect){
            log.info(repository.getClass().getName());
            Type actualTypeArgument = ((ParameterizedType) repository.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            meiliSearchMapperMap.put((Class<?>)actualTypeArgument, repository);
        }
        meiliSearchMapperMap.forEach((k,v) -> log.info("k:{} # v:{}", k ,v));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initIndex();
    }

    /**
     * 初始化索引信息
     *
     * @throws Exception
     */
    public void initIndex() throws Exception {
        if (MapUtils.isEmpty(meiliSearchMapperMap)) return;

        for (Map.Entry<Class, MeilisearchRepository> entry : meiliSearchMapperMap.entrySet()) {

            Class<?> tClass = entry.getKey();
            MeilisearchRepository meilisearchRepository = entry.getValue();

            if (Objects.isNull(tClass)) continue;

            MSIndex annoIndex = tClass.getAnnotation(MSIndex.class);

            String uid = StringUtils.defaultString(annoIndex.uid(), tClass.getSimpleName().toLowerCase());
            String primaryKey = StringUtils.defaultString(annoIndex.primaryKey(), "id");

            int maxTotalHit = Optional.ofNullable(annoIndex.maxTotalHits()).orElse(1000);
            int maxValuesPerFacet = Optional.ofNullable(annoIndex.maxValuesPerFacet()).orElse(100);

            List<String> filterKey = Lists.newArrayList();
            List<String> sortKey = Lists.newArrayList();
            List<String> noDisPlay = Lists.newArrayList();

            //获取类所有属性
            for (Field field : tClass.getDeclaredFields()) {
                //判断是否存在这个注解
                if (field.isAnnotationPresent(MSFiled.class)) {
                    MSFiled annotation = field.getAnnotation(MSFiled.class);
                    if (annotation.openFilter()) {
                        filterKey.add(annotation.key());
                    }

                    if (annotation.openSort()) {
                        sortKey.add(annotation.key());
                    }
                    if (annotation.noDisplayed()) {
                        noDisPlay.add(annotation.key());
                    }
                }
            }

            // 索引存在则更新,不存在就创建
            Results<Index> indexes = client.getIndexes();
            if (Stream.of(Optional.ofNullable(indexes.getResults())
                            .orElseThrow(() -> new RuntimeException("Failed to get Indexes.")))
                    .anyMatch(result -> uid.equals(result.getUid()))
            ) {
                client.updateIndex(uid, primaryKey);
            } else {
                client.createIndex(uid, primaryKey);
            }


            Index index = client.getIndex(uid);
            meilisearchRepository.setIndex(index);
            meilisearchRepository.setTClass(tClass);

            Settings settings = new Settings();
            settings.setDisplayedAttributes(noDisPlay.size() > 0 ? noDisPlay.toArray(new String[noDisPlay.size()]) : new String[]{"*"});
            settings.setFilterableAttributes(filterKey.toArray(new String[filterKey.size()]));
            settings.setSortableAttributes(sortKey.toArray(new String[sortKey.size()]));
            index.updateSettings(settings);

        }

    }
}
