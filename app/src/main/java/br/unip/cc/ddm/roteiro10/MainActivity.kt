package br.unip.cc.ddm.roteiro10

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.unip.cc.ddm.roteiro10.ui.theme.Roteiro10Theme // Verifique se o nome do seu tema é este
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private var startTimeMillis: Long = 0L
    private var totalInteractionTimeMillis: Long = 0L
    private lateinit var sharedPreferences: SharedPreferences

    // Constante para a chave do SharedPreferences
    private val PREFS_NAME = "InteractionTimePrefs"
    private val KEY_TOTAL_TIME = "totalInteractionTime"
    private val TAG = "MainActivityLifecycle" // Tag para logs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToast("onCreate()")
        Log.d(TAG, "onCreate()")

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        totalInteractionTimeMillis = sharedPreferences.getLong(KEY_TOTAL_TIME, 0L)

        setContent {
            Roteiro10Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(totalInteractionTimeMillis) {
                        // Ao clicar no botão, navegar para a SummaryActivity
                        val intent = Intent(this@MainActivity, SummaryActivity::class.java).apply {
                            putExtra("totalTime", totalInteractionTimeMillis)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showToast("onStart()")
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        showToast("onResume() - Iniciando contagem")
        Log.d(TAG, "onResume() - Iniciando contagem")
        startTimeMillis = SystemClock.elapsedRealtime() // Tempo desde o boot, mais preciso para medir durações
    }

    override fun onPause() {
        super.onPause()
        showToast("onPause() - Pausando contagem")
        Log.d(TAG, "onPause() - Pausando contagem")
        val elapsedTime = SystemClock.elapsedRealtime() - startTimeMillis
        totalInteractionTimeMillis += elapsedTime
        saveTotalTime()
    }

    override fun onStop() {
        super.onStop()
        showToast("onStop()")
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        val formattedTime = formatTime(totalInteractionTimeMillis)
        showToast("onDestroy() - Tempo total de interação: $formattedTime")
        Log.d(TAG, "onDestroy() - Tempo total de interação: $formattedTime")
        // O tempo já foi salvo em onPause, mas é bom para um último log/toast aqui.
    }

    private fun saveTotalTime() {
        sharedPreferences.edit().putLong(KEY_TOTAL_TIME, totalInteractionTimeMillis).apply()
        Log.d(TAG, "Tempo total salvo: ${totalInteractionTimeMillis}ms")
    }

    private fun showToast(message: String) {
        // Usamos this@MainActivity como contexto, pois estamos dentro de uma Activity
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    // Função auxiliar para formatar o tempo em um formato legível
    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}

@Composable
fun MainScreen(totalTimeMillis: Long, onNavigateToSummary: () -> Unit) {
    val formattedTime = formatTime(totalTimeMillis)
    val context = LocalContext.current // Pega o contexto para uso no Composable

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tempo Total de Interação (Sessão Atual + Salvo):",
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateToSummary) {
            Text("Ver Resumo Detalhado")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Interaja com a tela (navegue para fora, volte, feche o app) para ver a contagem e os toasts do ciclo de vida.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

// Função para formatar tempo no Composable (precisa ser definida fora do Composable para @Preview ou receber como parâmetro)
private fun formatTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Roteiro10Theme {
        MainScreen(123456L) {} // Valor de exemplo para o preview
    }
}