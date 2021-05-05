package com.ifugle.rap.bigdata.task.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.bigdata.task.service.BulkBusinessCommonService;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.elasticsearch.entity.BizException;
import com.ifugle.rap.elasticsearch.entity.HitEntity;
import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年07月17日 15:36
 */
@Service
public class BulkBusinessCommonServiceImpl implements BulkBusinessCommonService {
    private static final Logger logger = LoggerFactory.getLogger(BulkBusinessCommonServiceImpl.class);

    @Autowired
    private BulkTemplateRepository<Map<String, Object>> templateRepository;

    /**
     * 判断ES执行是否成功
     *
     * @param result
     *
     * @return
     */
    private boolean isSuccess(String result) {
        JsonObject resultJson = GsonUtil.fromJson(result, JsonObject.class);
        // 判断是否有error字段返回
        JsonElement error = resultJson.get("error");
        if (NullUtil.isNotNull(error)) {
            // 有error返回则为执行错误
            return false;
        }
        // 如果没有error，判断是否有errors，如果有且为true，则为执行失败
        JsonElement errors = resultJson.get("errors");
        if (NullUtil.isNotNull(errors)) {
            if (errors.getAsBoolean()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量新增或更新
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String postInsertOrUpdateBulk(String index, String paramBody) throws IOException {
        if (EsCode.LOG_OUT.get()) {
            logger.debug("index={}，request={}", index, paramBody);
        }
        HttpEntity httpEntity = getHttpEntity(index, paramBody);

        Response response = this.templateRepository.bulk(index, httpEntity);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行批量导入失败：{}", result);
        }
        return result;
    }

    /**
     * 按ID批量获取数据
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String mget(String index, String paramBody) throws IOException {
        HttpEntity httpEntity = getHttpEntity(index, paramBody);

        Response response = this.templateRepository.mget(index, httpEntity);
        HttpEntity httpEntityRes = response.getEntity();
        String queryResult = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(queryResult)) {
            throw new DsbServiceException("ES执行批量查询失败：{}", queryResult);
        }
        return queryResult;
    }

    /**
     * 按条件分页查询
     *
     * @param index
     * @param request
     * @param scrollTime
     *         分钟数 0：浅分页，大于0：快照分页
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String queryListByDSL(String index, String request, int scrollTime) throws IOException {
        logger.debug("index={}，request={}", index, request);
        Response response = this.templateRepository.queryListByDSL(index, request, scrollTime);
        HttpEntity httpEntityRes = response.getEntity();
        String queryResult = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(queryResult)) {
            throw new DsbServiceException("ES执行查询失败：{}", queryResult);
        }
        return queryResult;
    }

    @Override
    public String scrollQuery(String request) throws IOException {
        logger.debug("request={}", request);
        Response response = this.templateRepository.scrollQuery(request);
        HttpEntity httpEntityRes = response.getEntity();
        String queryResult = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(queryResult)) {
            throw new DsbServiceException("ES执行分页查询失败：{}", queryResult);
        }
        return queryResult;
    }

    @Override
    public void deleteScrollQuery(String scrollId) throws IOException {
        Response response = this.templateRepository.deleteScrollQuery(scrollId);
        HttpEntity httpEntityRes = response.getEntity();
        String queryResult = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(queryResult)) {
            throw new DsbServiceException("ES执行删除ScrollId失败：{}", queryResult);
        }
    }

    @Override
    public boolean getIndexExist(String index) {
        logger.debug("index={}", index);
        return this.templateRepository.getIndexExist(index);
    }

    /**
     * 根据查询条件更新数据
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String updateByQuery(String index, String paramBody, Map<String, String> paramMap) throws IOException {
        logger.debug("index={}，request={}", index, paramBody);
        HttpEntity httpEntity = new StringEntity(paramBody, ContentType.APPLICATION_JSON);
        if (NullUtil.isNull(paramMap)) {
            paramMap = new HashMap();
        }
        // 刷新策略，更新数据后等待数据可见
        paramMap.put("refresh", "true");
        Response response = this.templateRepository.updateByQuery(index, httpEntity, paramMap);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行根据条件修改数据失败：{}", result);
        }
        return result;
    }

    @Override
    public String updateByWaitForCompletion(String index, String paramBody) throws IOException {
        logger.debug("index={}，request={}", index, paramBody);
        HttpEntity httpEntity = new StringEntity(paramBody, ContentType.APPLICATION_JSON);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("wait_for_completion", "false");
        paramMap.put("scroll_size", "5000");
        Response response = this.templateRepository.updateByQuery(index, httpEntity, paramMap);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行根据条件异步修改数据失败：{}", result);
        }
        return result;
    }

    @Override
    public String tasks(String task) throws IOException {
        logger.debug("task={}", task);
        Response response = this.templateRepository.tasks(task);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行根据task查询后台执行结果失败：{}", result);
        }
        return result;
    }

    @Override
    public String settings(String index, String paramBody) throws IOException {
        logger.debug("index={}，settings={}", index, paramBody);
        HttpEntity httpEntity = new StringEntity(paramBody, ContentType.APPLICATION_JSON);
        Response response = this.templateRepository.settings(index, httpEntity);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("设置ES参数失败：{}", result);
        }
        return result;
    }

    @Override
    public String refresh(String index) throws IOException {
        logger.debug("index={}", index);
        Response response = this.templateRepository.refresh(index);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行刷新失败：{}", result);
        }
        return result;
    }

    private HttpEntity getHttpEntity(String index, String paramBody) {
        if (NullUtil.isNull(paramBody) || NullUtil.isNull(index)) {
            throw new BizException("[BulkBusinessCommonService] input param is null,index or source ,please check it");
        }
        return new StringEntity(paramBody, ContentType.APPLICATION_JSON);
    }

    /**
     * 聚合查询
     *
     * @param index
     * @param queryDSL
     *
     * @return
     */
    @Override
    public AggsResponseEntity aggregateQuery(String index, String queryDSL) {
        logger.debug("index={}，request={}", index, queryDSL);
        return this.templateRepository.aggsqueryByDSL(index, queryDSL);
    }

    /**
     * 根据查询条件删除数据
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String deleteByQuery(String index, String paramBody) throws IOException {
        logger.debug("index={}，request={}", index, paramBody);
        HttpEntity httpEntity = getHttpEntity(index, paramBody);
        Response response = this.templateRepository.deleteByQuery(index, httpEntity);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        if (!isSuccess(result)) {
            throw new DsbServiceException("ES执行根据条件删除数据失败：{}", result);
        }
        return result;
    }

    /**
     * 根据ID查询数据
     *
     * @param index
     * @param keyId
     *
     * @return
     */
    @Override
    public Map<String, Object> getById(String index, String keyId) {
        Map<String, Object> map = null;
        if (!StringUtils.isBlank(keyId) && !StringUtils.isBlank(index)) {
            HitEntity<Map<String, Object>> response = this.templateRepository.getById(index, keyId);
            if (response != null) {
                map = (Map) response.getSource();
            }
            return map;
        } else {
            throw new BizException("[BulkBusinessCommonService] input param is null,index or keyId ,please check it");
        }
    }

    /**
     * 批量新增或更新，（不处理返回的内容错误）
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public String postInsertOrUpdateBulkNotHandleInternalError(String index, String paramBody) throws IOException {
        if (EsCode.LOG_OUT.get()) {
            logger.debug("index={}，request={}", index, paramBody);
        }
        HttpEntity httpEntity = getHttpEntity(index, paramBody);

        Response response = this.templateRepository.bulk(index, httpEntity);
        HttpEntity httpEntityRes = response.getEntity();
        String result = EntityUtils.toString(httpEntityRes);
        return result;
    }
    /**
     * 创建索引和mapping
     * @param oldIndexName
     * @param newIndexName
     * @return
     */
    @Override
    public boolean createIndexAndMapping(String oldIndexName, String newIndexName) {
        return this.templateRepository.creatIndexAndMapping(oldIndexName,newIndexName);
    }
}
