package org.coepi.testbackend

import org.apache.commons.lang3.RandomStringUtils
import java.sql.Date
import java.time.Instant
import java.util.*

object Utils {
    val BUCKET_NAME: String = "ramnanib-s3-bucket-pdx"

    fun generateFakeReports(numCasesPerDay: Int, numDays: Int): List<ReportItem> {
        val dateNow = Instant.now()
        var timestamp = dateNow.toEpochMilli()
        val durationMs = ((24 * 3600) / numCasesPerDay) * 1000
        val totalCases = numCasesPerDay * numDays

        return List(totalCases) {
            val reportContent = RandomStringUtils.randomAlphabetic(500)
            val cenKeys = HashSet<String>()
            cenKeys.add(UUID.randomUUID().toString())
            cenKeys.add(UUID.randomUUID().toString())
            cenKeys.add(UUID.randomUUID().toString())

            timestamp -= durationMs
            val dateKey = Date(timestamp).toString().substring(0,10)

            ReportItem(dateKey, timestamp, reportContent, cenKeys)
        }
    }
}