package org.sharad.velvetinvestment.utils.networking

const val BASE_URL= "https://docker-velvet-backend-357888765640.asia-south1.run.app/api/v1"

const val PROD_URL="https://prod-velvet-357888765640.asia-south1.run.app/api/v1"

fun getUrl(endPoint:String)= "$PROD_URL$endPoint"