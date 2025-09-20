# Loan Simulator & Interest Rate API - Development Environment

This devcontainer provides a complete development environment for the Loan Simulator and Interest Rate API microservices.

## What's Included

- **Java 21** with Maven
- **VS Code Java Extension Pack** for full Java development support
- **Spring Boot Dashboard** for easy service management
- **REST Client extensions** for API testing
- **Auto-configured ports** (8080, 8081) with forwarding
- **Automatic service startup** (Interest Rate API runs in background)

## Quick Start

1. Open this repository in GitHub Codespaces or VS Code with Dev Containers
2. The Interest Rate API will start automatically on port 8081
3. Access the API documentation at: http://localhost:8081/swagger-ui.html
4. Start the Loan Simulator manually with: `start-simulator`

## Available Commands

- `start-api` - Start the Interest Rate API (port 8081)
- `start-simulator` - Start the Loan Simulator (port 8080)  
- `logs-api` - View Interest Rate API logs
- `logs-simulator` - View Loan Simulator logs
- `stop-services` - Stop all running services

## Service URLs

- **Interest Rate API**: http://localhost:8081
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **Loan Simulator**: http://localhost:8080

## Development Workflow

1. The Interest Rate API starts automatically and runs in the background
2. Use the Spring Boot Dashboard in VS Code to manage services
3. Edit code and services will auto-reload
4. Test APIs using the built-in REST client or Thunder Client
5. View logs using the provided aliases or VS Code terminal

## Testing Inter-Service Communication

```bash
# Test the Interest Rate API directly
curl -X POST http://localhost:8081/api/interest-rate/calculate \
  -H "Content-Type: application/json" \
  -d '{"ageCategory":"ADULT","professionalCategory":"EMPLOYEE","monthlyNetIncome":3500}'

# Get available categories
curl http://localhost:8081/api/interest-rate/categories/age
```

## Troubleshooting

- If services fail to start, check logs with `logs-api` or `logs-simulator`
- Restart services with `stop-services` followed by `start-api` or `start-simulator`
- Ensure ports 8080 and 8081 are available