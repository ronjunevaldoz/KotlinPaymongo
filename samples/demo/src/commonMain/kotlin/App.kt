import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ronjunevaldoz.paymongo.PayMongo
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLink
import kotlinx.coroutines.launch


/**
 * Sample usage
 * TODO improve UI design
 */
@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var webhooks by remember { mutableStateOf<List<String>>(listOf()) }
        var paymentLink by remember { mutableStateOf<PaymentLink?>(null) }
        var secretKey by remember { mutableStateOf("sk_test_mqRYWTNuCR3pnKQvD48iJfng") }
        val client by remember(secretKey) {
            mutableStateOf(
                PayMongo(
                    config = PayMongo.Config(
                        secretKey = secretKey
                    )
                )
            )
        }

        Scaffold { padding ->
            Column(
                modifier = Modifier.padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = secretKey,
                    onValueChange = { secretKey = it },
                    label = { Text("Secret Key") }
                )
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
                Button(onClick = {
                    scope.launch {
                        runCatching {
                            client.getLink("link_WrnsXCjNtdv8wfRDwcs6APjy")
                        }.fold(
                            onSuccess = {
                                paymentLink = it
                            },
                            onFailure = {
                                it.printStackTrace()
                            }
                        )
                    }
                }) {
                    Text("Get link by id")
                }
                Button(onClick = {
                    scope.launch {
                        runCatching {
                            client.getLink("NJUgWgz")
                        }.fold(
                            onSuccess = {
                                paymentLink = it
                            },
                            onFailure = {
                                it.printStackTrace()
                            }
                        )
                    }
                }) {
                    Text("Get link by reference")
                }
                if (paymentLink != null) {
                    Text("Payment Link: $paymentLink")
                }
                if (webhooks.isNotEmpty()) {
                    Text("Webhooks")
                    LazyColumn {
                        items(webhooks) {
                            Text(it)
                        }
                    }
                }
            }
        }
    }
}