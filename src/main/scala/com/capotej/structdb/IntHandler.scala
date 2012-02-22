package com.capotej.structdb

import com.posterous.finatra.FinatraApp
import scala.collection.mutable.Map
import java.util.concurrent.atomic.AtomicInteger

object IntHandler extends FinatraApp("/int") {
 
  var intMap = Map[String, AtomicInteger]()

  get("/:name") { 
    intMap.get(params("name")) match {
      case Some(v) => v.get
      case None  => status(404); "Not found"
    }
  }
 
  post("/:name") {
    var value: Int = 1
    
    if(!params("value").isEmpty)
      value = augmentString(params("value")).toInt

    intMap.get(params("name")) match {
      case Some(v) => v.addAndGet(value)
      case None  => intMap += Tuple2(params("name"), new AtomicInteger(value))
    }
  
    "OK"
  }

}
