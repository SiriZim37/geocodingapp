package com.tlt.georepo.modules.nonuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R

class SignUpActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pincode_layout)
        initInstances()
    }

    private fun initInstances() {

    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
}
