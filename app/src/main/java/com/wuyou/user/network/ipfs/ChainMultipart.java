package com.wuyou.user.network.ipfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Random;

/**
 * Created by DELL on 2018/10/23.
 */

public class ChainMultipart {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream out;

    public ChainMultipart(String requestURL, String charset) {
        this.charset = charset;
        this.boundary = createBoundary();

        try {
            URL url = new URL(requestURL);
            this.httpConn = (HttpURLConnection) url.openConnection();
            this.httpConn.setUseCaches(false);
            this.httpConn.setDoOutput(true);
            this.httpConn.setDoInput(true);
            this.httpConn.setRequestProperty("Expect", "100-continue");
            this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
            this.httpConn.setRequestProperty("User-Agent", "Java IPFS CLient");
            this.out = this.httpConn.getOutputStream();
        } catch (IOException var4) {
            throw new RuntimeException(var4.getMessage(), var4);
        }
    }

    public static String createBoundary() {
        Random r = new Random();
        String allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < 32; ++i) {
            b.append(allowed.charAt(r.nextInt(allowed.length())));
        }

        return b.toString();
    }

    private ChainMultipart append(String value) throws IOException {
        this.out.write(value.getBytes(this.charset));
        return this;
    }

    public void addFormField(String name, String value) throws IOException {
        this.append("--").append(this.boundary).append("\r\n");
        this.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append("\r\n");
        this.append("Content-Type: text/plain; charset=").append(this.charset).append("\r\n");
        this.append("\r\n");
        this.append(value).append("\r\n");
        this.out.flush();
    }

    public void addDirectoryPart(Path path) throws IOException {
        this.append("--").append(this.boundary).append("\r\n");
        this.append("Content-Disposition: file; filename=\"").append(encode(path.toString())).append("\"").append("\r\n");
        this.append("Content-Type: application/x-directory").append("\r\n");
        this.append("Content-Transfer-Encoding: binary").append("\r\n");
        this.append("\r\n");
        this.append("\r\n");
        this.out.flush();
    }

    private static String encode(String in) {
        try {
            return URLEncoder.encode(in, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public void addFilePart(String fieldName, ChainNamedStreamable uploadFile, String fileName) throws IOException {
        this.append("--").append(this.boundary).append("\r\n");
        this.append("Content-Disposition: file; filename=\"").append(fileName).append("\";").append("\r\n");

        this.append("Content-Type: application/octet-stream").append("\r\n");
        this.append("Content-Transfer-Encoding: binary").append("\r\n");
        this.append("\r\n");
        this.out.flush();

        try {
            InputStream inputStream = uploadFile.getInputStream();
            byte[] buffer = new byte[4096];

            while (true) {
                int r;
                if ((r = inputStream.read(buffer)) == -1) {
                    this.out.flush();
                    inputStream.close();
                    break;
                }

                this.out.write(buffer, 0, r);
            }
        } catch (IOException var8) {
            throw new RuntimeException(var8.getMessage(), var8);
        }

        this.append("\r\n");
        this.out.flush();
    }

    public void addHeaderField(String name, String value) throws IOException {
        this.append(name + ": " + value).append("\r\n");
        this.out.flush();
    }

    public String finish() throws IOException {
        StringBuilder b = new StringBuilder();
        this.append("--" + this.boundary + "--").append("\r\n");
        this.out.flush();
        this.out.close();

        try {
            int status = this.httpConn.getResponseCode();
            BufferedReader reader;
            String line;
            if (status == 200) {
                reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    b.append(line);
                }

                reader.close();
                this.httpConn.disconnect();
                return b.toString();
            } else {
                try {
                    reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));

                    while ((line = reader.readLine()) != null) {
                        b.append(line);
                    }

                    reader.close();
                } catch (Throwable var5) {
                    ;
                }

                throw new IOException("Server returned status: " + status + " with body: " + b.toString() + " and Trailer header: " + this.httpConn.getHeaderFields().get("Trailer"));
            }
        } catch (IOException var6) {
            throw new RuntimeException(var6.getMessage(), var6);
        }
    }
}
