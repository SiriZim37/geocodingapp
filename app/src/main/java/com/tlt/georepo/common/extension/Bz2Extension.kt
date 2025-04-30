package com.tlt.georepo.common.extension

import org.apache.commons.compress.compressors.CompressorException
import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.io.*

@Throws(FileNotFoundException::class, CompressorException::class)
fun ByteArray.unzip(): String {
    val fin = ByteArrayInputStream(this)
    val bis = BufferedInputStream(fin)
    val input = CompressorStreamFactory().createCompressorInputStream(bis)
    val bufferedReader = BufferedReader(InputStreamReader(input) as Reader?)
    return  bufferedReader.readLines().first()// bufferedReader.readText()// bufferedReader.readLines() //
}

