//package com.gitlab.artismarti.tinbo.timer
//
//import com.google.gson.Gson
//import com.google.gson.TypeAdapter
//import com.google.gson.stream.JsonReader
//import com.google.gson.stream.JsonWriter
//
///**
// * @author artur
// */
//
///*
//        out.beginObject()
//                .name("name").value(value.name)
//                .name("categories")
//
//        out.beginArray()
//        for (category in value.categories) {
//            out.beginObject()
//                    .name("name")
//                    .value(embedded.toJson(category.entries))
//            out.endObject()
//        }
//
//        out.endArray()
//        out.endObject()
//
//        object : TypeToken<List<TimerCategory>>() {}.type
//        object : TypeToken<List<TimerEntry>>() {}.type
// */
//
//class TimerDataAdapter : TypeAdapter<TimerData>() {
//
//    private val embedded = Gson()
//
//    override fun write(out: JsonWriter, value: TimerData) {
//        out.beginObject()
//                .name("name").value(value.name)
//                .name("categories").value(embedded.toJson(value.categories))
//        out.endObject()
//    }
//
//    override fun read(`in`: JsonReader): TimerData {
//        val timerData = TimerData()
//        `in`.beginObject()
//        while (`in`.hasNext()) {
//            when (`in`.nextName()) {
//                "name" -> timerData.name = `in`.nextString()
//                "categories" -> timerData.categories =
//                        embedded.fromJson(`in`.nextString(), Array<TimerCategory>::class.java).toList()
//            }
//        }
//        `in`.endObject()
//        return timerData
//    }
//
//}
//
//class TimerCategoryAdapter : TypeAdapter<TimerCategory>() {
//
//    private val embedded = Gson()
//
//    override fun write(out: JsonWriter, value: TimerCategory) {
//        out.beginObject()
//                .name("name").value(value.name)
//                .name("entries").value(embedded.toJson(value.entries))
//        out.endObject()
//    }
//
//    override fun read(`in`: JsonReader): TimerCategory {
//        val timerData = TimerCategory()
//        `in`.beginObject()
//        while (`in`.hasNext()) {
//            when (`in`.nextName()) {
//                "name" -> timerData.name = `in`.nextString()
//                "entries" -> timerData.entries =
//                        embedded.fromJson(`in`.nextString(), Array<TimerEntry>::class.java).toList()
//            }
//        }
//        `in`.endObject()
//        return timerData
//    }
//
//}
//
//class TimerEntryAdapter : TypeAdapter<TimerEntry>() {
//
//    override fun write(out: JsonWriter, value: TimerEntry) {
//        out.beginObject()
//                .name("message").value(value.message)
//                .name("hours").value(value.hours)
//                .name("minutes").value(value.minutes)
//                .name("seconds").value(value.seconds)
//        out.endObject()
//    }
//
//    override fun read(`in`: JsonReader): TimerEntry {
//        val timerData = TimerEntry()
//        `in`.beginObject()
//        while (`in`.hasNext()) {
//            when (`in`.nextName()) {
//                "message" -> timerData.message = `in`.nextString()
//                "hours" -> timerData.hours = `in`.nextString().toInt()
//                "minutes" -> timerData.minutes = `in`.nextString().toInt()
//                "seconds" -> timerData.seconds = `in`.nextString().toInt()
//            }
//        }
//        `in`.endObject()
//        return timerData
//    }
//
//}
