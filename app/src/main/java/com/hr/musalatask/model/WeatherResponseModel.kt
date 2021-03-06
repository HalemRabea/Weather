package com.hr.musalatask.model


import com.squareup.moshi.Json

data class WeatherResponseModel(
    @Json(name = "base")
    val base: String = "",
    @Json(name = "clouds")
    val clouds: Clouds = Clouds(),
    @Json(name = "cod")
    val cod: Int = 0,
    @Json(name = "coord")
    val coord: Coord = Coord(),
    @Json(name = "dt")
    val dt: Int = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "main")
    val main: Main = Main(),
    @Json(name = "name")
    val name: String = "",
    @Json(name = "sys")
    val sys: Sys = Sys(),
    @Json(name = "timezone")
    val timezone: Int = 0,
    @Json(name = "visibility")
    val visibility: Int = 0,
    @Json(name = "weather")
    val weather: List<Weather> = listOf(),
    @Json(name = "wind")
    val wind: Wind = Wind()
) {
    data class Clouds(
        @Json(name = "all")
        val all: Int = 0
    )

    data class Coord(
        @Json(name = "lat")
        val lat: Double = 0.0,
        @Json(name = "lon")
        val lon: Double = 0.0
    )

    data class Main(
        @Json(name = "feels_like")
        val feelsLike: Double = 0.0,
        @Json(name = "humidity")
        val humidity: Int = 0,
        @Json(name = "pressure")
        val pressure: Int = 0,
        @Json(name = "temp")
        val temp: Double = 0.0,
        @Json(name = "temp_max")
        val tempMax: Double = 0.0,
        @Json(name = "temp_min")
        val tempMin: Double = 0.0
    )

    data class Sys(
        @Json(name = "country")
        val country: String = "",
        @Json(name = "id")
        val id: Int = 0,
        @Json(name = "sunrise")
        val sunrise: Int = 0,
        @Json(name = "sunset")
        val sunset: Int = 0,
        @Json(name = "type")
        val type: Int = 0
    )

    data class Weather(
        @Json(name = "description")
        val description: String = "",
        @Json(name = "icon")
        val icon: String = "",
        @Json(name = "id")
        val id: Int = 0,
        @Json(name = "main")
        val main: String = ""
    )

    data class Wind(
        @Json(name = "deg")
        val deg: Int = 0,
        @Json(name = "speed")
        val speed: Double = 0.0
    )
}