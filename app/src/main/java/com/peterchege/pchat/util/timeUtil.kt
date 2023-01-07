/*
 * Copyright 2023 PChat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.pchat.util

import java.sql.Timestamp
import java.util.*

fun generateTimestamp(sentAt: String,sentOn: String): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, getYear(sentOn = sentOn))
    calendar.set(Calendar.MONTH, getMonth(sentOn = sentOn))
    calendar.set(Calendar.DAY_OF_MONTH, getDate(sentOn = sentOn))
    calendar.set(Calendar.HOUR_OF_DAY, getHours(sentAt = sentAt))
    calendar.set(Calendar.MINUTE, getMinutes(sentAt = sentAt))
    calendar.set(Calendar.SECOND, getSeconds(sentAt = sentAt))
    return calendar.timeInMillis
}

fun getHours(sentAt:String):Int{
    return sentAt.substring(0,2).toInt()

}

fun getMinutes(sentAt:String):Int{
    return sentAt.substring(3,5).toInt()

}

fun getSeconds(sentAt:String):Int{
    return sentAt.substring(6,8).toInt()
}

fun getDate(sentOn:String):Int{
    return sentOn.substring(0,2).toInt()
}
fun getMonth(sentOn: String):Int{
    return sentOn.substring(3,5).toInt()
}

fun getYear(sentOn: String):Int{
    return sentOn.substring(6,10).toInt()
}