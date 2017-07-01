package tagless

import model.{Country, CountryDetail}

class CountriesApiTestInterpreter(countries: List[Country], details: List[CountryDetail]) extends
  CountriesApiAlg[ListState]{

  override def getCountries: ListState[List[Country]] =
    addManyToState(countries.map(c => s"""\tCountry: ${c.name}, ${c.region}"""), countries)

  override def getCountyDetail(country: Country): ListState[CountryDetail] = {
    val result = details.find(_.name.equalsIgnoreCase(country.name)).get

    addToState(s"""\t${result.toString}""", result)
  }
}
