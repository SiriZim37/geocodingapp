package com.tlt.georepo.manager.db

import com.tlt.georepo.model.entity.DocumentEntity
import com.tlt.georepo.modules.menu.job.Job
import kotlin.math.ln


object DocumentManager {

    private const val SENDJOB_DOC = "sendjob"
    private const val SENDJOB_DOC_STATE = "sendjobstate"

    private val databaseManager = DatabaseManager.getInstance()



    fun saveStateSendJobDocs(base64List: List<String> , lat : String , lng : String ) {
        databaseManager.deleteBy(DocumentEntity::class.java, "type", SENDJOB_DOC_STATE)

        val item = base64List.map {
            DocumentEntity( base64 = it,
                            type = SENDJOB_DOC_STATE ,
                            lat =  lat ,
                            lng =  lng
                        )
        }

        databaseManager.save(DocumentEntity::class.java, item)
    }

    fun saveSendJobDocs(base64List: List<String> , lat : String , lng : String ) {
        databaseManager.deleteBy(DocumentEntity::class.java, "type", SENDJOB_DOC)

        val items = base64List.map {
            DocumentEntity(base64 = it,
                type = SENDJOB_DOC ,
                lat =  lat ,
                lng =  lng
            )
        }

        databaseManager.save(DocumentEntity::class.java, items)
    }

    fun deleteSendJobDocsState() = databaseManager.deleteBy(DocumentEntity::class.java, "type", SENDJOB_DOC_STATE)

    fun getDocumentList() = databaseManager.findAllBy(DocumentEntity::class.java)
            ?.filter { it.type != SENDJOB_DOC_STATE }
            ?.map { it.base64 }

    fun getSendJobDocuments() = databaseManager.findAllBy(DocumentEntity::class.java)
            ?.filter { it.type == SENDJOB_DOC  }
            ?.toList()
            ?.map {   DocumentEntity(
                base64 =   it?.base64 ,
                lat = it?.lat ,
                lng =it?.lng
            )}

     fun getSendJobDocumentsState() = databaseManager.findAllBy(DocumentEntity::class.java)
            ?.filter { it.type == SENDJOB_DOC_STATE }
            ?.map { it.base64 }


}