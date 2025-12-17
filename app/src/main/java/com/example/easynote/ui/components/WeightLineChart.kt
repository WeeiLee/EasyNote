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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LineChart(entries: Map<LocalDate, Float>) {
    AndroidView(
        factory = { context ->

            val sorted = entries.toSortedMap().values.toList()

            val chartEntries = sorted.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }

            LineChart(context).apply {

                val dataSet = LineDataSet(chartEntries, "Peso (kg)").apply {
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    color = android.graphics.Color.BLUE
                    lineWidth = 3f

                    setDrawCircles(true)
                    circleRadius = 4f
                    setDrawValues(false)

                    setDrawFilled(true)
                    fillAlpha = 40
                    fillColor = android.graphics.Color.BLUE
                }

                data = LineData(dataSet)

                description.isEnabled = false
                axisRight.isEnabled = false

                xAxis.apply {
                    setDrawLabels(false)
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                }

                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)

                animateX(600)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}



@Composable
fun PieChart(
    data: Map<String, Float>,
    modifier: Modifier
    ) {

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
