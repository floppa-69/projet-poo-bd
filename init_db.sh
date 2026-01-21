#!/bin/bash
echo "Initializing Database..."
echo "You may be asked for your sudo password."

# Try using mariadb client
if command -v mariadb &> /dev/null; then
    sudo mariadb < schema.sql
elif command -v mysql &> /dev/null; then
    sudo mysql < schema.sql
else
    echo "Error: neither 'mariadb' nor 'mysql' command found."
    exit 1
fi

if [ $? -eq 0 ]; then
    echo "Database initialized successfully!"
    echo "User: pharmacy_user created."
else
    echo "Failed to initialize database."
fi
