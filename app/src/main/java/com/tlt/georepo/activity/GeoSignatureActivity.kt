package com.tlt.georepo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import com.tlt.georepo.R
import android.widget.Toast
import kotlinx.android.synthetic.main.geo_signature.*

class GeoSignatureActivity  : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_signature)

        btn_sign_submit.setOnClickListener {
            if (signature_view.isBitmapEmpty) {
                Toast.makeText(this, "please make any signature", Toast.LENGTH_SHORT).show();
            } else {
                val bitmap = signature_view.signatureBitmap
                img_signature.setImageBitmap(bitmap)
            }
        }


        btn_sign_clear.setOnClickListener {
            signature_view.clearCanvas()
        }
    }

        override fun onBackPressedSupport() {
            try {
                GeoLabTestActivity.open(this)
            }catch (e : Exception){
                e.message
            }
        }

        companion object {
            private val STROKE_WIDTH = 5f
            private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2

            fun open(context: Context) {
                val intent = Intent(context, GeoSignatureActivity::class.java)
                context.startActivity(intent)
            }
        }
}
