package com.example.stockmarketpricepredictor

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stockmarketpricepredictor.ml.ConvertedModel
import com.example.stockmarketpricepredictor.ui.theme.BackgroundColor
import com.example.stockmarketpricepredictor.ui.theme.CardColor
import com.example.stockmarketpricepredictor.ui.theme.ElementColor
import com.example.stockmarketpricepredictor.ui.theme.StockMarketPricePredictorTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockMarketPricePredictorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DisplayListView()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockMarketPricePredictorTheme {
        DisplayListView()
    }
}

fun parseJSONArray(x: Float, ctx: Context,onValueChange: (Float) -> Unit ={}) {

    // on below line we are creating a variable for url
    var url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=F4R7SZIADPMCG1C5"

    // on below line we are creating a
    // variable for our request queue
    val queue = Volley.newRequestQueue(ctx)

    // on below line we are creating a request variable
    // for making our json object request.
    var x:Float?=null
    val request =JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            onValueChange((response.getJSONObject("Global Quote").getString("03. high").toFloat()+response.getJSONObject("Global Quote").getString("04. low").toFloat())/2)
        }
    ) {
        onValueChange(-1f)
    }
    // at last we are adding our request to our queue.
    queue.add(request)

}

@Composable
fun DisplayListView() {
    val context = LocalContext.current

    // on below line we are creating and
    // initializing our array list
    var courseList by remember { mutableStateOf<Float>(0f) }
    parseJSONArray(courseList, context) { courseList = it }

    val model = ConvertedModel.newInstance(context)
    val byteBuffer = ByteBuffer.allocateDirect(4)
    byteBuffer.order(ByteOrder.nativeOrder())
    byteBuffer.putFloat(courseList)
// Creates inputs for reference.
    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1, 1), DataType.FLOAT32)
    inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer
    val data1 = outputFeature0.floatArray
// Releases model resources if no longer used.
    model.close()
//    LazyColumn {
//        // on below line we are populating
//        // items for listview.
//        item() {
//            // on below line we are specifying ui for each item of list view.
//            // we are specifying a simple text for each item of our list view.
//            Text(data1[0].toString(), modifier = Modifier.padding(15.dp))
//            // on below line we are specifying
//            // divider for each list item
//            Divider()
//        }
//    }
    var colorList=Color.Green
    if((data1[0]-courseList)<0){
        colorList=Color.Red
    }
    val x=(((data1[0]-courseList)/courseList)*100f)
    var y=String.format("%.3f",x)
    var regulator=0.01f
    if((((data1[0]-courseList)/courseList)*100f)>1){
        regulator=0.1f
    }
    else if((((data1[0]-courseList)/courseList)*100f)>10){
        regulator=1f
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor)) {
        Card(
            Modifier
                .padding(5.dp)
                .height(700.dp),
            backgroundColor = CardColor,
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Column(Modifier) {

                    Progressed(radius = 260, strokeWidth = 30.dp, (data1[0]-courseList), colorList, Sum = courseList, regulator = regulator)
                }
                Spacer(modifier = Modifier.size(50.dp))
                Card(
                    Modifier
                        .padding(5.dp),
                    backgroundColor = ElementColor,
                    shape = MaterialTheme.shapes.medium
                ) {
                    LazyColumn {
                        item {
                            y=(String.format("%.3f",x))
                            element(name = "Pofit",colorList, value = y, regulator = regulator)
                        }
                        item {
                            y=(String.format("%.2f",courseList))
                            element(name = "Original Value", CardColor, value = y, regulator = -1f)
                        }
                        item {
                            y=(String.format("%.2f",data1[0]))
                            element(name = "Predicted Value(after 1hr)", CardColor, value = y, regulator = -1f)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun Progressed(radius:Int, strokeWidth: Dp, list:Float, listColor:Color,Sum:Float,regulator:Float) {
    Column(modifier = Modifier
        .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(
            modifier = Modifier
                .height(radius.dp)
                .width(radius.dp)
        ) {
            var angle=0f
            Log.d(Sum.toString(),"YESS")
            drawArc(
                color = (Color.LightGray),
                angle,
                360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Butt),
            )
            angle=-90f
            Log.d(Sum.toString(),"YESS")
            drawArc(
                color = (listColor),
                angle,
                (360f*((list)/(Sum*regulator))),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Butt),
            )
        }
    }
}
@Composable
fun element(name:String, color:Color,value:String,regulator: Float){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp), backgroundColor = ElementColor, shape = MaterialTheme.shapes.medium)  {
        Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
            Card(
                Modifier
                    .height(50.dp)
                    .padding(horizontal = 10.dp),backgroundColor=(color),shape= RoundedCornerShape(0.dp)){ Spacer(modifier = Modifier.width(20.dp))}
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "$name:",
                fontSize = 18.sp,
                style = MaterialTheme.typography.h2,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(5.dp))
            if(regulator!=-1f) {
                Text(
                    text = "$value%",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
            }
            else{
                Text(
                    text = "$value$",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
            }
            if(regulator!=-1f) {
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "(max ${(regulator * 100f)}%)",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
            }
        }
    }
}
//https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=IBM&apikey=F4R7SZIADPMCG1C5