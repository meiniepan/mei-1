package com.wuyou.user.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2018/2/7.
 */

public class ServeDetailBean implements Parcelable {

    /**
     * service_id : 1
     * title : 空调清洗
     * photo : http://images4.5maiche.cn/2016-07-11_57833334133a7.jpg
     * price : 980
     * visiting_fee : 0
     * unit : 次
     * sold : 5
     * stock : 0
     * specification : []
     * start_at : 08:00
     * end_at : 06:30
     * content : <p>家政详情</p><p><img src="http://images4.5maiche.cn/1465818606000114.jpg" style=""/></p><p><img src="http://images4.5maiche.cn/146581860600009.jpg" style=""/></p><p><br/></p>
     * star : 4
     * high_praise : 26%
     * mode : {"1":"上门服务"}
     * shop_id : 2
     * shop_name : 重庆鸡公煲
     * recorded : 1
     */
    public List<String> images;
    public String service_id;
    public String service_name;
    public String title;
    public String photo;
    public float price;
    public int visiting_fee;
    public int stock;
    public int has_specification;
    public String start_at;
    public String end_at;
    public String content;
    public int star;
    public String high_praise;
    public List<ServeMode> mode;
    public String shop_id;
    public String shop_name;
    public int recorded;
    public String sold;
    public String unit;
    public String advert_word;
    public float market_price;
    public List<ServeSpecification> specification;
    public ServeSpecification spec;

    public int number;

    public ServeDetailBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
        dest.writeString(this.service_id);
        dest.writeString(this.service_name);
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeFloat(this.price);
        dest.writeInt(this.visiting_fee);
        dest.writeInt(this.stock);
        dest.writeInt(this.has_specification);
        dest.writeString(this.start_at);
        dest.writeString(this.end_at);
        dest.writeString(this.content);
        dest.writeInt(this.star);
        dest.writeString(this.high_praise);
        dest.writeTypedList(this.mode);
        dest.writeString(this.shop_id);
        dest.writeString(this.shop_name);
        dest.writeInt(this.recorded);
        dest.writeString(this.sold);
        dest.writeString(this.unit);
        dest.writeString(this.advert_word);
        dest.writeFloat(this.market_price);
        dest.writeTypedList(this.specification);
        dest.writeParcelable(this.spec, flags);
        dest.writeInt(this.number);
    }

    protected ServeDetailBean(Parcel in) {
        this.images = in.createStringArrayList();
        this.service_id = in.readString();
        this.service_name = in.readString();
        this.title = in.readString();
        this.photo = in.readString();
        this.price = in.readFloat();
        this.visiting_fee = in.readInt();
        this.stock = in.readInt();
        this.has_specification = in.readInt();
        this.start_at = in.readString();
        this.end_at = in.readString();
        this.content = in.readString();
        this.star = in.readInt();
        this.high_praise = in.readString();
        this.mode = in.createTypedArrayList(ServeMode.CREATOR);
        this.shop_id = in.readString();
        this.shop_name = in.readString();
        this.recorded = in.readInt();
        this.sold = in.readString();
        this.unit = in.readString();
        this.advert_word = in.readString();
        this.market_price = in.readFloat();
        this.specification = in.createTypedArrayList(ServeSpecification.CREATOR);
        this.spec = in.readParcelable(ServeSpecification.class.getClassLoader());
        this.number = in.readInt();
    }

    public static final Creator<ServeDetailBean> CREATOR = new Creator<ServeDetailBean>() {
        @Override
        public ServeDetailBean createFromParcel(Parcel source) {
            return new ServeDetailBean(source);
        }

        @Override
        public ServeDetailBean[] newArray(int size) {
            return new ServeDetailBean[size];
        }
    };
}
