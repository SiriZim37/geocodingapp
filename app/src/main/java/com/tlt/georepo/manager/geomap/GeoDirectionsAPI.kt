package com.tlt.georepo.manager.geomap

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class GeoDirectionsAPI(private val mContext: Context) {
    private var map: GoogleMap? = null
    private var parser: DirectionsJSONParser? = null
    private var lineColor = Color.parseColor("#E57D27")
    private var mListener: DirectionsJSONParser.OnDirectionAPIListener? = null
    var routeDistance: Int = 0


    fun drawDirection(
        _map: GoogleMap,
        origin: LatLng,
        dest: LatLng,
        color: Int,
        listener: DirectionsJSONParser.OnDirectionAPIListener
    ) {
        map = _map
        lineColor = color
        mListener = listener
        val url = getDirectionsUrl(origin, dest)
        DownloadTask().execute(url)
    }


    private inner class DownloadTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            var data = ""

            try {
                data = downloadUrl(params[0])
                Log.i("geocoding", data)
            } catch (e: Exception) {

            }

            return data
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)

            ParserTask().execute(s)
        }
    }


    private inner class ParserTask : AsyncTask<String, Void, List<List<HashMap<String, String>>>>() {


        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null

            try {
                jObject = JSONObject(jsonData[0])
                parser = DirectionsJSONParser()

                // Starts parsing data
                routes = parser!!.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            super.onPostExecute(result)

            var points: ArrayList<LatLng>? = null
            var lineOptions: PolylineOptions? = null
            val markerOptions = MarkerOptions()


            for (i in result.indices) {

                points = ArrayList()
                lineOptions = PolylineOptions()
                lineOptions.clickable(true)

                // Fetching i-th route
                val path = result[i]

                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                    val position = LatLng(lat, lng)

                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(15f)
                lineOptions.color(lineColor)


                // Drawing polyline in the Google Map for the i-th route
                try {
                    map!!.addPolyline(lineOptions)
                    routeDistance = parser!!.distance
                    mListener!!.onFinished(this@GeoDirectionsAPI, points)
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Can't get route direction", Toast.LENGTH_SHORT).show()
                }

            }


        }


    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Sensor enabled
        val sensor = "sensor=false"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
        Log.i("geocoding", "webservice request url: $url")

        return url
    }

    /** A method to download json data from url  */
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            val br : BufferedReader = BufferedReader(InputStreamReader(iStream!!))

            val sb : StringBuffer = StringBuffer()

            var line : String = ""
            while (line == br.readLine())  {
                sb.append(line)
            }

//            String line = "";
//            while( ( line = br.readLine())  != null){
//                sb.append(line);
//            }

            data = sb.toString()

            br.close()

        } catch (e: Exception) {
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    class DirectionsJSONParser {

        var distance = 0
        var strDistance = ""
        var strDuration = ""

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude  */
        fun parse(jObject: JSONObject): List<List<HashMap<String, String>>> {

            val routes = ArrayList<List<HashMap<String, String>>>()
            var jRoutes: JSONArray? = null
            var jLegs: JSONArray? = null
            var jSteps: JSONArray? = null

            try {

                jRoutes = jObject.getJSONArray("routes")

                /** Traversing all routes  */
                for (i in 0 until jRoutes!!.length()) {
                    jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")

                    val path = ArrayList<HashMap<String, String>>()

                    val data = jLegs!!.getJSONObject(0)
                    distance = Integer.valueOf(data.getJSONObject("distance").getString("value"))
                    strDistance = data.getJSONObject("distance").getString("text")
                    strDuration = data.getJSONObject("duration").getString("text")

                    /** Traversing all legs  */
                    for (j in 0 until jLegs.length()) {
                        jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")

                        /** Traversing all steps  */
                        for (k in 0 until jSteps!!.length()) {
                            var polyline = ""
                            polyline =
                                ((jSteps.get(k) as JSONObject).get("polyline") as JSONObject).get("points") as String
                            val list = decodePoly(polyline)

                            /** Traversing all points  */
                            for (l in list.indices) {
                                val hm = HashMap<String, String>()
                                hm["lat"] = java.lang.Double.toString(list[l].latitude)
                                hm["lng"] = java.lang.Double.toString(list[l].longitude)
                                path.add(hm)
                            }
                        }
                        routes.add(path)
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
            }


            return routes
        }


        /**
         * Method to decode polyline points
         * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
        private fun decodePoly(encoded: String): List<LatLng> {

            val poly = ArrayList<LatLng>()
            var index = 0
            val len = encoded.length
            var lat = 0
            var lng = 0

            while (index < len) {
                var b: Int
                var shift = 0
                var result = 0
                do {
                    b = encoded[index++].toInt() - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat

                shift = 0
                result = 0
                do {
                    b = encoded[index++].toInt() - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dlng

                val p = LatLng(
                    lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5
                )
                poly.add(p)
            }

            return poly
        }

        interface OnDirectionAPIListener {
            fun onFinished(api: GeoDirectionsAPI, points: ArrayList<LatLng>)
        }
    }


}
