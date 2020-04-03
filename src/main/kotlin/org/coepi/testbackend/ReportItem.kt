package org.coepi.testbackend

import com.amazonaws.services.dynamodbv2.datamodeling.*

@DynamoDBTable(tableName = "Reports")
data class ReportItem (
        @DynamoDBHashKey
        var did: String = "",

        @DynamoDBRangeKey
        var timestamp: Long = 0,

        @DynamoDBAttribute
        var reportContent: String = "",

        @DynamoDBAttribute
        var cenKeys: Set<String> = setOf()
)