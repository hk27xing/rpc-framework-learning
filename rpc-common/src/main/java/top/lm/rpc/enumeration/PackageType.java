package top.lm.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hk27xing
 * @createTime 2021/7/6 16:19
 * */
@Getter
@AllArgsConstructor
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
