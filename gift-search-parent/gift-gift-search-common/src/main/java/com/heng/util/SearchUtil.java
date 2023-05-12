package com.heng.util;/**
 * @author shkstart
 * @create 2023-05-10 17:27
 */

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;

import java.util.*;

/**
 * @Auther:Jarvis
 * @Date:2023年05月2023/5/10日17:27
 * @Description:
 */
public class SearchUtil {
    /**
     * 处理terms聚合   id  name
     * @param aggregations
     * @return
     */
    public static Map<String, List<IdAndName>> handleTermsAggsData(Aggregations aggregations) {
        Map<String, Aggregation> aggregationsMap = aggregations.getAsMap();
        Set<Map.Entry<String, Aggregation>> entries = aggregationsMap.entrySet();
        Iterator<Map.Entry<String, Aggregation>> iterator = entries.iterator();
        //6.1有多少聚合就要返回多少个key-List<IdAndName<Long,String>
        Map<String, List<IdAndName>> aggsData = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Aggregation> entry = iterator.next();
            String key = entry.getKey();
            System.out.println(key);
            Aggregation aggsId = entry.getValue();
            if (aggsId instanceof LongTerms) {   //6.2 拿到id聚合,并且必须是LongTerms
                LongTerms aggsIdLong = (LongTerms) aggsId;
                List<LongTerms.Bucket> buckets = aggsIdLong.getBuckets();
                //6.3 List<IdAndName<Long,String>
                List<IdAndName> list = new ArrayList<>();
                buckets.forEach(bucket -> {
                    String idStr = bucket.getKeyAsString();
                    //6.4 通过子聚合获取name
                    Map<String, Aggregation> subAggs = bucket.getAggregations().getAsMap();
                    Set<Map.Entry<String, Aggregation>> entries1 = subAggs.entrySet();
                    //直接获取第一个
                    Map.Entry<String, Aggregation> nameAggEntry = entries1.iterator().next();
                    Aggregation nameAgg = nameAggEntry.getValue();
                    if (nameAgg instanceof StringTerms) {
                        StringTerms nameAggStringTerms = (StringTerms) nameAgg;
                        String nameStr = nameAggStringTerms.getBuckets().get(0).getKeyAsString();
                        IdAndName<Long, String> idName = new IdAndName<>();
                        idName.setId(Long.valueOf(idStr));
                        idName.setName(nameStr);
                        list.add(idName);
                    }
                });
                aggsData.put(key, list);
            }
        }
        return aggsData;
    }
}
