#!/bin/bash

# Setup script for the devcontainer
echo "🚀 Setting up Loan Simulator & Interest Rate API development environment..."

# Create logs directory
mkdir -p /workspaces/logs

# Set permissions
sudo chown -R vscode:vscode /workspaces

# Install dependencies for both services
echo "📦 Installing Maven dependencies..."

# Detect workspace folder name
WORKSPACE_NAME=$(basename "$PWD")
WORKSPACE_ROOT="/workspaces/$WORKSPACE_NAME"

# Install dependencies for interest-rate-api
cd "$WORKSPACE_ROOT/interest-rate-api"
mvn dependency:resolve -q

# Install dependencies for loan-simulator (root)
cd "$WORKSPACE_ROOT"
mvn dependency:resolve -q

# Create useful aliases
echo "alias start-api='cd \"$WORKSPACE_ROOT/interest-rate-api\" && mvn spring-boot:run > /workspaces/logs/interest-rate-api.log 2>&1 &'" >> /home/vscode/.bashrc
echo "alias start-simulator='cd \"$WORKSPACE_ROOT\" && mvn spring-boot:run > /workspaces/logs/loan-simulator.log 2>&1 &'" >> /home/vscode/.bashrc
echo "alias logs-api='tail -f /workspaces/logs/interest-rate-api.log'" >> /home/vscode/.bashrc
echo "alias logs-simulator='tail -f /workspaces/logs/loan-simulator.log'" >> /home/vscode/.bashrc
echo "alias stop-services='pkill -f spring-boot:run'" >> /home/vscode/.bashrc

echo "✅ Setup complete!"
echo ""
echo "🔧 Available commands:"
echo "  start-api         - Start the Interest Rate API (port 8081)"
echo "  start-simulator   - Start the Loan Simulator (port 8080)"
echo "  logs-api          - View Interest Rate API logs"
echo "  logs-simulator    - View Loan Simulator logs"
echo "  stop-services     - Stop all running services"
echo ""
echo "📚 Documentation:"
echo "  Interest Rate API: http://localhost:8081/swagger-ui.html"
echo "  Loan Simulator:    http://localhost:8080"