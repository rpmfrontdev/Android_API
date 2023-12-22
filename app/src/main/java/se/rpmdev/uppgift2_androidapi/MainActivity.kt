package se.rpmdev.uppgift2_androidapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import se.magictechnology.androidapi.ui.theme.AndroidAPITheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            uppgift2_androidapitheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    currentWeather(lat = "xxx", long = "yyy")
                }
            }
        }
    }
}

@Composable
fun currentWeather(lat: String, long: String) {
    var currentCount by remember { mutableStateOf( 10 ) }

    Column (
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)

    )
    {
        BoldGreetingText( )

        Text(currentCount.toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            Button(
                onClick = {currentCount++ },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Plus",

                    )
            }
            Button(
                onClick = { currentCount-- },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Minus",

                    )
            }
        }

        Button(
            onClick = { currentCount = 10 },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Reset",

                )
        }
    }
}

@Composable
fun BoldGreetingText(){
    Text("Start",
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(Color.Cyan)
            .wrapContentHeight(),
        style = MaterialTheme.typography.bodyLarge
    )

}


@SuppressLint("ModifierFactoryUnreferencedReceiver")


@Preview(showBackground = true)

@Composable
fun weatherPreview() {
    uppgift2_androidapiTheme {
        currentWeather(lat = "xxx", long = "yyy")
    }
}

@Serializable
data class currentWeather(val latitude: String, val longitude: String, val temperature_2m: String, val precipitation: String, val wind_speed_10m: String)








package se.magictechnology.androidapi



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAPITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Chuckjoke()
                }
            }
        }
    }
}

@Composable
fun Chuckjoke(modifier: Modifier = Modifier) {

    val client = OkHttpClient()

    var thejoke by remember { mutableStateOf("joke") }

    var started by remember { mutableStateOf(false) }

    fun loadjoke() {

        val req = Request.Builder().url("https://api.chucknorris.io/jokes/random").build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("APIDEBUG", "HÄMTNING INTE OK")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    var responseString = response.body!!.string()

                    Log.i("APIDEBUG", responseString)

                    val jokedata = Json{ ignoreUnknownKeys = true}.decodeFromString<Chuckjoke>(responseString)

                    thejoke = jokedata.value

                }
            }
        })
    }

    LaunchedEffect(started) {
        loadjoke()

    }

    Column {
        Text(
            text = thejoke,
            modifier = modifier
        )
        Button(onClick = { loadjoke() }) {
            Text(text = "Load joke")
        }
    }



}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidAPITheme {
        Chuckjoke()
    }
}

@Serializable
data class Chuckjoke(val categories : List<String>, val created_at : String, val value : String)

