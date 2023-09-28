package com.spongycode.servicenet

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.spongycode.servicenet.data.model.University


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Container(viewModel: MainViewModel) {
    val backgroundColor by viewModel.backgroundColor.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            ExposedDropdownMenuSample(MainActivity.viewModel)
            Spacer(modifier = Modifier.height(10.dp))
            LookCurrentUIScreen(MainActivity.viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LookCurrentUIScreen(viewModel: MainViewModel) {
    val selectedOptionText by viewModel.selectedOptionText.collectAsState()
    when (selectedOptionText) {
        viewModel.options[0] -> UniversityListScreen(viewModel)
        viewModel.options[1] -> QuoteText(viewModel)
        viewModel.options[2] -> TimeText(viewModel)
    }
}

@Composable
fun QuoteText(viewModel: MainViewModel) {
    val quote by viewModel.quote.collectAsState()
    CommonUIQuoteAndTime(text = quote, size = 30)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeText(viewModel: MainViewModel) {
    val time by viewModel.time.collectAsState()
    if (time.contains("Time")) {
        CommonUIQuoteAndTime(text = time, size = 30)
    } else {
        val filteredText = time.substring(0, 10) + '\n' + time.substring(11, 19)
        CommonUIQuoteAndTime(text = filteredText, size = 40)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CommonUIQuoteAndTime(text: String, size: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    tileMode = TileMode.Mirror
                ),
                fontSize = size.sp,
                textAlign = TextAlign.Center
            ),
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun UniversityListScreen(viewModel: MainViewModel) {
    val universitiesList by viewModel.universitiesList.collectAsState()
    val backgroundColor by viewModel.backgroundColor.collectAsState()
    Surface(color = backgroundColor) {
        UniversityList(universitiesList)
    }
}


@Composable
fun UniversityCard(university: University, index: Int) {
    val activity = LocalContext.current as Activity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = universitiesListColors[index % universitiesListColors.size],
        ), shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = university.name,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            university.country?.let {
                Text(
                    text = it,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            university.web_pages?.forEach { link ->
                ClickableText(
                    text = AnnotatedString(link),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    val intent = Intent(activity.applicationContext, WebViewActivity::class.java)
                    intent.putExtra("URL", link)
                    activity.startActivity(
                        Intent(intent)
                    )
                }
            }
        }
    }
}


@Composable
fun UniversityList(universitiesList: List<University>) {
    LazyColumn {
        universitiesList.forEachIndexed { index, university ->
            item {
                UniversityCard(university, index)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuSample(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val selectedOptionText by viewModel.selectedOptionText.collectAsState()
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFFFFFFF),
                textColor = Color(0xFF000000),
                focusedBorderColor = Color(0xFF2C461C),
                unfocusedBorderColor = Color(0xFF000000)
            ),
            textStyle = TextStyle(fontWeight = FontWeight.Bold)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize()
                .padding(horizontal = 10.dp)
        ) {
            viewModel.options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        viewModel.changeSelectedOptionText(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun WebViewCompose(url: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}


private val universitiesListColors = listOf(
    Color(0xFFE2FCF2),
    Color(0xFFD6DEF7),
    Color(0xFFFAEAE7)
)

private val gradientColors = listOf(
    Color(0xFF1C43B9),
    Color(0xFF000A29),
)