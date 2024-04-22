package Applications.MathClientServer;

import sORB.ClientProxy;

public class MathClient {
    public interface MathService {
        double doAdd(double a, double b);
        double doSqrt(double a);
    }

    public static void main(String[] args) {
        new Configuration();
        MathService service = ClientProxy.getProxy(MathService.class, "MathServer");
        System.out.println("Sum: " + service.doAdd(1, 2.2f));
        System.out.println("Sqrt: " + service.doSqrt(16));
    }
}