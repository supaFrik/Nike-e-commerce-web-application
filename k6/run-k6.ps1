param (
    [ValidateSet("smoke", "load", "stress", "spike", "soak")]
    [string]$Type = "smoke"
)

docker compose -f k6/k6-testing.yml run --rm k6 run `
    -o experimental-prometheus-rw `
    "/scripts/$Type/product-list.js"