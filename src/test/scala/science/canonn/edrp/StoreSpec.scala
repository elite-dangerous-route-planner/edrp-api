package science.canonn.edrp

import org.scalatest._

class StoreSpec extends FlatSpec with Matchers {

  "EDRP Store" should "return correct system name typeaheads" in {
    Store.systemTypeAhead("Do") should be (Seq(
      "Dobunn",
      "Do Canum Venaticorum",
      "Docleachi",
      "Dofni",
      "Dogoneja",
      "Dohkwibur",
      "Dohkwithi",
      "Dolanque",
      "Domawilata",
      "Domocast"))
  }

}
