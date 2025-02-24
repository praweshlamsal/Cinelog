package com.example.cinelog.ui.home.graph

import android.graphics.Color
import android.os.Bundle
import android.view.View
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
import com.example.cinelog.util.PushDataToFireBase
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

      /*  //Push all the Required Data to firebase
        PushDataToFireBase( FirebaseFirestore.getInstance())*/


        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)


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


    }

    private fun setupPieChart(pieChart: PieChart, data: List<PieChartData>) {
        val entries = data.map { PieEntry(it.percentage, it.category) }

        val dataSet = PieDataSet(entries, "Genre Popularity").apply {
            colors = listOf(
                0xFF9B5A5A.toInt(), // Matte Red
                0xFF6D7072.toInt(), // Matte Blue
                0xFF5E7350.toInt(), // Matte Green
                0xFFD1B77F.toInt(), // Matte Yellow
                0xFF946C81.toInt()  // Matte Magenta
            )
        }

        pieChart.data = PieData(dataSet)
        pieChart.description.text = "Movie Genre Popularity (%)"
        pieChart.animateY(1000)
        pieChart.setEntryLabelColor(Color.BLACK)
    }

    private fun setupBarChart(barChart: BarChart, data: List<BarChartData>) {
        val entries = data.mapIndexed { index, barData -> BarEntry(index.toFloat(), barData.watchHours) }
        val labels = data.map { it.movieName }

        val dataSet = BarDataSet(entries, "Top 5 Watched Movies").apply {
            colors = listOf(
                0xFF6D7072.toInt(), 0xFF7FA6B8.toInt(),
                0xFF8B8C89.toInt(), 0xFF5E7350.toInt(),
                0xFF9B5A5A.toInt()
            )
        }

        barChart.data = BarData(dataSet)
        barChart.description.text = "Total Watch Hours per Movie"
        barChart.setFitBars(true)
        barChart.animateY(1000)

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setDrawGridLines(false)
        }
    }

    private fun setupLineChart(lineChart: LineChart, data: List<LineChartData>) {
        val entries = data.mapIndexed { index, lineData -> Entry(index.toFloat(), lineData.watchHours) }
        val labels = data.map { it.day }

        val dataSet = LineDataSet(entries, "Daily Watch Time (Hours)").apply {
            color = 0xFF6D7072.toInt() // Matte Blue
            valueTextColor = Color.BLACK
            setDrawCircles(true)
            setCircleColor(Color.RED)
        }

        lineChart.data = LineData(dataSet)
        lineChart.description.text = "Daily Watch Hours Trend"
        lineChart.animateX(1000)

        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setDrawGridLines(false)
        }
    }



