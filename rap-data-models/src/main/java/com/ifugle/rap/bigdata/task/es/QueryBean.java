package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * es must must_not 查询条件类
 *
 * @author GuanTao
 * @version $Id$
 * @since 2019年08月02日 22:56
 */
public class QueryBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoolBean bool;

    public QueryBean() {
    }

    public QueryBean(QueryBean.BoolBean bool) {
        this.bool = bool;
    }

    public BoolBean getBool() {
        return bool;
    }

    public void setBool(BoolBean bool) {
        this.bool = bool;
    }

    public static class HasParent {

        @SerializedName("parent_type")
        private String parentType;

        private QueryBean query;

        public String getParentType() {
            return parentType;
        }

        public void setParentType(String parentType) {
            this.parentType = parentType;
        }

        public QueryBean getQuery() {
            return query;
        }

        public void setQuery(QueryBean query) {
            this.query = query;
        }
    }

    public static class HasChild {

        private String type;

        private QueryBean query;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public QueryBean getQuery() {
            return query;
        }

        public void setQuery(QueryBean query) {
            this.query = query;
        }
    }

    public static class BoolBean {
        private List<MustBean> must;

        @SerializedName("must_not")
        private List<MustBean> mustNot;

        private List<MustBean> should;

        public BoolBean(List<MustBean> must) {
            this.must = must;
        }

        public BoolBean(List<MustBean> must, List<MustBean> mustNot) {
            this.must = must;
            this.mustNot = mustNot;
        }

        public BoolBean(List<MustBean> must, List<MustBean> mustNot, List<MustBean> should) {
            this.must = must;
            this.mustNot = mustNot;
            this.should = should;
        }

        public List<MustBean> getMust() {
            return must;
        }

        public void setMust(List<MustBean> must) {
            this.must = must;
        }

        public List<MustBean> getMustNot() {
            return mustNot;
        }

        public void setMustNot(List<MustBean> mustNot) {
            this.mustNot = mustNot;
        }

        public List<MustBean> getShould() {
            return should;
        }

        public void setShould(List<MustBean> should) {
            this.should = should;
        }

        public static class MustBean {
            private Map<String, Object> match;
            private Map<String, Collection> terms;
            private Map<String, Number> term;
            private Map<String, String> prefix;
            private Map<String, RangeBean> range;
            private Map<String, BoolBean> bool;
            private Map<String, String> exists;
            @SerializedName("has_parent")
            private HasParent hasParent;
            @SerializedName("has_child")
            private HasChild hasChild;

            public MustBean() {
            }

            public MustBean(Map<String, Object> match, Map<String, Collection> terms, Map<String, Number> term, Map<String, RangeBean> range) {
                this.match = match;
                this.terms = terms;
                this.term = term;
                this.range = range;
            }

            public MustBean(Map<String, Object> match, Map<String, Collection> terms, Map<String, Number> term, Map<String, RangeBean> range,
                    Map<String, BoolBean> bool, Map<String, String> exists) {
                this.match = match;
                this.terms = terms;
                this.term = term;
                this.range = range;
                this.bool = bool;
                this.exists = exists;
            }

            public Map<String, Object> getMatch() {
                return match;
            }

            public void setMatch(Map<String, Object> match) {
                this.match = match;
            }

            public Map<String, Collection> getTerms() {
                return terms;
            }

            public void setTerms(Map<String, Collection> terms) {
                this.terms = terms;
            }

            public Map<String, Number> getTerm() {
                return term;
            }

            public void setTerm(Map<String, Number> term) {
                this.term = term;
            }

            public Map<String, String> getPrefix() {
                return prefix;
            }

            public void setPrefix(Map<String, String> prefix) {
                this.prefix = prefix;
            }

            public Map<String, RangeBean> getRange() {
                return range;
            }

            public void setRange(Map<String, RangeBean> range) {
                this.range = range;
            }

            public Map<String, BoolBean> getBool() {
                return bool;
            }

            public void setBool(Map<String, BoolBean> bool) {
                this.bool = bool;
            }

            public Map<String, String> getExists() {
                return exists;
            }

            public void setExists(Map<String, String> exists) {
                this.exists = exists;
            }

            public HasParent getHasParent() {
                return hasParent;
            }

            public void setHasParent(HasParent hasParent) {
                this.hasParent = hasParent;
            }

            public HasChild getHasChild() {
                return hasChild;
            }

            public void setHasChild(HasChild hasChild) {
                this.hasChild = hasChild;
            }
        }
    }
}
