package com.heng.service.impl;/**
 * @author shkstart
 * @create 2023-05-10 16:38
 */

import com.heng.doc.GiftDoc;
import com.heng.highlight.HighlightResultMapper;
import com.heng.query.GiftDocQuery;
import com.heng.repository.GiftDocRepository;
import com.heng.service.IGiftDocService;
import com.heng.util.AjaxResult;
import com.heng.util.IdAndName;
import com.heng.util.PageList;
import com.heng.util.SearchUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日16:38
 *@Description:
 */
@Service
public class GiftDocServiceImpl implements IGiftDocService {

    @Autowired
    private GiftDocRepository giftDocRepository;

    @Autowired
    private ElasticsearchTemplate elasticSearchTemplate;

    @Autowired
    private HighlightResultMapper highlightResultMapper;

    @Override
    public AjaxResult search(GiftDocQuery query) {
        //创建本地的NativeSearchQueryBuilder对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //添加筛选条件
        //boolean 层
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //1、根据keyword关键字进行分词查询
        //参数非空校验
        if (StringUtils.isNotEmpty(query.getKeyword())){
            //must 层
            List<QueryBuilder> must = boolQueryBuilder.must();
            must.add(QueryBuilders.multiMatchQuery(query.getKeyword(),"name","tenantName","brandName","typeName"));
        }
        //将筛选条件添加到nativeSearchQueryBuilder
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

        //2、排序条件
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("onsaleTime").order(SortOrder.DESC));

        //3、分页条件
        nativeSearchQueryBuilder.withPageable(PageRequest.of(query.getPage()-1,query.getRows()));

        //4、高亮处理
        HighlightBuilder.Field name = new HighlightBuilder.Field("name").preTags("<font style='color:red'><b>").postTags("</b></font>");
        HighlightBuilder.Field tenantName = new HighlightBuilder.Field("tenantName").preTags("<font style='color:blue'><b>").postTags("</b></font>");
        HighlightBuilder.Field brandName = new HighlightBuilder.Field("brandName").preTags("<font style='color:blue'><b>").postTags("</b></font>");
        HighlightBuilder.Field typeName = new HighlightBuilder.Field("typeName").preTags("<font style='color:blue'><b>").postTags("</b></font>");
        nativeSearchQueryBuilder.withHighlightFields(name, tenantName, brandName, typeName);

        //5、设置聚合函数
        //品牌聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brandIdAgg").field("brandId").order(BucketOrder.count(false)).size(500)
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName.keyword").order(BucketOrder.count(false)).size(500)));
        //店铺聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("tenantIdAgg").field("tenantId").order(BucketOrder.count(false)).size(500)
                .subAggregation(AggregationBuilders.terms("tenantNameAgg").field("tenantName.keyword").order(BucketOrder.count(false)).size(500)));

        //查询es中的数据
        AggregatedPage<GiftDoc> queryForPage = elasticSearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), GiftDoc.class, highlightResultMapper);
        //封装es中的数据
        //当前页数据
        List<GiftDoc> content = queryForPage.getContent();
        //总条数
        long total = queryForPage.getTotalElements();
        PageList<GiftDoc> pageList = new PageList<GiftDoc>(total, content);
        //获取聚合的数据
        Aggregations aggregations = queryForPage.getAggregations();
        //封装聚合数据
        Map<String, List<IdAndName>> termsAggsData = SearchUtil.handleTermsAggsData(aggregations);
        pageList.setAggsData(termsAggsData);
        return AjaxResult.me().setResultObj(pageList);

    }
}
