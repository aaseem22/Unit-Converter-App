package com.endurancetech.unitconverterapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.endurancetech.unitconverterapp.ui.theme.CardBackground
import com.endurancetech.unitconverterapp.ui.theme.DeepCoral
import com.endurancetech.unitconverterapp.ui.theme.SoftCoral
import com.endurancetech.unitconverterapp.ui.theme.TextDark
import com.endurancetech.unitconverterapp.ui.theme.TextMedium
import com.endurancetech.unitconverterapp.ui.theme.WarmCream
import com.endurancetech.unitconverterapp.ui.theme.WarmWhite


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmCream
                ) {
                    UnitConverterScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun UnitConverterScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isConverting by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isConverting) 360f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutCubic),
        finishedListener = { isConverting = false }
    )
    val lastConversion by viewModel.lastConversion.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        WarmCream,
                        WarmWhite,
                        WarmCream.copy(alpha = 0.7f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .shadow(8.dp, CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(SoftCoral, DeepCoral)
                            ),
                            CircleShape
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.swap),
                        contentDescription = "Unit Converter",
                        modifier = Modifier.size(40.dp),
                        tint = WarmWhite
                    )
                }

                Text(
                    text = "Unit Converter",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextDark
                )

                Text(
                    text = "Convert between different units with precision and style",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.inputValue,
                        onValueChange = { viewModel.onInputValueChange(it) },
                        label = {
                            Text(
                                "Enter Value",
                                color = TextMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Input",
                                tint = SoftCoral
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftCoral,
                            focusedLabelColor = SoftCoral,
                            unfocusedBorderColor = TextMedium.copy(alpha = 0.5f),
                            unfocusedLabelColor = TextMedium,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark,
                            cursorColor = SoftCoral
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UnitDropdown(
                            units = viewModel.units,
                            selectedUnit = viewModel.fromUnit,
                            onUnitSelected = { viewModel.onFromUnitChange(it) },
                            modifier = Modifier.weight(1f),
                            label = "From"
                        )

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(4.dp, CircleShape)
                                .background(SoftCoral, CircleShape)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val temp = viewModel.fromUnit
                                    viewModel.onFromUnitChange(viewModel.toUnit)
                                    viewModel.onToUnitChange(temp)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.swap_2),
                                    contentDescription = "Swap units",
                                    tint = WarmWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        UnitDropdown(
                            units = viewModel.units,
                            selectedUnit = viewModel.toUnit,
                            onUnitSelected = { viewModel.onToUnitChange(it) },
                            modifier = Modifier.weight(1f),
                            label = "To"
                        )
                    }

                    Button(
                        onClick = {
                            isConverting = true
                            viewModel.convert()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(SoftCoral, DeepCoral)
                                    ),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.calculator),
                                    contentDescription = "Convert",
                                    modifier = Modifier
                                        .rotate(rotationAngle)
                                        .size(28.dp),
                                    tint = WarmWhite
                                )
                                Text(
                                    text = "Convert",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmWhite
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = viewModel.result.isNotEmpty(),
                enter = slideInVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftCoral.copy(alpha = 0.1f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                SoftCoral.copy(alpha = 0.3f),
                                RoundedCornerShape(20.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Result",
                                style = MaterialTheme.typography.titleLarge,
                                color = DeepCoral,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = viewModel.result,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 28.sp
                                ),
                                color = TextDark,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                lastConversion?.let { conversion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = WarmWhite
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Last conversion",
                                    tint = SoftCoral,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Recent Conversion",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = {
                                        copyToClipboard(context, conversion.getFormattedConversion())
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SoftCoral.copy(alpha = 0.15f),
                                        contentColor = DeepCoral
                                    ),
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.copy),
                                        contentDescription = "Copy",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Copy",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Text(
                                text = conversion.getFormattedConversion(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextMedium,
                                modifier = Modifier
                                    .background(
                                        WarmCream.copy(alpha = 0.5f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnitDropdown(
    units: List<String>,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = WarmWhite,
                    contentColor = TextDark
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            SoftCoral.copy(alpha = 0.7f),
                            DeepCoral.copy(alpha = 0.5f)
                        )
                    ),
                    width = 1.5.dp
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedUnit,
                        color = TextDark,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = SoftCoral
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        WarmWhite,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = unit,
                                color = if (unit == selectedUnit) DeepCoral else TextDark,
                                fontWeight = if (unit == selectedUnit) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        },
                        leadingIcon = if (unit == selectedUnit) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = DeepCoral
                                )
                            }
                        } else null,
                        modifier = Modifier.background(
                            if (unit == selectedUnit)
                                SoftCoral.copy(alpha = 0.1f)
                            else
                                Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun UnitConverterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("conversion", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard!!", Toast.LENGTH_SHORT).show()
}