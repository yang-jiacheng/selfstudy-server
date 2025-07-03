package com.lxy.system.dto;

import lombok.Data;

import java.io.Serial;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Data
public class FeedBackReplyDTO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1728135602314542737L;

    private  Integer id;
    private  String reply;

}
