import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.ronjunevaldoz.paymongo.Paymongo
import kotlinx.coroutines.launch

val client = Paymongo(
    config = Paymongo.Config(
        secretKey = "sk_test_xxxxxxxxxxxxxxxx"
    )
)

@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var webhooks by remember { mutableStateOf<List<String>>(listOf()) }

        Scaffold {
            Column(
                modifier = Modifier.padding(it)
            ) {
                Button(onClick = {
                    scope.launch {
                        runCatching {
                            client.getWebhooks()
                        }.fold(
                            onSuccess = {
                                webhooks = it.data.map { it.id }
                            },
                            onFailure = {
                                it.printStackTrace()
                            }
                        )
                    }
                }) {
                    Text("Get webhooks")
                }
                LazyColumn {
                    items(webhooks) {
                        Text(it)
                    }
                }
            }
        }
    }
}