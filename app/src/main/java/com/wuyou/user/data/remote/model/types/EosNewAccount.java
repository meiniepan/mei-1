/*
 * Copyright (c) 2017-2018 PLACTAL.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wuyou.user.data.remote.model.types;

import com.google.gson.annotations.Expose;
import com.wuyou.user.crypto.util.HexUtils;

/**
 * Created by swapnibble on 2017-09-12.
 */

public class EosNewAccount implements EosType.Packer {
    @Expose
    private String phone_number;
    @Expose
    private String account;
    @Expose
    private String key;
    @Expose
    private TypeAsset ram;

    public EosNewAccount(String phone_number, String account, String key, TypeAsset ram) {
        this.phone_number = phone_number;
        this.account = account;
        this.key = key;
        this.ram = ram;
    }

    public String getActionName() {
        return "createbyphone";
    }

    public String getCreatorName(){
        return account.toString();
    }

    @Override
    public void pack(EosType.Writer writer) {  //EosByteWriter
        writer.putString(phone_number);
        writer.putString(account);
        writer.putString(key);
        writer.putLongLE(ram.getAmount());
    }

    public String getAsHex() {
        EosType.Writer writer = new EosByteWriter(256);

        pack(writer);
        return HexUtils.toHex( writer.toBytes() );
    }
}
