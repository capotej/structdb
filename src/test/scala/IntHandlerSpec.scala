package com.capotej.structdb.tests
import com.capotej.structdb.IntHandler

import org.junit.Test
import com.posterous.finatra.{FinatraServer, Router}
import com.codahale.simplespec.Spec
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.Status._
import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, DefaultHttpResponse, HttpMessage,
    HttpMethod, HttpRequest, HttpVersion, QueryStringEncoder}

class IntegrationSpec extends Spec {

  var lastResponse:Response = null

  def get(uri: String, params: Tuple2[String, String]*) = {
    val r = req(HttpMethod.GET, uri, params:_*)
    lastResponse = Router.dispatch(r)
  }

  def post(uri: String, params: Tuple2[String, String]*) = {
    val r = req(HttpMethod.POST, uri, params:_*)
    lastResponse = Router.dispatch(r)
  }

  def post(uri: String) = {
    val r       = Request(HttpMethod.POST, uri)
    lastResponse  = Router.dispatch(r)
  }

  def get(uri: String) = {
    val r       = Request(HttpMethod.GET, uri)
    lastResponse  = Router.dispatch(r)
  }
  
  def req(method: HttpMethod, uri: String, params: Tuple2[String, String]*): Request = {
    val encoder = new QueryStringEncoder(uri)
    params.foreach { case (key, value) =>
      encoder.addParam(key, value)
    }
    Request(method, encoder.toString)
  }

}

class IntHandlerSpec extends IntegrationSpec {

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
