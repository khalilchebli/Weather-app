package com.exemple.weatherapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.exemple.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 18fa041a2e03848bfa93eb4d4f19261e
class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("jaipur")
        SearchCity()


    }

    private fun SearchCity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(Cityname:String) {
         val retrofit=Retrofit.Builder()
             .addConverterFactory(GsonConverterFactory.create())
             .baseUrl("https://api.openweathermap.org/data/2.5/")
             .build().create(ApiInterface::class.java)
         val response=retrofit.getWeatherData(Cityname ,"18fa041a2e03848bfa93eb4d4f19261e" , units = "metric")
         response.enqueue(object : Callback<WeatherApp>{
             override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                 val responseBody=response.body()
                 if (response.isSuccessful && responseBody !=null){
                     val temperature=responseBody.main.temp.toString()
                     val humidity=responseBody.main.humidity
                     val windSpeed=responseBody.wind.speed
                     val sunRise=responseBody.sys.sunrise.toLong()
                     val sunSet=responseBody.sys.sunset.toLong()
                     val sea=responseBody.main.pressure
                     val condition=responseBody.weather.firstOrNull()?.main?:"unknow"
                     val maxTemp=responseBody.main.temp_max
                     val minTemp=responseBody.main.temp_min
                     binding.temp.text="$temperature °C"
                     binding.weather.text=condition
                     binding.maxTemp.text="Max Temp: $maxTemp °C"
                     binding.minTemp.text="Min Temp: $minTemp °C"
                     binding.humudity.text= "$humidity %"
                     binding.wind2.text="$windSpeed m/s"
                     binding.sunrise2.text="${time(sunRise)} "
                     binding.sunset2.text="${time(sunSet)}"
                     binding.sea2.text="$sea hPa"
                     binding.condition2.text=dayName(System.currentTimeMillis())
                         binding.date.text=date()
                         binding.Cityname.text=" $Cityname"


                     //Log.d("TAG", "onResponse: $temperature")


                     changeImagsaccordingtoWeatherCondition(condition)
                 }
             }

             override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                 TODO("Not yet implemented")
             }

         })

     }

    private fun changeImagsaccordingtoWeatherCondition(conditions: String) {
        when(conditions){

            "Clear sky","Sunny","Clear","sun" ->{
                binding.root.setBackgroundResource(R.drawable.sunnybackground)
                binding.lottieAnimationView.setAnimation(R.raw.sunanimation)
            }
            "Partly Clouds", "Clouds" , "Overcast","Mist","Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.cloudbackground)
            binding.lottieAnimationView.setAnimation(R.raw.coldanimation)
        }
        "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Thunderstorm"," Rain" ->{
        binding.root.setBackgroundResource(R.drawable.rainbackground)
        binding.lottieAnimationView.setAnimation(R.raw.rainanimation)
    }
    "Light Snow","Moderate Snow","Heavy Snow","Blizzard","Snow" ->{
    binding.root.setBackgroundResource(R.drawable.snowbackground)
    binding.lottieAnimationView.setAnimation(R.raw.snowanimation)
}
            else->{
                binding.root.setBackgroundResource(R.drawable.sunnybackground)
                binding.lottieAnimationView.setAnimation(R.raw.sunanimation)

            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format((Date()))

    }
    private fun time(timestamp:Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))

    }

    fun dayName(timestamp:Long):String{

        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
     }

 }

