package org.coepi.testbackend

fun main(args: Array<String>) {
    DynamoTest().populateDatabase(5000, 15)
}