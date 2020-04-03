package org.coepi.testbackend

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import java.util.*


class DynamoTest {
    private val dynamoMapper: DynamoDBMapper

    init {
        val ddbClient = AmazonDynamoDBClientBuilder.standard().build()
        this.dynamoMapper = DynamoDBMapper(ddbClient)
    }

    fun populateDatabase(numCasesPerDay: Int, numDays: Int) {
        val reports = Utils.generateFakeReports(numCasesPerDay, numDays)
        reports.forEach { reportItem ->
            println("Adding report item to DDB: $reportItem")
            dynamoMapper.save(reportItem)
        }
    }

    fun queryReportsByDay(day: String): List<ReportItem> {
        val queryExpression = DynamoDBQueryExpression<ReportItem>()
        queryExpression.keyConditionExpression = "did = :val1"

        val attributeValueMap = HashMap<String, AttributeValue>()
        attributeValueMap[":val1"] = AttributeValue().withS(day)
        queryExpression.expressionAttributeValues = attributeValueMap

        val outputList = mutableListOf<ReportItem>()
        var lastEvalKey: Map<String, AttributeValue>? = null

        do {
            queryExpression.exclusiveStartKey = lastEvalKey
            val pageOutput = dynamoMapper.queryPage(ReportItem::class.java, queryExpression)
            outputList.addAll(pageOutput.results)
            lastEvalKey = pageOutput.lastEvaluatedKey
        } while (lastEvalKey != null)

        return outputList
    }
}