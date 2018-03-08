package com.wuyou.user.event;

import com.amap.api.services.core.PoiItem;

/**
 * Created by hjn on 2018/3/7.
 */

public class AddressEvent {
    public PoiItem poiItem;

    public AddressEvent(PoiItem title) {
        this.poiItem = title;
    }
}
