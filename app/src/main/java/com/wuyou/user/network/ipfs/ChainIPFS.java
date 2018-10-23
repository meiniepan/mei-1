package com.wuyou.user.network.ipfs;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import io.ipfs.api.IPFS;

/**
 * Created by DELL on 2018/10/23.
 */

public class ChainIPFS extends IPFS {
    public ChainIPFS(String host, int port) {
        super(host, port, "/api/v0/", false);
    }

    public String addFile(List<ChainNamedStreamable> files) throws IOException {
        ChainMultipart m = new ChainMultipart(this.protocol + "://" + this.host + ":" + this.port + "/api/v0/" + "add?stream-channels=true&w=" + false + "&n=" + false, "UTF-8");
        Iterator var5 = files.iterator();
        while (var5.hasNext()) {
            ChainNamedStreamable file = (ChainNamedStreamable) var5.next();
            m.addFilePart("file", file, file.getName());
        }

        String res = m.finish();
        JsonObject object = new GsonBuilder().create().fromJson(res, JsonObject.class);
        return object.get("Hash").toString().replace("\"","");
    }


    private byte[] retrieve(String path) throws IOException {
        URL target = new URL(this.protocol, this.host, this.port, "/api/v0/" + path);
        return get(target);
    }

    private static byte[] get(URL target) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) target.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        try {
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];

            int r;
            while ((r = in.read(buf)) >= 0) {
                resp.write(buf, 0, r);
            }

            return resp.toByteArray();
        } catch (ConnectException var6) {
            throw new RuntimeException("Couldn't connect to IPFS daemon at " + target + "\n Is IPFS running?");
        } catch (IOException var7) {
            String err = new String(readFully(conn.getErrorStream()));
            throw new RuntimeException("IOException contacting IPFS daemon.\nTrailer: " + conn.getHeaderFields().get("Trailer") + " " + err, var7);
        }
    }

    private static final byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream resp = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];

        int r;
        while ((r = in.read(buf)) >= 0) {
            resp.write(buf, 0, r);
        }

        return resp.toByteArray();
    }


    public void local() throws IOException {
        String jsonStream = new String(retrieve("refs/local"));
    }
}
