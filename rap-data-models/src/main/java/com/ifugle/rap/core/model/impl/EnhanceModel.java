/**
 * Copyright(C) 2013 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.core.model.impl;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @since Sep 28, 2013 2:32:24 PM
 * @version $Id: EnhanceModel.java 116126 2020-04-13 07:40:44Z HuYu $
 * @author WuJianqiang
 *
 */
public class EnhanceModel<ID extends Serializable> extends DefaultModel<ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected String creatorId;
    protected String creator;
    protected Date creationDate;
    protected String modifierId;
    protected String modifier;
    protected Date modificationDate;
    protected Integer revision;
    protected Integer syncFlag;
    protected Timestamp syncTime;
    protected Integer nodeId;

    /**
     *
     */
    public EnhanceModel() {
        super();
    }

    /**
     * @param id
     */
    public EnhanceModel(ID id) {
        super(id);
    }

    /**
     * @param creatorId
     *            the creatorId to set
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }


    /**
     * @param creator
     *            the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * @param modifierId
     *            the modifierId to set
     */
    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * @param modifier
     *            the modifier to set
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate
     *            the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision
     *            the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * @return the syncFlag
     */
    public Integer getSyncFlag() {
        return syncFlag;
    }

    /**
     * @param syncFlag
     *            the syncFlag to set
     */
    public void setSyncFlag(Integer syncFlag) {
        this.syncFlag = syncFlag;
    }

    /**
     * @return the syncTime
     */
    public Timestamp getSyncTime() {
        return syncTime;
    }

    /**
     * @param syncTime
     *            the syncTime to set
     */
    public void setSyncTime(Timestamp syncTime) {
        this.syncTime = syncTime;
    }

    /**
     * @return the nodeId
     */
    public Integer getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId
     *            the nodeId to set
     */
    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((creator == null) ? 0 : creator.hashCode());
        result = prime * result + ((creatorId == null) ? 0 : creatorId.hashCode());
        result = prime * result + ((modificationDate == null) ? 0 : modificationDate.hashCode());
        result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
        result = prime * result + ((modifierId == null) ? 0 : modifierId.hashCode());
        result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
        result = prime * result + ((revision == null) ? 0 : revision.hashCode());
        result = prime * result + ((syncFlag == null) ? 0 : syncFlag.hashCode());
        result = prime * result + ((syncTime == null) ? 0 : syncTime.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        EnhanceModel other = (EnhanceModel) obj;
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (creator == null) {
            if (other.creator != null) {
                return false;
            }
        } else if (!creator.equals(other.creator)) {
            return false;
        }
        if (creatorId == null) {
            if (other.creatorId != null) {
                return false;
            }
        } else if (!creatorId.equals(other.creatorId)) {
            return false;
        }
        if (modificationDate == null) {
            if (other.modificationDate != null) {
                return false;
            }
        } else if (!modificationDate.equals(other.modificationDate)) {
            return false;
        }
        if (modifier == null) {
            if (other.modifier != null) {
                return false;
            }
        } else if (!modifier.equals(other.modifier)) {
            return false;
        }
        if (modifierId == null) {
            if (other.modifierId != null) {
                return false;
            }
        } else if (!modifierId.equals(other.modifierId)) {
            return false;
        }
        if (nodeId == null) {
            if (other.nodeId != null) {
                return false;
            }
        } else if (!nodeId.equals(other.nodeId)) {
            return false;
        }
        if (revision == null) {
            if (other.revision != null) {
                return false;
            }
        } else if (!revision.equals(other.revision)) {
            return false;
        }
        if (syncFlag == null) {
            if (other.syncFlag != null) {
                return false;
            }
        } else if (!syncFlag.equals(other.syncFlag)) {
            return false;
        }
        if (syncTime == null) {
            if (other.syncTime != null) {
                return false;
            }
        } else if (!syncTime.equals(other.syncTime)) {
            return false;
        }
        return true;
    }

}
