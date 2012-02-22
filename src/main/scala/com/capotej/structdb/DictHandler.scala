package com.capotej.structdb

import com.posterous.finatra.FinatraApp
import scala.collection.mutable.Map

object DictHandler extends FinatraApp("/dict") {
 
  var dictMap = Map[String, Map[String, String]]()

  get("/:name") {
    dictMap.get(params("name")) match {
      case Some(dict) => dict.get(params("key")) match {
        case Some(v) => v
        case None => status(404); "Not found"
      }
      case None  => status(404); "Not found"
    }
  }
 
  post("/:name") {
    dictMap.get(params("name")) match {
      case Some(dict) => dict += Tuple2(params("key"), params("value"))
      case None  => dictMap += Tuple2(params("name"), Map(Tuple2(params("key"), params("value"))))
    }
    "OK"
  }

}
