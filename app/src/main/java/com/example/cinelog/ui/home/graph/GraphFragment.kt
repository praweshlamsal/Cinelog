package com.example.cinelog.ui.home.graph

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.model.BarChartData
import com.example.cinelog.model.LineChartData
import com.example.cinelog.model.PieChartData
import com.example.cinelog.util.Constant
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.FirebaseFirestore

class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var movieViewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val transparentColor = ResourcesCompat.getColor(resources, android.R.color.transparent, requireContext().theme)
        pieChart.setBackgroundColor(transparentColor)
        barChart.setBackgroundColor(transparentColor)
        lineChart.setBackgroundColor(transparentColor)

        movieViewModel.pieChartDataList.observe(viewLifecycleOwner, Observer {
            setupPieChart(pieChart, it)
        })

        movieViewModel.barChartDataList.observe(viewLifecycleOwner, Observer {
            setupBarChart(barChart, it)
        })

        movieViewModel.lineChartDataList.observe(viewLifecycleOwner, Observer {
            setupLineChart(lineChart, it)
        })

        movieViewModel.fetchPieChartData(Constant.GRAPH_ID)
        movieViewModel.fetchBarChartData(Constant.GRAPH_ID)
        movieViewModel.fetchLineChartData(Constant.GRAPH_ID)
    }

    private fun setupPieChart(pieChart: PieChart, data: List<PieChartData>) {
        val entries = data.map { PieEntry(it.percentage, it.category) }

        val dataSet = PieDataSet(entries, getString(R.string.chart_genre_popularity)).apply {
            colors = listOf(
                ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_blue_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_green_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_orange_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_purple, requireContext().theme)
            )
            valueTextColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        }

        pieChart.data = PieData(dataSet)
        pieChart.description.text = getString(R.string.chart_genre_description)
        pieChart.description.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        pieChart.animateY(1000)
        pieChart.setEntryLabelColor(ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme))
        pieChart.legend.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
    }

    private fun setupBarChart(barChart: BarChart, data: List<BarChartData>) {
        val entries = data.mapIndexed { index, barData -> BarEntry(index.toFloat(), barData.watchHours) }
        val labels = data.map { it.movieName }

        val dataSet = BarDataSet(entries, getString(R.string.chart_top_movies)).apply {
            colors = listOf(
                ResourcesCompat.getColor(resources, android.R.color.holo_blue_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_blue_light, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_orange_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_green_dark, requireContext().theme),
                ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, requireContext().theme)
            )
            valueTextColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        }

        barChart.data = BarData(dataSet)
        barChart.description.text = getString(R.string.chart_bar_description)
        barChart.description.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        barChart.setFitBars(true)
        barChart.animateY(1000)

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
            granularity = 1f
            setDrawGridLines(false)
        }
        barChart.axisLeft.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        barChart.axisRight.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        barChart.legend.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
    }

    private fun setupLineChart(lineChart: LineChart, data: List<LineChartData>) {
        val entries = data.mapIndexed { index, lineData -> Entry(index.toFloat(), lineData.watchHours) }
        val labels = data.map { it.day }

        val dataSet = LineDataSet(entries, getString(R.string.chart_daily_watch)).apply {
            color = ResourcesCompat.getColor(resources, android.R.color.holo_blue_dark, requireContext().theme)
            valueTextColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
            setDrawCircles(true)
            setCircleColor(ResourcesCompat.getColor(resources, android.R.color.holo_red_dark, requireContext().theme))
        }

        lineChart.data = LineData(dataSet)
        lineChart.description.text = getString(R.string.chart_line_description)
        lineChart.description.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        lineChart.animateX(1000)

        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
            granularity = 1f
            setDrawGridLines(false)
        }
        lineChart.axisLeft.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        lineChart.axisRight.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
        lineChart.legend.textColor = ResourcesCompat.getColor(resources, android.R.color.black, requireContext().theme)
    }
}
