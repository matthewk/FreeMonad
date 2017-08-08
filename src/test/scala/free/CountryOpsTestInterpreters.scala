package free
import cats.~>
import model.{Country, CountryDetail}

object CountryOpsTestInterpreters {
  def apply(countries: List[Country], details: List[CountryDetail]): (CountriesAlg ~> ListState) =
    new (CountriesAlg ~> ListState) {
      override def apply[A](op: CountriesAlg[A]): ListState[A] = op match {
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
