package com.example.ibmsharepredictor

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ibmsharepredictor.databinding.ActivityMainBinding
import com.example.ibmsharepredictor.ml.ConvertedModel
import com.android.volley.Request
import org.tensorflow.lite.DataType
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.http.Body
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private var x by Delegates.notNull<Float>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@MainActivity,
            R.layout.activity_main
        )
        val compatList = CompatibilityList()

        val options = if (compatList.isDelegateSupportedOnThisDevice) {
            Model.Options.Builder().setDevice(Model.Device.GPU).build()
        } else {
            Model.Options.Builder().setNumThreads(4).build()
        }
        binding.button.setOnClickListener{
            x=binding.edit1.text.toString().toFloat()
            yohoh(this,options)
        }
        val url =
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=F4R7SZIADPMCG1C5"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                x=((response.getJSONObject("Global Quote").getString("03. high").toFloat()+response.getJSONObject("Global Quote").getString("04. low").toFloat())/2)
                binding.Text2.text =x.toString()
                binding.textView4.text="On: "+(response.getJSONObject("Global Quote").getString("07. latest trading day")).toString()
                yohoh(this,options)
            }
        ) { error: VolleyError ->
            "Please Try Again".also { binding.Text2.text = it }
            "Error".also { binding.Text2.text = it }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun yohoh(ctn: Context, options:Model.Options){
        val model = ConvertedModel.newInstance(this, options)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1, 1), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.putFloat(x)
        inputFeature0.loadBuffer(byteBuffer)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val data1 = outputFeature0.floatArray
        binding.Text1.text = (arrayOf(data1[0]).contentDeepToString())
        model.close()
    }
}