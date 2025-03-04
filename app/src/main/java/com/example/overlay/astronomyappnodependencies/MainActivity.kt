package com.example.overlay.astronomyappnodependencies

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.overlay.astronomyappnodependencies.astronomy.AstronomyListViewModel
import com.example.overlay.astronomyappnodependencies.astronomy.AstronomyListViewState
import com.example.overlay.astronomyappnodependencies.network.api.AstronomyPicture
import com.example.overlay.astronomyappnodependencies.ui.theme.AstronomyAppNoDependenciesTheme

/**
 * Implement a photo list of incoming pictures
 *
 * AppCompactActivity extends FragmentActivity which extends ComponentActivity
 *
 * ComponentActivity has all you need for Compose-only
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<AstronomyListViewModel>()
            val lazyListState = rememberLazyListState()
            val viewState by viewModel.viewState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            AstronomyAppNoDependenciesTheme {
                when (viewState) {
                    is AstronomyListViewState.Loading -> {
                        Screen { CircularProgressIndicator() }
                    }
                    is AstronomyListViewState.Content -> {
                        PhotoListView(
                            (viewState as AstronomyListViewState.Content).list,
                            { text -> Toast.makeText(context, text, Toast.LENGTH_SHORT).show() },
                            lazyListState
                        )
                    }
                    is AstronomyListViewState.Error -> {
                        Screen { Text(text = "Error!", modifier = Modifier.fillMaxSize()) }
                    }
                }
            }
        }
    }
}

@Composable
fun Screen(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun PhotoListView(
    photos: List<AstronomyPicture>,
    onClick: (String) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(photos, key = { photo -> photo.url }) { photo ->
            ListItem(
                headlineContent =  {
                    Text(photo.title)
                },
                leadingContent = {
                    AsyncImage(
                        model = photo.url,
                        contentDescription = photo.title,
                        placeholder = painterResource(R.drawable.baseline_image_24),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp)
                    )
                },
                modifier = Modifier.clickable {
                    onClick(photo.url)
                }.shadow(6.dp)
            )
        }
    }
}