package com.gnway.bangwoba.bean;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * Created by luzhan on 2017/8/16.
 */

public class ExtensionXml implements ExtensionElement {
    private String elementName = "GNCustField1";
    private String gnCustField1Body;

    public String getGnCustField1Body() {
        return gnCustField1Body;
    }

    public void setGnCustField1Body(String gnCustField1Body) {
        this.gnCustField1Body = gnCustField1Body;
    }


    @Override
    public String getNamespace() {
        return "";
    }

    @Override
    public String getElementName() {
        return elementName;
    }

    @Override
    public CharSequence toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(elementName+" type=\"chat\"");
        //sb.append(elementName);
        sb.append(">");
        sb.append(gnCustField1Body);
        sb.append("</");
        sb.append(elementName);
        sb.append(">");
        return sb.toString();
    }
}
