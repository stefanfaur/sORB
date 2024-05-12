package src.sORB.Activator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

public interface IActivatorProxy extends InvocationHandler {
    static <T> T create(Class<T> interfaceClass, Supplier<?> instanceSupplier) {
        IActivatorProxy handler = new ActivatorProxy(instanceSupplier);
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass, IActivatorProxy.class},
                handler
        );
    }

    void activate();

    void deactivate();

    @Override
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    Class<?> getServiceClass();

    Object getServiceInstance();

    boolean isActive();
}
