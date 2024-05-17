# sORB

sORB (simple ORB) is a simplistic and naive re-implementation of Java RMI that I built for my `Design and Architecture of Complex Software Systems` course. It aims to provide basic remote method invocation functionality, mirroring the fundamental concepts of Java RMI.

## Features

- **Dynamic Proxy Support**: Utilizes Java dynamic proxies for remote method invocation.
- **Service Activation/Deactivation**: Allows services to be dynamically activated and deactivated.
- **Custom Marshaller**: Includes a custom marshaller for converting objects to byte streams and vice versa.
- **Naming Service**: Provides a simple naming service for service lookup and registration.
- **Example Applications**: Includes example applications (Math and Info services) to demonstrate the usage of sORB.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Running the Naming Service

The naming service is essential for service registration and lookup.

1. Compile the code:

   ```sh
   javac src/sORB/NamingService.java
   ```

2. Run the naming service:

   ```sh
   java src.sORB.NamingService
   ```

### Running the Example Applications

#### Math Service

1. Start the Math Server:

   ```sh
   javac -cp . src/Applications/MathClientServer/MathServer.java
   java src.Applications.MathClientServer.MathServer
   ```

2. Start the Math Client:

   ```sh
   javac -cp . src/Applications/MathClientServer/MathClient.java
   java src.Applications.MathClientServer.MathClient
   ```

#### Info Service

1. Start the Info Server:

   ```sh
   javac -cp . src/Applications/InfoClientServer/InfoServer.java
   java src.Applications.InfoClientServer.InfoServer
   ```

2. Start the Info Client:

   ```sh
   javac -cp . src/Applications/InfoClientServer/InfoClient.java
   java src.Applications.InfoClientServer.InfoClient
   ```

## How It Works

### Activator Proxy

The `ActivatorProxy` class manages the activation and deactivation of services. It implements the `InvocationHandler` interface to handle method calls dynamically, and removes the service instance when it's no longer used(told by the Naming Service).

### Server-Side Proxy

The `ServerSideProxy` class is responsible for receiving requests from clients, invoking the appropriate methods on the service, and sending back the results.

### Client Proxy

The `ClientProxy` class acts as a dynamic proxy on the client side, converting method calls into messages that are sent over the network to the server-side proxy.

### Naming Service

The `NamingService` class provides a registry for service names and their corresponding addresses. Clients use the naming service to look up the addresses of the services they want to invoke, and it also handles sending activate/deactivate commands, as it keeps track of how many clients are using a service.
