package com.heng.repository;

import com.heng.doc.GiftDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shkstart
 * @create 2023-05-10 15:24
 */
@Repository
public interface GiftDocRepository extends ElasticsearchRepository<GiftDoc,Long> {
}
