package com.tlt.georepo.manager.geomap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.location.Location
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon

import java.util.ArrayList

object GeoMapUtil {
    val LOG_TAG = "geocoding"
    private val EARTH_RADIUS = 6371000.0// meters


    fun addText(
        context: Context?, map: GoogleMap?,
        polygon: Polygon, text: String?, padding: Int,
        fontSize: Int
    ): Marker? {
        var marker: Marker? = null

        val location = getPolygonCenterPoint(polygon.points)

        if (context == null || map == null || location == null || text == null
            || fontSize <= 0
        ) {
            return marker
        }

        val textView = TextView(context)
        textView.text = text
        textView.textSize = fontSize.toFloat()

        val paintText = textView.paint

        val boundsText = Rect()
        paintText.getTextBounds(text, 0, textView.length(), boundsText)
        paintText.textAlign = Paint.Align.CENTER

        val conf = Bitmap.Config.ARGB_8888
        val bmpText = Bitmap.createBitmap(boundsText.width() + 2 * padding, boundsText.height() + 2 * padding, conf)

        val canvasText = Canvas(bmpText)
        paintText.color = Color.BLACK




        canvasText.drawText(
            text, (canvasText.width / 2).toFloat(),
            (canvasText.height - padding - boundsText.bottom).toFloat(), paintText
        )

        val markerOptions = MarkerOptions()
            .position(location)

            .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
            .anchor(0.5f, 1f)

        marker = map.addMarker(markerOptions)


        return marker
    }


    private fun getPolygonCenterPoint(polygonPointsList: List<LatLng>): LatLng {
        var centerLatLng: LatLng? = null
        val builder = LatLngBounds.Builder()
        for (i in polygonPointsList.indices) {
            builder.include(polygonPointsList[i])
        }
        val bounds = builder.build()
        centerLatLng = bounds.center

        return centerLatLng
    }


    fun requestOpenGPS(context: Context) {
        val provider = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)

        if (!provider.contains("gps")) { //if gps is disabled
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            Toast.makeText(context, "Please turn on Location Tracking", Toast.LENGTH_SHORT).show()
        }
    }

    fun calculatePolygonArea(latLngs: List<LatLng>): Double {

        val locations = ArrayList<Location>()
        for (loc in latLngs) {
            val aLoc = Location("")
            aLoc.latitude = loc.latitude
            aLoc.longitude = loc.longitude
            locations.add(aLoc)
        }

        return calculateAreaOfGPSPolygonOnSphereInSquareMeters(locations, EARTH_RADIUS)
    }


    private fun calculateAreaOfGPSPolygonOnSphereInSquareMeters(locations: List<Location>, radius: Double): Double {
        if (locations.size < 3) {
            return 0.0
        }

        val diameter = radius * 2
        val circumference = diameter * Math.PI
        val listY = ArrayList<Double>()
        val listX = ArrayList<Double>()
        val listArea = ArrayList<Double>()
        // calculate segment x and y in degrees for each point
        val latitudeRef = locations[0].latitude
        val longitudeRef = locations[0].longitude
        for (i in 1 until locations.size) {
            val latitude = locations[i].latitude
            val longitude = locations[i].longitude
            listY.add(calculateYSegment(latitudeRef, latitude, circumference))
            Log.d(LOG_TAG, String.format("Y %s: %s", listY.size - 1, listY[listY.size - 1]))
            listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference))
            Log.d(LOG_TAG, String.format("X %s: %s", listX.size - 1, listX[listX.size - 1]))
        }

        // calculate areas for each triangle segment
        for (i in 1 until listX.size) {
            val x1 = listX[i - 1]
            val y1 = listY[i - 1]
            val x2 = listX[i]
            val y2 = listY[i]
            listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2))
            Log.d(LOG_TAG, String.format("area %s: %s", listArea.size - 1, listArea[listArea.size - 1]))
        }

        // sum areas of all triangle segments
        var areasSum = 0.0
        for (area in listArea) {
            areasSum = areasSum + area
        }

        // get abolute value of area, it can't be negative
        return Math.abs(areasSum)// Math.sqrt(areasSum * areasSum);
    }

    private fun calculateAreaInSquareMeters(x1: Double, x2: Double, y1: Double, y2: Double): Double {
        return (y1 * x2 - x1 * y2) / 2
    }

    private fun calculateYSegment(latitudeRef: Double, latitude: Double, circumference: Double): Double {
        return (latitude - latitudeRef) * circumference / 360.0
    }

    private fun calculateXSegment(
        longitudeRef: Double, longitude: Double, latitude: Double,
        circumference: Double
    ): Double {
        return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0
    }
}
