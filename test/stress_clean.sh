#!/bin/bash

echo "Cleaning up logs..."
find . -name "*.log" -delete -print;

echo "Cleaning up class files..."
find . -name "*.class" -delete -print;

echo "Clean up completed."
