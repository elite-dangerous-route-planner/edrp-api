package science.canonn.edrp

import org.json4s.native.Serialization

class EdrpSrv extends EdrpStack {

  get("/") {
    contentType="text/html"
    jade("/home")
  }

  get("/a/ta") {
    if(!params.contains("v"))
      Serialization.write(List.empty)
    else {
      val v = params("v").toLowerCase().replaceAll("[^a-z0-9]","")
      if(v.length() < 3) {
        Serialization.write(List.empty)
      } else {
        Serialization.write(Store.typeaheadCache.get(v))
      }
    }
  }

  get("/a/sys") {
    val dSol = if(params.contains("dSol")) params("dSol").toInt else 1000
    val dArr = if(params.contains("dArr")) params("dArr").toInt else 20000
    val mVal = if(params.contains("mVal")) params("mVal").toInt else 2000000

    Serialization.write(Store.systemCache.get((dSol, dArr, mVal)))
  }
}

