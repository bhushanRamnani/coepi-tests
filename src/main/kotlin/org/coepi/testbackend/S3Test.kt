package org.coepi.testbackend

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ListObjectsV2Request
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils

class S3Test(bucketName: String) {
    private val amazonS3 = AmazonS3ClientBuilder.standard().build()
    private val bucketName: String = bucketName
    private val objectMapper = ObjectMapper()

    fun populateBucket(numCasesPerDay: Int, numDays: Int) {
        val reportList = Utils.generateFakeReports(numCasesPerDay, numDays)

        reportList.forEach { reportItem ->
            val content = objectMapper.writeValueAsString(reportItem)
            println("Writing item to S3: $content")
            amazonS3.putObject(bucketName, reportItem.did + "/" + reportItem.timestamp, content)
        }
    }

    fun getItems(did: String): List<ReportItem> {
        val objectSummaries = listKeys(did)
        val outputList = mutableListOf<ReportItem>()

        objectSummaries.forEach { obj ->
            val s3Object = amazonS3.getObject(bucketName, obj.key)

            val objectContent = IOUtils.toString(s3Object.objectContent, "UTF-8")
            val reportItem = objectMapper.readValue(objectContent, ReportItem::class.java)
            outputList.add(reportItem)
        }
        return outputList
    }

    private fun listKeys(did: String): List<S3ObjectSummary> {
        var continuationToken: String? = null
        var output = mutableListOf<S3ObjectSummary>()

        do {
            val request = ListObjectsV2Request()
            request.bucketName = bucketName
            request.prefix = did
            request.continuationToken = continuationToken

            val listObjectsResult = amazonS3.listObjectsV2(request)
            output.addAll(listObjectsResult.objectSummaries)
            continuationToken = listObjectsResult.nextContinuationToken
        } while (continuationToken != null)

        return output
    }
}