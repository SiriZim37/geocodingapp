package com.tlt.georepo.manager.db

import com.tlt.georepo.model.entity.masterdata.*

class MasterDataManager {

    fun isSameAsVersion(version: String = ""): Boolean {
        val currentVersion = DatabaseManager.getInstance().getMasterDataVersion()
        return currentVersion == version
    }

    fun updateVersion(version: String = "") {
        DatabaseManager.getInstance().saveMasterDataVersion(version)
    }

    fun getCurrentVersion() = DatabaseManager.getInstance().getMasterDataVersion()



    fun getCompanyList() = DatabaseManager.getInstance().findAllBy(Company::class.java)

    fun getSubMitList() = DatabaseManager.getInstance().findAllBy(SubmitJob::class.java)

    fun getRegectList() = DatabaseManager.getInstance().findAllBy(RejectJob::class.java)

    fun getPromiseLimitDate() = DatabaseManager.getInstance().findAllBy(PromiseLimit::class.java)

    fun getHoldLimitDate() = DatabaseManager.getInstance().findAllBy(HoldLimit::class.java)

    /*fun getDealerNameByCode(code: String): List<DealerInfo> {
        return DatabaseManager.getInstance().findAllBy(DealerInfo::class.java)
                ?.filter {
                    it.dealerCode == code
                } ?: getDealerNameByName("gg")
    }

    fun getDealerNameByName(name: String): List<DealerInfo> {

    }*/

    companion object {
        private val masterDataManager = MasterDataManager()

        fun getInstance() = masterDataManager
    }
}