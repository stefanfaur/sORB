package src.sORB;

import src.sORB.ByteCommunication.Commons.Address;
import src.sORB.ByteCommunication.MessageMarshaller.*;
import src.sORB.ByteCommunication.RequestReply.*;

import java.lang.reflect.*;

public class ClientProxy implements InvocationHandler {
    private Address remoteAddress;
    private Requestor requestor;
    private Marshaller marshaller;

    public ClientProxy(String serviceName) {
        this.remoteAddress = NamingServiceProxy.lookup(serviceName);
        this.requestor = new Requestor(serviceName);
        this.marshaller = new Marshaller();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String requestData = method.getName();
        for (Object arg : args) requestData += "/" + arg.toString(); // using "/" as separator

        Message request = new Message("Client", requestData);
        byte[] requestBytes = marshaller.marshal(request);
        System.out.println("Sending request: " + request.data);
        byte[] responseBytes = requestor.deliver_and_wait_feedback(remoteAddress, requestBytes);
//        System.out.println("Received response: " + new String(responseBytes));
        Message response = marshaller.unmarshal(responseBytes);

        return convertStringToReturnType(method.getReturnType(), response.data);
    }

    private Object convertStringToReturnType(Class<?> returnType, String data) {
        // check return type and convert data accordingly
        if (returnType.isAssignableFrom(String.class)) {
            return data;
        } else if (returnType == int.class || returnType == Integer.class) {
            return Integer.parseInt(data);
        } else if (returnType == double.class || returnType == Double.class) {
            return Double.parseDouble(data);
        } else if (returnType == boolean.class || returnType == Boolean.class) {
            return Boolean.parseBoolean(data);
        }
        throw new IllegalArgumentException("Unsupported return type: " + returnType);
    }

    public static <T> T getProxy(Class<T> interfaceClass, String serviceName) {
        System.out.println("Creating proxy for service: " + serviceName);
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ClientProxy(serviceName)
        );
    }
}