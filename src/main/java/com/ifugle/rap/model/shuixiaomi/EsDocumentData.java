package com.ifugle.rap.model.shuixiaomi;

import java.io.Serializable;
import java.util.*;

public class EsDocumentData implements Serializable {

    private static final long serialVersionUID = 7107266068279125753L;

    private final List<Long> ids; // 需要同步更新的文档ID

    private final String indexName; // 索引名称

    private final String docName; // 文档名称

    private Map<String, Object> properties = new HashMap<>(); // 需要更新的属性数据

    public EsDocumentData(Long id, String docName,String indexName) {
        this.ids = Arrays.asList(id);
        this.docName =docName;
        this.indexName =indexName;
    }

    public EsDocumentData(List<Long> ids,  String docName,String indexName) {
        this.ids = Collections.unmodifiableList(ids);
        this.docName = docName;
        this.indexName = indexName;
    }

    public void addData(String propertyName, Object data) {
        properties.put(propertyName, data);
    }

    public void addData(Map<String, Object> partialDocument) {
        properties.putAll(partialDocument);
    }

    /**
     * @return the ids
     */
    public List<Long> getIds() {
        return ids;
    }

    /**
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    public String getIndexName() {
        return indexName;
    }
}