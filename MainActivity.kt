package com.example.mycalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var amount by remember { mutableStateOf("0") }
            var fromCurrency by remember { mutableStateOf("IDR") }
            var toCurrency by remember { mutableStateOf("USD") }

            val currencyOptions = listOf("IDR", "USD", "MYR", "EUR")

            Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Currency Calculator", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row for selecting currencies
                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DropdownSelector("From", currencyOptions, fromCurrency) { fromCurrency = it }
                        DropdownSelector("To", currencyOptions, toCurrency) { toCurrency = it }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // TextField for entering amount
                    TextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Convert button
                    Button(
                        onClick = {
                            val converted = convertCurrency(amount.toDoubleOrNull() ?: 0.0, fromCurrency, toCurrency)
                            Toast.makeText(applicationContext, "Result: $converted $toCurrency", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Convert")
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selected)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun convertCurrency(amount: Double, from: String, to: String): Double {
    val ratesToIDR = mapOf(
        "IDR" to 1.0,
        "USD" to 16800.0,
        "MYR" to 3800.0,
        "EUR" to 19000.0
    )

    val fromRate = ratesToIDR[from] ?: 1.0
    val toRate = ratesToIDR[to] ?: 1.0

    val amountInIDR = amount * fromRate
    return (amountInIDR / toRate * 100).roundToInt() / 100.00 // rounding to 2 decimal places
}
