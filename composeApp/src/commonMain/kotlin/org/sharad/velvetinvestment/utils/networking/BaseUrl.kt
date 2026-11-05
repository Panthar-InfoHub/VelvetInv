package org.sharad.velvetinvestment.utils.networking

const val DEV_URL= "https://docker-velvet-backend-357888765640.asia-south1.run.app/api/v21"

const val PROD_URL="https://prod-velvet-357888765640.asia-south1.run.app/api/v2"

const val FD_URL="https://vlv-jn-001-34918043640.asia-south1.run.app/api/v1"

fun getUrl(endPoint:String)= "$PROD_URL$endPoint"

fun getFDUrl(endPoint:String)= "$FD_URL$endPoint"
