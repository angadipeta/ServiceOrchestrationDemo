package com.aa.demo;

import com.aa.svcs.Request;
import com.aa.svcs.Response;
import com.aa.svcs.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Anand Swapna on 11/9/2016.
 */
public class ServiceController
{

    public Service createService(String serviceName)
    {
        Request request = new Request(serviceName);
        Response response = new Response(serviceName);
        return new Service(serviceName, request, response);
    }

    public void executeService(Service service)
    {
        service.start();
    }

    public void executeServices(List<Service> services)
    {
        for (Service service : services)
        {
            service.start();
        }
    }

}
