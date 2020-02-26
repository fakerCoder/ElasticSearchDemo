package com.example.demo.dao;

import com.example.demo.entity.BlogEntity;
import com.example.demo.entity.DemoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogDao extends ElasticsearchRepository<BlogEntity,String> {

    List<BlogEntity> findDemoEntitiesByContent(String content);


    List<BlogEntity> findDemoEntitiesByContentOrTitle(String param);

    List<BlogEntity> findByTitleStartingWith(String test);

}
