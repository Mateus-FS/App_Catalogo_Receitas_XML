package com.example.app_aprenda_hiragana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_aprenda_hiragana.ui.QuizTela
import com.example.app_aprenda_hiragana.ui.theme.App_Aprenda_HiraganaTheme
import com.example.app_aprenda_hiragana.viewModel.QuizViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App_Aprenda_HiraganaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: QuizViewModel = viewModel()

                    QuizTela(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}