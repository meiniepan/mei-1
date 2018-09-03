package com.wuyou.user.data.remote.response;

import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class CategoryParent {
    public String id;
    public String name;
    public String icon;
    public List<CategoryChild> sub;
}
