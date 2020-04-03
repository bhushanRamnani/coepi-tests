package org.coepi.testbackend

fun main(args: Array<String>) {
    S3Test(Utils.BUCKET_NAME).populateBucket(5000, 15)
}