package com.capotej.structdb

import com.posterous.finatra.FinatraServer

object App {

  def main(args: Array[String]) = {
    FinatraServer.register(IntHandler)
    FinatraServer.register(DictHandler)
    FinatraServer.start()
  }


}




