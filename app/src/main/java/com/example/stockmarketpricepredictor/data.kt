package com.example.stockmarketpricepredictor

data class data(
    val symbol:String,
    val open: Float,
    val high: Float,
    val low: Float,
    val price: Float,
    val volume: String,
    val latest_trading_day: String,
    val previous_close: Float,
    val change: Float,
    val change_percent: Float
)