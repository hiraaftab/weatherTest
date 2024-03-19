package com.example.weathertest



import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathertest.data.WeatherResponse
import com.example.weathertest.ui.theme.WeatherTestTheme
import com.example.weathertest.viewModel.WeatherViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource



import coil.compose.rememberImagePainter

import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var latitude by mutableDoubleStateOf(0.0)
    private var longitude by mutableDoubleStateOf(0.0)
    private val viewModel: WeatherViewModel by viewModels()

    private val locationHelper: LocationHelper by lazy { LocationHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.purple_200)
                ) {

                   locationHelper.getCityName(this,latitude,longitude)?.let {
                        WeatherScreenContent(this,viewModel = viewModel,
                            it
                        )
                    }
                }
            }
        }

        locationHelper.requestLocationUpdates()

        locationHelper.getLastLocation { latitude, longitude ->
            // Use latitude and longitude here
            this@MainActivity.longitude = longitude
            this@MainActivity.latitude = latitude
        }
    }

}



@Composable
fun WeatherScreenContent(context : Context, viewModel: WeatherViewModel, cityName :String) {


    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(cityName)
    }


    val weatherData by viewModel.weatherData.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    if(errorMessage !=null)
    {
        Toast.makeText(context,"$errorMessage",Toast.LENGTH_SHORT).show()

    }
    WeatherScreen(weatherData = weatherData, viewModel =viewModel )
}

@Composable
fun WeatherScreen(
    weatherData: WeatherResponse?,
    viewModel: WeatherViewModel
) {

    var cityName by remember { mutableStateOf("Lahore") }

      Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally
         ) {

          Spacer(modifier = Modifier.height(40.dp))

          TextField(
              value = cityName,
              singleLine = true,
              onValueChange = { cityName = it },
              label = { Text(stringResource(R.string.enter_city)) }
          )

          Spacer(modifier = Modifier.height(16.dp)) // Add some space between the fields



          Button(onClick = {

                  if (cityName!= null && cityName.isNotEmpty()) {
                      // Update the latitude and longitude values
                      
                      viewModel.getCurrentWeather(cityName)
                  }

              // Action to perform when the button is clicked
          },
              colors = ButtonDefaults.buttonColors(
                  containerColor = colorResource(R.color.purple_500)
              )) {
              Text(stringResource(R.string.refresh))

          }


          Spacer(modifier = Modifier.height(20.dp)) // Add some space between the fields

          if(weatherData != null) {

              Text(
                  text = "Weather in ${weatherData?.name}",
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  modifier = Modifier.padding(bottom = 8.dp)
              )




              Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
              ) {
                  Text(
                      text = "${(weatherData?.main?.temp?.minus(273.15))?.toInt()} °C",
                      fontSize = 50.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                  )

                  Text(
                      text = weatherData?.weather?.firstOrNull()?.main ?: "",
                      fontSize = 24.sp,
                      color = Color.White,
                      modifier = Modifier.padding(start = 12.dp)
                  )
              }

              Spacer(modifier = Modifier.height(20.dp))
              Text(
                  text = "Humidity ${weatherData?.main?.humidity} %",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  modifier = Modifier.padding(bottom = 8.dp)
              )

              Text(
                  text = "wind speed ${weatherData?.wind?.speed} mph",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  modifier = Modifier.padding(bottom = 8.dp)
              )


              Spacer(modifier = Modifier.height(10.dp))

              CoilImageComponent("https://openweathermap.org/img/w/${weatherData?.weather?.firstOrNull()?.icon}.png")
          }

        // Other weather details can be displayed here
    }
}


@Composable
fun CoilImageComponent(imageUrl: String) {
    Image(
        modifier = Modifier
            .size(200.dp),
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                // Optional: Add image transformations

            }
        ),
        contentDescription = "Coil Image",

    )
}

