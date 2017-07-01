package free
import cats.~>
import model.{Country, CountryDetail}

object CountryOpsTestInterpreters {
  def apply(countries: List[Country], details: List[CountryDetail]): (CountriesApiAlg ~> ListState) =
    new (CountriesApiAlg ~> ListState) {
      override def apply[A](op: CountriesApiAlg[A]): ListState[A] = op match {
        case GetCountyDetail(country) => {
          val result = details.find(_.name.equalsIgnoreCase(country.name))
          addToState(s"""\t${result.toString}""", result.get.asInstanceOf[A])
        }

        case GetCountries() =>
          addManyToState(
            countries.map(c => s"""\tCountry: ${c.name}, ${c.region}"""),
            countries.asInstanceOf[A])
      }
    }
}
