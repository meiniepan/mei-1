package com.wuyou.user.data.api;

import java.util.List;

/**
 * Created by DELL on 2018/10/29.
 */

public class ListRowResponse<T> {
    public  boolean more;
    public List<T> rows;
}
