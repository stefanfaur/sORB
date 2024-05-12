package src.sORB.Activator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;

public class ActivatorProxy implements IActivatorProxy {
    private Object serviceInstance; // Not generic anymore
    private Supplier<?> instanceSupplier;
    boolean isActive = false;

    public ActivatorProxy(Supplier<?> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void activate() {
        if (serviceInstance == null) {
            serviceInstance = instanceSupplier.get();
            System.out.println("Service activated.");
        }
        isActive = true;
    }

    @Override
    public void deactivate() {
        isActive = false;
        System.out.println("Service deactivated.");
        // Optional: You can nullify serviceInstance to force re-creation upon next activation
        // serviceInstance = null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("activate")) {
            activate();
            return null;
        } else if (method.getName().equals("deactivate")) {
            deactivate();
            return null;
        } else if
        (method.getName().equals("isActive")) {
            return isActive();
        } else if (method.getName().equals("getServiceClass")) {
            return getServiceClass();
        } else if (method.getName().equals("getServiceInstance")) {
            return getServiceInstance();
        } else if (!isActive()) {
            throw new IllegalStateException("Service is currently deactivated.");
        }

        System.out.println("Invoking method: " + method.getName() + " with args: " + Arrays.toString(args) + " on service: " + serviceInstance.getClass().getName());
        method.setAccessible(true); // again, necessary even for public methods
        return method.invoke(serviceInstance, args);
    }

    @Override
    public Class<?> getServiceClass() {
        return serviceInstance.getClass();
    }

    @Override
    public Object getServiceInstance() {
        return serviceInstance;
    }
}
