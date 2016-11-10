package com.aa.svcs;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anand Swapna on 11/9/2016.
 */

public class Service extends Thread
{
    private Request request;
    private Response response;
    private boolean processed = false;

    // List of Dependent services that should be processed before this servcie can start processing.
    private List<Service> dependencies = new ArrayList<Service>();

    // List of dependencies still in progress
    private ConcurrentHashMap<Service, Boolean> dependenciesTracker = new ConcurrentHashMap<Service, Boolean>();

    // List of Parents that are dependent on this Service, intent is to notify the parent once the processing is done.
    private List<Service> parents = new ArrayList<Service>();

    // List of dependencies whose output should be captured as input for this service
    private List<Service> inputs = new ArrayList<Service>();

    public Service(String name, Request request, Response response)
    {
        setName(name);
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }


    public boolean isProcessed() {
        return processed;
    }

    /**
     * Adds the dependency, sets the parent and flags if the output of dependency should be used as input
     * @param preProcessor
     * @param useOutput
     */
    public void addDependency(Service preProcessor, boolean useOutput)
    {
        dependencies.add(preProcessor);
        preProcessor.addParent(this);
        if(useOutput)
        {
            addDependencyToinput(preProcessor);
        }
    }

    private void addDependencyToinput(Service preProcessor)
    {
        inputs.add(preProcessor);
    }

    private void markDependencyAsFulfilled(Service fulfilledProcess)
    {
        dependenciesTracker.remove(fulfilledProcess);
    }

    private void notifyParents()
    {
        for (Service parent : parents)
        {
            parent.markDependencyAsFulfilled(this);
        }
    }

    private void addParent(Service parent)
    {
        parents.add(parent);
    }

    public void run()
    {
        for (Service preprocessor : dependencies)
        {
            dependenciesTracker.put(preprocessor, false);
            preprocessor.run();
        }

        while(!isProcessed())
        {
            if(dependenciesTracker.isEmpty())
            {
                System.out.println("Starting service |" + request.getInput() + "| " + Clock.systemUTC().millis());

                // Capture output of dependencies
                StringJoiner joiner = new StringJoiner("");
                for (Service input : inputs)
                {
                    joiner.add(input.getResponse().getOutput());
                }

                // Using the output of dependencies as Input
                if(!joiner.toString().isEmpty())
                {
                    getRequest().setInput(joiner.toString());
                }

                long secs = (Math.round(Math.random()*12345678)%4);
                sleepInterval(secs);

                //getResponse().setOutput(String.format("<%s time=\"%d seconds\">%s</%s>", getName(), secs/1000, getRequest().getInput(), getName()));

                getResponse().setOutput(String.format("<%s>%s</%s>", getName(), getRequest().getInput(), getName()));

                // Notify parents
                notifyParents();

                // Mark this service as done
                processed = true;

                System.out.println(getResponse().getOutput());
            }
        }
    }

    public void sleepInterval(long seconds) {

        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
