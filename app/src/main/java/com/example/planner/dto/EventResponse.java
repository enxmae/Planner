package com.example.planner.dto;

import com.example.planner.dao.Event;
import com.google.gson.annotations.Expose;

public class EventResponse  {

    @Expose
    private int count;

    @Expose
    private Event[] data;

    @Expose
    private String message;

    @Expose
    private int offset;

    @Expose
    private int status;

    @Expose
    private boolean success;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
