package br.unip.cc.ddm.roteiro10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.unip.cc.ddm.roteiro10.ui.theme.Roteiro10Theme // Verifique se o nome do seu tema é este
import java.util.concurrent.TimeUnit

class SummaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val totalTime = intent.getLongExtra("totalTime", 0L)

        setContent {
            Roteiro10Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SummaryScreen(totalTime)
                }
            }
        }
    }
}

@Composable
fun SummaryScreen(totalTimeMillis: Long) {
    val hours = TimeUnit.MILLISECONDS.toHours(totalTimeMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeMillis) % 60
    val milliseconds = totalTimeMillis % 1000

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resumo da Interação",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "Tempo Total:",
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = String.format("%02d horas", hours),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = String.format("%02d minutos", minutes),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = String.format("%02d segundos", seconds),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = String.format("%03d milissegundos", milliseconds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    Roteiro10Theme {
        SummaryScreen(123456789L) // Valor de exemplo para o preview
    }
}