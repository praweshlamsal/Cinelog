package com.example.cinelog.util

import android.util.Log
import com.example.cinelog.model.BarChartData
import com.example.cinelog.model.LineChartData
import com.example.cinelog.model.PieChartData
import com.google.firebase.firestore.FirebaseFirestore

class PushDataToFireBase(private val db: FirebaseFirestore) {

    init {
        pushDataToFireStore()
    }

    private fun getPieChartData(): List<PieChartData> {
        return listOf(
            PieChartData(35f, "Action"),
            PieChartData(25f, "Comedy"),
            PieChartData(15f, "Drama"),
            PieChartData(10f, "Horror"),
            PieChartData(15f, "Sci-Fi")
        )
    }

    private fun getBarChartData(): List<BarChartData> {
        return listOf(
            BarChartData("Inception", 120f),
            BarChartData("Avengers", 95f),
            BarChartData("Interstellar", 85f),
            BarChartData("Joker", 70f),
            BarChartData("Parasite", 60f)
        )
    }

    private fun getLineChartData(): List<LineChartData> {
        return listOf(
            LineChartData("Mon", 3.5f),
            LineChartData("Tue", 4.0f),
            LineChartData("Wed", 2.8f),
            LineChartData("Thu", 5.2f),
            LineChartData("Fri", 4.8f)
        )
    }

    private fun pushDataToFireStore() {
        val graphId = Constant.GRAPH_ID

        // Push Pie Chart Data
        getPieChartData().forEach { data ->
            db.collection(Constant.GRAPH).document(graphId)
                .collection(Constant.PIE_CHART).document()
                .set(data)
                .addOnSuccessListener { Log.d(Constant.FIRE_STORE, "PieChart Data Added: $data") }
                .addOnFailureListener { e -> Log.e(Constant.FIRE_STORE, "Error adding PieChart data", e) }
        }

        // Push Bar Chart Data (Fixing Wrong Data)
        getBarChartData().forEach { data ->
            db.collection(Constant.GRAPH).document(graphId)
                .collection(Constant.BAR_CHART).document()
                .set(data)
                .addOnSuccessListener { Log.d(Constant.FIRE_STORE, "BarChart Data Added: $data") }
                .addOnFailureListener { e -> Log.e(Constant.FIRE_STORE, "Error adding BarChart data", e) }
        }

        // Push Line Chart Data
        getLineChartData().forEach { data ->
            db.collection(Constant.GRAPH).document(graphId)
                .collection(Constant.LINE_CHART).document()
                .set(data)
                .addOnSuccessListener { Log.d(Constant.FIRE_STORE, "LineChart Data Added: $data") }
                .addOnFailureListener { e -> Log.e(Constant.FIRE_STORE, "Error adding LineChart data", e) }
        }
    }
}
