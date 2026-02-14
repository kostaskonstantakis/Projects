/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonElement;
/**
 *
 * @author gussl
 */
public class PostContainerResponse {

    private ResponseEnum status;
    private String message;
    private JsonElement data;

    public PostContainerResponse(ResponseEnum status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseEnum getStatus() {
        return status;
    }

    public void setStatus(ResponseEnum status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public enum ResponseEnum {
        SUCCESS, ERROR
    }

}
