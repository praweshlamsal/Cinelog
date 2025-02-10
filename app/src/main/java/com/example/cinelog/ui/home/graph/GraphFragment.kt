package com.example.cinelog.ui.home.graph

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cinelog.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class GraphFragment : Fragment(R.layout.fragment_graph) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        setupPieChart(pieChart)
        setupBarChart(barChart)
        setupLineChart(lineChart)
    }


    private fun setupPieChart(pieChart: PieChart) {
        val entries = listOf(
            PieEntry(35f, "Action"),
            PieEntry(25f, "Comedy"),
            PieEntry(15f, "Drama"),
            PieEntry(10f, "Horror"),
            PieEntry(15f, "Sci-Fi")
        )

        val dataSet = PieDataSet(entries, "Genre Popularity")
        dataSet.colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA)

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.text = "Movie Genre Popularity (%)"
        pieChart.animateY(1000)
        pieChart.setEntryLabelColor(Color.BLACK)
    }


    private fun setupBarChart(barChart: BarChart) {
        val entries = listOf(
            BarEntry(0f, 120f),
            BarEntry(1f, 95f),
            BarEntry(2f, 85f),
            BarEntry(3f, 70f),
            BarEntry(4f, 60f)
        )

        val labels = listOf("Inception", "Avengers", "Interstellar", "Joker", "Parasite")
        val dataSet = BarDataSet(entries, "Top 5 Watched Movies")
        dataSet.colors = listOf(Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.RED)

        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.text = "Total Watch Hours per Movie"
        barChart.setFitBars(true)
        barChart.animateY(1000)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
    }


    private fun setupLineChart(lineChart: LineChart) {
        val entries = listOf(
            Entry(0f, 3.5f),
            Entry(1f, 4.0f),
            Entry(2f, 2.8f),
            Entry(3f, 5.2f),
            Entry(4f, 4.8f)
        )

        val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
        val dataSet = LineDataSet(entries, "Daily Watch Time (Hours)")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawCircles(true)
        dataSet.setCircleColor(Color.RED)

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.description.text = "Daily Watch Hours Trend"
        lineChart.animateX(1000)

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
    }
}