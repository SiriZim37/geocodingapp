package com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseFragment
import com.tlt.georepo.common.extension.callPhone
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.loadImageByBase64
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandGuarantorAdapter
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandProfileItem
import com.tlt.georepo.modules.menu.job.onhand.topmenu.SubDetailMainActivity
import com.tlt.georepo.util.ExternalAppUtils
import kotlinx.android.synthetic.main.fragment_collect_main.*
import java.lang.Exception

class SubMainFragment : BaseFragment(), OnMapReadyCallback {
    protected var mGoogleMap: GoogleMap? = null
    var MAP_lATITUDE : Double? = null
    var MAP_LONGTIUDE: Double? = null
   var ProfileListData : List<OnHandProfileItem>? = null
    private val SCROLL_POSITION_STATE = "scroll_position"

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SubMainViewModel::class.java)
    }

    override fun onSupportInvisible() {

        super.onSupportInvisible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sub_onhand_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            nestedscrollview.scrollY = savedInstanceState.getInt(SCROLL_POSITION_STATE, 0)
        }
    }

    private fun initViewModel() {

    }

    private fun initInstances() {
        try{
            recycler_view.layoutManager = LinearLayoutManager(context)

            ProfileListData = SubDetailMainActivity.jobOnHandProfileListData
            if (ProfileListData!!.count()>0){
                var items  = ProfileListData!![0]
                if (items.iDPIC.isEmpty()) {
                    cust_profile.setImageDrawable(ContextCompat.getDrawable(this!!.context!!, R.drawable.ic_person_black_24dp))
                } else {
                    cust_profile.loadImageByBase64(items.iDPIC)
                }
                txt_cust_name.text = items.cUSTOMERNAME
                txt_cust_type.text = items.rECORDTYPE
                txt_id_card.text = items.iDCARD
                txt_real_address.text = items.rEALADDRESS
                txt_mobile_phone.text = items.tELMOBILE
                txt_mobile_phone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                txt_mobile_home.text = items.tELHOME
                txt_mobile_home.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)


                if(items.gUARANTOR!!.count() > 0) {
                    if (items.gUARANTOR!!.isNotEmpty()) {
                        recycler_view.visible()
                        recycler_view.adapter = OnHandGuarantorAdapter(onGuarantorListener)
                        val adapter = recycler_view.adapter as OnHandGuarantorAdapter
                        adapter.updateItems(items.gUARANTOR!!)
                    } else {
                        recycler_view.gone()
                    }

                }

                txt_mobile_phone.setOnClickListener {
                    if(!"".equals(items.tELMOBILE)) {
                        context!!.callPhone(items.tELMOBILE.toString())
                    }
                }

                txt_mobile_home.setOnClickListener {
                    if(!"".equals(items.tELHOME)) {
                        context!!.callPhone(items.tELHOME.toString())
                    }
                }

                try{
                    map.onCreate(null)
                    map.getMapAsync(this)
                    MAP_LONGTIUDE = items.lONGITUDE!!.toDouble()
                    MAP_lATITUDE = items.lATITUDE!!.toDouble()
                    // If the map is ready, update its content.
                    if (mGoogleMap != null) {
                        updateMapContents(items.lATITUDE!!.toDouble() , items.lONGITUDE!!.toDouble())
                    }

                }catch (ex : Exception){
                    Log.e("Map MainFragment ex" , ex.stackTrace.toString())
                }

//                map.setOnClickListener {
//                    val lat = items.lATITUDE!!.toDouble()
//                    val lng = items.lONGITUDE!!.toDouble()
//                    ExternalAppUtils.openGoogleDirection(context, lat, lng)
//                }
            }

        }catch (e : Exception){
            Log.e("MainFragment e" , e.stackTrace.toString())
        }

    }
    private val onGuarantorListener = object : OnHandGuarantorAdapter.Listener {
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map

        map.setOnMapClickListener(GoogleMap.OnMapClickListener {
            ExternalAppUtils.openGoogleDirection(context, MAP_lATITUDE!!, MAP_LONGTIUDE!!)}
        )


        MapsInitializer.initialize(context!!)
        map.uiSettings.isMapToolbarEnabled = false
        val lat = this!!.ProfileListData!![0].lATITUDE!!.toDouble()
        val lng = this!!.ProfileListData!![0].lONGITUDE!!.toDouble()
        map.addMarker(MarkerOptions().position(LatLng(lat, lng)))
        val coords = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(coords))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 10f)
        map.moveCamera(cameraUpdate)
    }



    protected fun updateMapContents(lat : Double , lng : Double) {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap!!.clear()
        var  latlng = LatLng(lat, lng)
        // Update the mapView feature data and camera position.
        mGoogleMap!!.addMarker(MarkerOptions().position(latlng))

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng ,10f)
        mGoogleMap!!.moveCamera(cameraUpdate)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance() = SubMainFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}
