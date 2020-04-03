package org.coepi.testbackend

/**
 * This test is used to compare query efficiency for getting a day worth of coepi report data
 * when using S3 and DynamoDB as back-end. Before running the test, do the following steps:
 *
 * 1. Update the bucket name to the test bucket in org.coepi.testbackend.Utils.kt
 * 2. Create AWS creds to have read/write access to the bucket and update your local configs using
 *    'aws configure' with the credentials and region of the bucket
 * 3. Use PopulateBucket.kt and PoupulateDynamo.kt to fill the datastores with fake test data
 * 4. Run this test
 */
fun main(args: Array<String>) {
    var start = System.currentTimeMillis()
    val reports = DynamoTest().queryReportsByDay("2020-04-01")
    var timeTaken = System.currentTimeMillis() - start
    println("Reports from DynamoDB: ${reports.size}")
    println("Amount of time taken to query a single day worth of data in ms: $timeTaken")

    start = System.currentTimeMillis()
    val s3Reports = S3Test(Utils.BUCKET_NAME).getItems("2020-04-01")
    timeTaken = System.currentTimeMillis() - start
    println("Reports from S3: ${s3Reports.size}")
    println("Amount of time taken to query a single day worth of data in ms: $timeTaken")
}