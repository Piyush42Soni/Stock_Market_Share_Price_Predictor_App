# Stock Market Share Price Predictor App
<div align="center">
<img src="https://th.bing.com/th/id/OIG.KaoRXthw6k2vgT_RVFgi?w=173&h=173&c=6&pcl=1b1a19&r=0&o=5&dpr=1.1&pid=ImgGn" width=250 style="border-radius:50%"/>
  
  ![Python 3.10](https://img.shields.io/badge/Python-287595.svg?style=for-the-badge&logo=Python&logoColor=white)
  ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
  ![TensorFlow](https://img.shields.io/badge/TensorFlow-%23FF6F00.svg?style=for-the-badge&logo=TensorFlow&logoColor=white)
  ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
  ![Android](https://img.shields.io/badge/Android-37F52FF.svg?style=for-the-badge&logo=Android&logoColor=white)
  ![SQLite](https://img.shields.io/badge/SQLite-293764.svg?style=for-the-badge&logo=SQLite&logoColor=white)
</div>

## Description
It is an Android app that is made using the latest Jetpack Compose library and uses the TF Lite model to forecast the future value of IBM's stock. In my deep learning model, I used LSTM and CNN layers for time series prediction of the stocks, then converted the model into a TF-Lite model and sent it to my Android app. I have used the alphavantage.co API to get the data for training. I used the Retrofit and Volly libraries to get data from the API, which is in JSON format.

## Locations
- [SharePricePredictor.ipynb is the jupyter notebook of my DL model](https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/SharePricePredictor.ipynb)
- [converted_model.tflite is the TFlite model](https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/app/src/main/ml/converted_model.tflite)
- [App Home Page Code](https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/app/src/main/java/com/example/stockmarketpricepredictor/MainActivity.kt)

## App UI Design

<div align="center"> 
 <img src="https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/AppUIdesign.png" width=250 />
 <img src="https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/AppUIdesign2.png" width=250 />
</div>

## Deep Learning Model Structure

<div align="center"> 
 <img src="https://github.com/Piyush42Soni/Stock_Market_Share_Price_Predictor_App/blob/master/Model_structure.png" width=250 />
</div>

## Contributing/Running the project locally

The application can be downloaded from the releases section 

Clone the repository:

```bash
$ git clone https://github.com/Piyush42Soni/OS_project.git
```
From here on you can run, test or update the various models based on the dataset.
