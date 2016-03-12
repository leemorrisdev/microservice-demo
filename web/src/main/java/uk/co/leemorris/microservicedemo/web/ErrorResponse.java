package uk.co.leemorris.microservicedemo.web;

/**
 * Created by Lee on 05/03/2016.
 */
public class ErrorResponse {

    private int code;
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
