package com.ifugle.rap.sqltransform.samekeyaction;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SameKeyAction;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * LINK_RATE=昨天（月）/前天（月）
 *CHAIN_RATE=昨月（天）/（去年或者上个月）前月（天）
 * @author Minc
 * @date 2022/1/18 17:37
 */
public class RateSameKeyAction extends SameKeyAction<IndexDayModel> {

    private final boolean isChainRate;

    public RateSameKeyAction(KeySelector<IndexDayModel> indexDayModelKeySelector, boolean isChainRate) {
        super(indexDayModelKeySelector);
        this.isChainRate = isChainRate;
    }

    @Override
    public void sameKeyAction(IndexDayModel remain, IndexDayModel leave) throws Exception {
        double incCountRate = leave.getIncCount() != 0 ? (remain.getIncCount() - leave.getIncCount()) * 1.0 / leave.getIncCount() : 0;
        double netIncCountRate = leave.getNetIncCount() != 0 ? (remain.getNetIncCount() - leave.getNetIncCount()) * 1.0 / leave.getNetIncCount() : 0;
        double decCountRate = leave.getDecCount() != 0 ? (remain.getDecCount() - leave.getDecCount()) * 1.0 / leave.getDecCount() : 0;
        double totalCountRate = leave.getTotalCount() != 0 ? (remain.getTotalCount() - leave.getTotalCount()) * 1.0 / leave.getTotalCount() : 0;
        double incCountRateAfterHandle = Double.parseDouble(String.format("%.4f", incCountRate));
        double netIncCountRateAfterHandle = Double.parseDouble(String.format("%.4f", netIncCountRate));
        double decCountRateAfterHandle = Double.parseDouble(String.format("%.4f", decCountRate));
        double totalCountRateAfterHandle = Double.parseDouble(String.format("%.4f", totalCountRate));
        if (isChainRate) {
            remain.setIncCountChainRat(incCountRateAfterHandle < 0 ? 0 : incCountRateAfterHandle);
            remain.setNetIncCountChainRat(netIncCountRateAfterHandle < 0 ? 0 : netIncCountRateAfterHandle);
            remain.setDecCountChainRat(decCountRateAfterHandle < 0 ? 0 : decCountRateAfterHandle);
            remain.setTotalCountChainRat(totalCountRateAfterHandle < 0 ? 0 : totalCountRateAfterHandle);
        } else {
            remain.setIncCountLinkRat(incCountRateAfterHandle < 0 ? 0 : incCountRateAfterHandle);
            remain.setNetIncCountLinkRat(netIncCountRateAfterHandle < 0 ? 0 : netIncCountRateAfterHandle);
            remain.setDecCountLinkRat(decCountRateAfterHandle < 0 ? 0 : decCountRateAfterHandle);
            remain.setTotalCountLinkRat(totalCountRateAfterHandle < 0 ? 0 : totalCountRateAfterHandle);
        }
    }
}
