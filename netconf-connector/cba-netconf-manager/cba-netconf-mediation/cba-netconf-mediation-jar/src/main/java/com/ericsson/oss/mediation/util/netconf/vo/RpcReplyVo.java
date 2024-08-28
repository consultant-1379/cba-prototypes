/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.vo;

/**
 * 
 * @author xvaltda
 */
public class RpcReplyVo extends NetconfVo {
    private boolean ok = false;
    private String messageId;
    private String data = "no-data";

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(final boolean ok) {
        this.ok = ok;
    }
}
