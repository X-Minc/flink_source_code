/**
 * Copyright(C) 2019 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author HuZhihuai
 * @version $Id$
 * @since 2019年07月04日 16:58
 */
@Setter
@Getter
@ToString
public class YhzxXnzzBmChild extends YhzxXnzzBm {

    /**
     * 管理员
     */
    private String gly;
    /** 税务机关名称 **/
    private String swjgmc;
    /** 税务机关名称 **/
    private String swjgjc;
    /** 管护标志 **/
    private Byte ghbz;

}