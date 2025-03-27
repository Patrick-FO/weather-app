import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherapp.CurrentWeatherView
import com.example.weatherapp.DailyWeatherView
import com.example.weatherapp.HourlyWeatherView
import com.example.weatherapp.WeatherViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

//TODO Analyize this entire file to learn how pagers work, as I just vibe coded this without looking into it. Maybe check some documentation?
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeatherPager(
    activity: Activity,
    viewModel: WeatherViewModel,
    navController: NavHostController
) {
    // Load weather data when this composable is first launched
    LaunchedEffect(Unit) {
        viewModel.loadWeatherData(activity)
    }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.loadWeatherData(activity) },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
        ) {
            Text("Update Location")
        }

        // Page indicator dots
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
        )

        // The actual pager that allows swiping between screens
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (page) {
                    0 -> CurrentWeatherView(viewModel)
                    1 -> HourlyWeatherView(activity, viewModel)
                    2 -> DailyWeatherView(activity, viewModel)
                }
            }
        }
    }
}