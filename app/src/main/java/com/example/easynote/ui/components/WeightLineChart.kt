package com.example.easynote.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

@Composable
fun LineChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {

                val entries = listOf(
                    Entry(0f, 65f),
                    Entry(1f, 66.5f),
                    Entry(2f, 66f),
                    Entry(3f, 64.8f),
                    Entry(4f, 65.3f)
                )

                val dataSet = LineDataSet(entries, "Peso").apply {
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    color = Color.Blue.hashCode()
                    lineWidth = 3f

                    setDrawCircles(true)
                    setCircleColor(Color.Blue.hashCode())
                    circleRadius = 4f

                    setDrawFilled(true)
                    fillColor = Color.Blue.hashCode()
                    fillAlpha = 60

                    setDrawValues(false)
                }

                this.data = LineData(dataSet)
                this.description.isEnabled = false
                this.axisRight.isEnabled = false
                this.xAxis.position = XAxis.XAxisPosition.BOTTOM

                this.animateX(750)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}
//@Preview
@Composable
fun pre() {
    val entries = mapOf(
        "Comida" to 250f,
        "Transporte" to 90f,
        "Compras" to 120f,
        "Otros" to 40f
    )
    PieChart(entries)
}

@Composable
fun PieChart(data: Map<String, Float>) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { context ->
            PieChart(context).apply {

                val entries = data.map { PieEntry(it.value, it.key) }

                val dataSet = PieDataSet(entries, "").apply {
                    colors = listOf(
                        Color(0xFFEF5350).hashCode(),
                        Color(0xFFAB47BC).hashCode(),
                        Color(0xFF42A5F5).hashCode(),
                        Color(0xFF26A69A).hashCode(),
                        Color(0xFFFFCA28).hashCode()
                    )
                    valueTextSize = 12f
                    sliceSpace = 2f
                }

                this.data = PieData(dataSet)

                setUsePercentValues(true)
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f
                setCenterTextSize(16f)

                animateY(1000)
                description.isEnabled = false
            }
        }
    )
}
