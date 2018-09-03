package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.ServeSites;
import com.wuyou.user.util.GpsUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by DELL on 2018/6/13.
 */

public class SingleBottomChoosePanel extends Dialog {

    private ArrayList<String> data = new ArrayList<>();
    private ServeSites serveSite;

    public SingleBottomChoosePanel(@NonNull Context context, ArrayList<String> list) {
        super(context, R.style.bottom_dialog);
        initView();
        data.addAll(list);
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.single_bottom_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        ListView listView = rootView.findViewById(R.id.bottom_recycler);
        listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, data));
        listView.setOnItemClickListener((parent, view, position, id) -> startGuide(data.get(position)));
    }

    public void setServeSite(ServeSites serSite) {
        serveSite = serSite;
    }


    private void startGuide(String map) {
        dismiss();
        if (Constant.GAODE_MAP.equals(map)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("amapuri://route/plan?sourceApplication=" + getContext().getResources().getString(R.string.app_name) +
                    "&dname=" + serveSite.name + "&dlat=" + serveSite.lat + "&dlon=" + serveSite.lng + "&dev=0&t=1");
            intent.setData(uri);
            getContext().startActivity(intent);
        } else if (Constant.TENCENT_MAP.equals(map)) {
            String baseUrl = "qqmap://map/";
            String drivePlan = "routeplan?type=drive&from=&fromcoord=&to="+serveSite.name+"&tocoord=" + serveSite.lat + "," + serveSite.lng +"&policy=1";
            String tencnetUri = baseUrl + drivePlan + "&referer=" + getContext().getResources().getString(R.string.app_name);
            Intent intent;
            try {
                intent = Intent.parseUri(tencnetUri, 0);
                getContext().startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (Constant.BAIDU_MAP.equals(map)) {
            double[] baiduLoc = GpsUtils.gcj02_To_Bd09(serveSite.lat, serveSite.lng);
            try {
                Intent intent = Intent.parseUri("intent://map/direction?" +
                        "destination=latlng:" + baiduLoc[0] + "," + baiduLoc[1] + "|name:" + serveSite.name +
                        "&mode=driving" + "&region=" + "&src=" + getContext().getResources().getString(R.string.app_name) +
                        "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
                getContext().startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
