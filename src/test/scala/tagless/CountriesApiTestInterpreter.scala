package tagless

import model.{Country, CountryDetail}

class CountriesApiTestInterpreter(countries: List[Country], details: List[CountryDetail]) extends
  CountriesApi[ListState]{

  override def getCountries: ListState[List[Country]] =
    addManyToState(countries.map(c => s"""\tCountry: ${c.name}, ${c.region}"""), countries)

  override def getCountryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = details.find(_.name.equalsIgnoreCase(country.name))

    addToState(s"""\t${result.toString}""", result)
  }
}
