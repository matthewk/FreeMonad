package tagless

import model.{Country, CountryDetail}

class CountriesApiTestInterpreter(countryData: List[Country], detailData: List[CountryDetail]) extends
  CountriesApi[ListState]{

  override def countries: ListState[List[Country]] =
    addManyToState(countryData.map(c => s"""\tCountry: ${c.name}, ${c.region}"""), countryData)

  override def countryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = detailData.find(_.name.equalsIgnoreCase(country.name))

    addToState(s"""\t${result.toString}""", result)
  }
}
