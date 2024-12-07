package com.example.demo.domain.dto;

public class RestResponse<T> {
    private int status;
    private String error;
    private Object mess;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMess() {
        return mess;
    }

    public void setMess(Object mess) {
        this.mess = mess;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
