#!/bin/bash

# Create directories for logs if they do not exist
mkdir -p server_logs client_logs


# Compile Java files without showing any warnings
echo "Compiling Java files..."
find . -name "*.java" -exec javac -nowarn {} \;

# Start InfoServer and MathServer in the background and redirect output to log files
echo "Starting servers..."
java src.Applications.InfoClientServer.InfoServer &> test/server_logs/InfoServer.log &
INFO_SERVER_PID=$!
echo "InfoServer started with PID $INFO_SERVER_PID"

java src.Applications.MathClientServer.MathServer &> test/server_logs/MathServer.log &
MATH_SERVER_PID=$!
echo "MathServer started with PID $MATH_SERVER_PID"

# Allow servers to initialize
sleep 5

# Run clients for stress testing
NUM_CLIENTS=100
echo "Starting $NUM_CLIENTS clients for each server..."

CLIENT_PIDS=()

for (( i=0; i<$NUM_CLIENTS; i++ ))
do
   echo "Starting InfoClient $i..."
   java src.Applications.InfoClientServer.InfoClient >> test/client_logs/InfoClient.log &
   CLIENT_PIDS+=($!)
   echo "Starting MathClient $i..."
   java src.Applications.MathClientServer.MathClient >> test/client_logs/MathClient.log &
   CLIENT_PIDS+=($!)
done

echo "All clients are running. Waiting for all clients to finish..."

# Wait only for client processes
for PID in "${CLIENT_PIDS[@]}"; do
    wait $PID
done

echo "All clients have completed their requests."

echo "Shutting down servers..."
kill -9 $INFO_SERVER_PID
kill -9 $MATH_SERVER_PID
echo "Servers have been shut down."

echo "Stress test completed."

