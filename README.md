# Stock Market Share Price Predictor App
## Description
It is an Android app that uses the TF lite model to forecast the future value of IBM's stock.
In my deep learning model, I used LSTM layers to predict the value of stocks, then converted the model into a TF-Lite model and sent it to my Android app.
I have used the alphavantage.co API to get the data for training. I used the Retrofit and Volly libraries to get data from the API, which is in JSON format.

SharePricePredictor.ipynb is the jupyter notebook of my DL model
Stock_Market_Share_Price_Predictor_App/app/src/main/ml/converted_model.tflite is the TF lite model
