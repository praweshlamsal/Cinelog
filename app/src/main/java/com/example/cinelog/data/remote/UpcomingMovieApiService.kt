package com.example.cinelog.data.remote

import com.example.cinelog.model.UpcomingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UpcomingMovieApiService {
    @GET("movie/upcoming?language=en-US&page=1")
    suspend fun getUpcomingMovies(
        @Header("Authorization") auth: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNWQwOGI4ZTY2ZmM3ZmVmYTExZmI5MmU0YTAwNTg5MiIsIm5iZiI6MTc0MjQ5NzIzNC42ODk5OTk4LCJzdWIiOiI2N2RjNjVkMjg0M2E5NTAzMzY2YjljZjkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.jU3RwnJl89ue-riI_luDiIFNcMQLXLz-6deXjImiwew"
    ): Response<UpcomingMovieResponse>
}