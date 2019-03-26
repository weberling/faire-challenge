# faire-challenge
faire-challenge for vanhack

# run
```
gradle bootRun -Pargs=--faire_token={token}

# endpoints

## consume all orders
POST /orders/consume

## return statistics of all orders
GET /orders/statistics

## print statistics of all orders and consume orders
POST /orders/consume-statistics

