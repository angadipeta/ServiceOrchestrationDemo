package com.aa.demo;

import com.aa.svcs.Response;
import com.aa.svcs.Service;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Created by Anand Swapna on 11/10/2016.
 */
public class ServiceTest
{
    ServiceController ctrl;
    Order order;
    Service serviceA;
    Service serviceB ;
    Service serviceAA ;
    Service serviceC ;
    Service serviceD ;
    Response response;


    @Before
    public void init()
    {
        ctrl = new ServiceController();
        order = new Order("Order");
        serviceA = ctrl.createService(order.getName().concat("-A"));
        serviceB = ctrl.createService(order.getName().concat("-B"));
        serviceAA = ctrl.createService(order.getName().concat("-AA"));
        serviceC = ctrl.createService(order.getName().concat("-C"));
        serviceD = ctrl.createService(order.getName().concat("-D"));
    }

    @Test
    public void testSequentialServices()
    {
        ctrl.executeServices(new ArrayList<Service>(Arrays.asList(serviceA, serviceC, serviceB)));
        response = serviceC.getResponse();
        sleep(5);
        assertTrue(response.getOutput().equals("<Order-C>Order-C</Order-C>"));
    }

    @Test
    public void testOrchestratedServices1()
    {
        serviceAA.addDependency(serviceA, true);
        serviceC.addDependency(serviceAA, true);
        serviceC.addDependency(serviceB, true);
        ctrl.executeService(serviceC);
        response = serviceC.getResponse();
        sleep(15);
        assertTrue(response.getOutput().equals("<Order-C><Order-AA><Order-A>Order-A</Order-A></Order-AA><Order-B>Order-B</Order-B></Order-C>"));
    }

    @Test
    public void testOrchestratedServices2()
    {
        serviceAA.addDependency(serviceA, true);
        serviceC.addDependency(serviceAA, true);
        serviceC.addDependency(serviceB, true);
        serviceB.addDependency(serviceD, true);
        ctrl.executeService(serviceC);
        response = serviceC.getResponse();
        sleep(15);
        assertTrue(response.getOutput().equals("<Order-C><Order-AA><Order-A>Order-A</Order-A></Order-AA><Order-B><Order-D>Order-D</Order-D></Order-B></Order-C>"));
    }

    private void sleep(int seconds) {

        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
