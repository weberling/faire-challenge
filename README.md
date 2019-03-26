# faire-challenge
faire-challenge for vanhack
I had some doubts in this challenge.
Then I put some TODOs for the doubts

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

