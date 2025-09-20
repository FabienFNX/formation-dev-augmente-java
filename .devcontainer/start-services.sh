#!/bin/bash

# Auto-start services script
echo "🌟 Starting services automatically..."

# Detect workspace folder
WORKSPACE_NAME=$(basename "$PWD")
WORKSPACE_ROOT="/workspaces/$WORKSPACE_NAME"

# Wait a moment for the workspace to be fully ready
sleep 3

# Start Interest Rate API first (required dependency)
echo "🚀 Starting Interest Rate API on port 8081..."
cd "$WORKSPACE_ROOT/interest-rate-api"
nohup mvn spring-boot:run > /workspaces/logs/interest-rate-api.log 2>&1 &
API_PID=$!

# Wait for API to be ready
echo "⏳ Waiting for Interest Rate API to start..."
for i in {1..30}; do
  if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1 || curl -f http://localhost:8081/api/interest-rate/config >/dev/null 2>&1; then
    echo "✅ Interest Rate API is ready!"
    break
  fi
  sleep 2
done

# Optionally start the Loan Simulator as well
# Uncomment the lines below if you want both services to start automatically
# echo "🚀 Starting Loan Simulator on port 8080..."
# cd "$WORKSPACE_ROOT"
# nohup mvn spring-boot:run > /workspaces/logs/loan-simulator.log 2>&1 &

echo "🎉 Services startup complete!"
echo ""
echo "📋 Service Status:"
echo "  ✅ Interest Rate API: http://localhost:8081 (running)"
echo "  📖 API Documentation: http://localhost:8081/swagger-ui.html"
echo ""
echo "💡 To start the Loan Simulator manually, run:"
echo "   start-simulator"
echo ""
echo "📊 To view logs, use:"
echo "   logs-api"