package com.aa.svcs;

/**
 * Created by Anand Swapna on 11/9/2016.
 */
public class Response {
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    private String output;

    public Response(String output)
    {
        setOutput(output);
    }
}
