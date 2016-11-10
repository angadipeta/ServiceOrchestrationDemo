import com.aa.demo.Order;
import com.aa.demo.ServiceController;
import com.aa.svcs.Request;
import com.aa.svcs.Response;
import com.aa.svcs.Service;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Anand Swapna on 11/9/2016.
 */
public class HelloWorld {
    
    static ServiceController svcCtrl = new ServiceController();
    
    public static void main(String[] args)
    {
        System.out.println("Hello World");
        ServiceController controller = new ServiceController();
        orchestratedServices(new Order("FirstOrder"));
        //sequentialServices(new Order("SecondOrder"));
    }

    public static void orchestratedServices(Order order)
    {
        Service serviceA = svcCtrl.createService(order.getName().concat("-A"));
        Service serviceB = svcCtrl.createService(order.getName().concat("-B"));
        Service serviceAA = svcCtrl.createService(order.getName().concat("-AA"));
        Service serviceC = svcCtrl.createService(order.getName().concat("-C"));
        Service serviceD = svcCtrl.createService(order.getName().concat("-D"));

        serviceAA.addDependency(serviceA, true);

        serviceC.addDependency(serviceAA, true);
        serviceC.addDependency(serviceB, true);

        serviceB.addDependency(serviceD, true);

        svcCtrl.executeService(serviceC);
    }


    public static void sequentialServices(Order order)
    {
        Service serviceA = svcCtrl.createService(order.getName().concat("-A"));
        Service serviceB = svcCtrl.createService(order.getName().concat("-B"));
        Service serviceC = svcCtrl.createService(order.getName().concat("-C"));

        svcCtrl.executeServices(new ArrayList<Service>(Arrays.asList(serviceA, serviceC, serviceB)));
    }
}
