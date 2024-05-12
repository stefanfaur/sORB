package src.sORB;

import src.sORB.Activator.IActivatorProxy;
import src.sORB.ByteCommunication.MessageMarshaller.*;
import src.sORB.ByteCommunication.RequestReply.*;
import java.lang.reflect.*;

public class ServerSideProxy implements ByteStreamTransformer {
    private IActivatorProxy activator;

    public ServerSideProxy(IActivatorProxy activator) {
        this.activator = activator;
    }

    @Override
    public byte[] transform(byte[] in) {
        Marshaller marshaller = new Marshaller();
        Message msg = marshaller.unmarshal(in);

        System.out.println("Received: " + msg.data);

        // Handle activation/deactivation commands
        if ("deactivate".equals(msg.data)) {
            activator.deactivate();
            return marshaller.marshal(new Message("Server", "Deactivated"));
        } else if ("activate".equals(msg.data)) {
            System.out.println("Activating service");
            activator.activate();
            return marshaller.marshal(new Message("Server", "Activated"));
        }

        // Prevent method invocation if the service is not active
        if (!activator.isActive()) {
            return marshaller.marshal(new Message("Server", "Service is not active"));
        }

        try {
            // Extract method name and arguments
            String methodName = msg.data.split("/")[0];
            String[] args = msg.data.contains("/") ? msg.data.substring(msg.data.indexOf("/") + 1).split("/") : new String[0];
            Method method = findMethod(activator.getServiceClass(), methodName, args.length);
            Object[] convertedArgs = convertArgs(args, method.getParameterTypes());
            Object response = method.invoke(activator.getServiceInstance(), convertedArgs);
            return marshaller.marshal(new Message("Server", response.toString()));
        } catch (Exception e) {
            return marshaller.marshal(new Message("Server", "Error: " + e.getMessage()));
        }
    }

    private Method findMethod(Class<?> serviceClass, String methodName, int argCount) throws NoSuchMethodException {
        for (Method method : serviceClass.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == argCount) {
                method.setAccessible(true); // Necessary even for public methods
                return method;
            }
        }
        throw new NoSuchMethodException("Method " + methodName + " with " + argCount + " arguments not found in " + serviceClass.getName());
    }

    private Object[] convertArgs(String[] args, Class<?>[] types) {
        Object[] convertedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            convertedArgs[i] = convertStringToType(args[i], types[i]);
        }
        return convertedArgs;
    }

    private Object convertStringToType(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value; // Assumes all other types are String or compatible with String
    }
}
