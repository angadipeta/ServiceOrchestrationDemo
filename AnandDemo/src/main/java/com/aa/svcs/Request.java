package com.aa.svcs;

/**
 * Created by Anand Swapna on 11/9/2016.
 */
public class Request {
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    private String input;

    public Request(String input)
    {
        setInput(input);
    }

}
