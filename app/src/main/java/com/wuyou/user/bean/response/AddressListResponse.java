package com.wuyou.user.bean.response;

import com.wuyou.user.bean.AddressBean;
import com.wuyou.user.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2018/2/6.
 */

public class AddressListResponse {
    public int has_more;
    public ArrayList<AddressBean> list;
}
