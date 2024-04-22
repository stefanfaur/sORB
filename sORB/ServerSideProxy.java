package sORB;

import sORB.ByteCommunication.MessageMarshaller.*;
import sORB.ByteCommunication.RequestReply.*;
import java.lang.reflect.*;

public class ServerSideProxy implements ByteStreamTransformer {
    private Object implementation;

    public ServerSideProxy(Object impl) {
        this.implementation = impl;
    }

    @Override
    public byte[] transform(byte[] in) {
        try {
            Marshaller marshaller = new Marshaller();
            Message msg = marshaller.unmarshal(in);
            String methodName = msg.data.split("/")[0];
            String[] args = msg.data.split("/").length > 1 ? msg.data.substring(msg.data.indexOf("/") + 1).split("/") : new String[0];

            Method[] methods = implementation.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object[] convertedArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        convertedArgs[i] = convertStringToType(args[i], paramTypes[i]);
                    }
                    method.setAccessible(true);  // is this safe??? won't work without, although already public
                    System.out.println("invoking method: " + method.getName() + " with args: " + msg.data);
                    Object response = method.invoke(implementation, convertedArgs);
                    System.out.println("type of response: " + method.getReturnType() + " response: " + response);
                    System.out.println("Invoked method: " + methodName + " with args: " + msg.data + " and responding with: " + response.toString());
                    return marshaller.marshal(new Message("Server", response.toString()));
                }
            }
            return marshaller.marshal(new Message("Server", "Method not found"));
        } catch (Exception e) {
            return new Marshaller().marshal(new Message("Server", "Error invoking method: " + e.getMessage()));
        }
    }

    private Object convertStringToType(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}