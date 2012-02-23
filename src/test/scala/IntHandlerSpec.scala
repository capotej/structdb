package com.capotej.structdb.tests
import com.capotej.structdb.IntHandler

import org.junit.Test
import com.posterous.finatra.{FinatraServer, Router, FinatraSpec}
import com.codahale.simplespec.Spec
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.Status._
import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, DefaultHttpResponse, HttpMessage,
    HttpMethod, HttpRequest, HttpVersion, QueryStringEncoder}

class IntHandlerSpec extends FinatraSpec {

  FinatraServer.register(IntHandler)

  class `GET for a missing key'` {

    get("/int/foo")

    @Test def `returns 404` = {
      lastResponse.statusCode.must(be(404))
    }

  }

  class `POST for a missing key, creates it with 1'` {

    post("/int/foo")
    get("/int/foo")

    @Test def `returns 200` = {
      lastResponse.statusCode.must(be(200))
      lastResponse.content.toString("UTF8").must(be("1"))
    }

  }
  class `POST for an existing key, with a value of 10'` {

    post("/int/foo2", List(Tuple2("value", "10")):_*)
    get("/int/foo2")

    @Test def `returns 200` = {
      lastResponse.statusCode.must(be(200))
      lastResponse.content.toString("UTF8").must(be("10"))
    }

  }
}
