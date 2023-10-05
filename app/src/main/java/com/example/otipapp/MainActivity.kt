package com.example.otipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.otipapp.components.InputField
import com.example.otipapp.ui.theme.OtipAppTheme
import com.example.otipapp.widgets.RoundIconButton
import kotlin.math.absoluteValue
import androidx.compose.ui.Alignment as Alignment1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myapp {
                //TopHeader()

            }
        }
    }
}


@Composable
fun Myapp(content: @Composable () -> Unit) {

    OtipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            //modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment1.CenterHorizontally
            ) {
//                TopHeader()
                MainContent()
            }
        }
    }
}


//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp, 10.dp)
            .offset(0.dp, 5.dp)
            .clip(shape = RoundedCornerShape(10.dp)),
        color = Color(0xFFE3D7F7),
        shadowElevation = 3.dp
    ) {
        Column(
            horizontalAlignment = Alignment1.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal
            )
            //total= total/PersonCount
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    BillForm() { billAmt ->
        Log.d("AMT", "MainContent: $billAmt ")
    }
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OtipAppTheme {
        Myapp {
            Text(text = "Hello Boys!")
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier, onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    var PersonCount by remember { mutableStateOf(1) }
    var SliderPositionState by remember { mutableStateOf(0f) }
    val tipPercentage = (SliderPositionState * 100).toInt()
    var totalPerPerson by remember {
        mutableStateOf("")
    }
    var TipAmountState by remember { mutableStateOf(0.0) }
    var totalPerPersonState by remember {
        mutableStateOf(0.0)
    }
    TopHeader(totalPerPersonState)
    Surface(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)

    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment1.Start
        ) {
            InputField(valueState = totalBillState,
                labelId = "Enter Total Amount",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions() {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })
            if (validState) {
                Row(
                    modifier = Modifier
                        .padding(3.dp)
                        .offset(0.dp, 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split", modifier = Modifier.align(
                            alignment = Alignment1.CenterVertically
                        )
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            if (PersonCount == 1) PersonCount = 1
                            else PersonCount--
                            totalPerPersonState =
                                calculatePerPerson(
                                    totalBillState.value.toDouble(),
                                    PersonCount,
                                    tipPercentage
                                )
                        })
                        Text(
                            text = "$PersonCount",
                            Modifier
                                .align(Alignment1.CenterVertically)
                                .padding(10.dp)
                        )
                        RoundIconButton(imageVector = Icons.Default.Add,
                            onClick = {
                                PersonCount++
                                totalPerPersonState =
                                    calculatePerPerson(
                                        totalBillState.value.toDouble(),
                                        PersonCount,
                                        tipPercentage
                                    )
                            })
                    }
                }

                Row(
                    modifier = Modifier
                        .offset(0.dp, 35.dp)
                        .padding(3.dp)
                ) {
                    Text(text = "Tip")
                    Spacer(modifier = Modifier.size(170.dp, 20.dp))
                    Text(text = "$ $TipAmountState")
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                        //.offset(0.dp, 150.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //val test = totalBillState.value.toDouble()*TipPercentage.toDouble()
                    Text(
                        text = "Tip Percentage: $tipPercentage%",
                        Modifier
                            .padding(0.dp, 20.dp)
                            .offset(0.dp, 20.dp)
                    )

                    Slider(

                        value = SliderPositionState,
                        onValueChange = { newVal ->
                            SliderPositionState = newVal
                            TipAmountState =
                                calculateTipAmt(tipPercentage, totalBill = totalBillState.value.toDouble())
                            totalPerPersonState =
                                calculatePerPerson(
                                    totalBillState.value.toDouble(),
                                    PersonCount,
                                    tipPercentage
                                )
                        }
                    )
                }
            } else {
                Box {

                }
            }
        }
    }
}



fun calculateTipAmt(tipPercentage: Int, totalBill: Double): Double {
    return if (totalBill > 1 && totalBill.toString().trim()
            .isNotEmpty()
    ) (totalBill * tipPercentage) / 100
    else 0.0
}

fun calculatePerPerson(
    totalBill: Double,
    PersonCount: Int,
    tipPercentage: Int
): Double {
    val bill = calculateTipAmt(tipPercentage, totalBill) + totalBill
    return (bill / PersonCount)
}



