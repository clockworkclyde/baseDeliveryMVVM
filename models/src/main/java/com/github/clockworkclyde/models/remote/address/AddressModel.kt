package com.github.clockworkclyde.models.remote.address

import com.github.clockworkclyde.models.mappers.IConvertableTo
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.mapbox.geojson.Point
import com.mapbox.search.result.SearchAddress

data class AddressModel(
    val id: String,
    val name: String,
    val address: SearchAddress?,
    val coordinates: Point?
) : IConvertableTo<AddressItem> {

    override fun convertTo(): AddressItem = AddressItem(
        id = id,
        name = name,
        coordinates = coordinates,
        place = address?.place ?: "",
        street = address?.street ?: "",
        house = address?.houseNumber ?: ""
    )
}

//E/TAG: [SearchResult(id='address.2816109993625116',
// name='Улица Семёна Билецкого', matchingName='null',
// address='SearchAddress(houseNumber=6, street=Улица Семёна Билецкого,
// neighborhood=null, locality=null, postcode=628456, place=Сургут, district=null,
// region=Ханты-Мансийский автономный округ — Югра, country=Россия)', descriptionText='null',
// routablePoints='null', categories='[]', makiIcon='null', coordinate='Point{type=Point, bbox=null,
// coordinates=[73.35442352294922, 61.27824783325195]}', accuracy='com.mapbox.search.result.ResultAccuracy$Rooftop@e2ef8a',
// types='[ADDRESS]', etaMinutes='null', metadata='SearchResultMetadata(extraData={iso_3166_1=ru, iso_3166_2=RU-KHM},
// reviewCount=null, phone=null, website=null, averageRating=null, description=null, primaryPhotos=null,
// otherPhotos=null, openHours=null, parking=null, cpsJson=null, countryIso1=ru, countryIso2=RU-KHM)',
// externalIDs='{}`, distanceMeters='32.52958807377149', serverIndex='0',
// requestOptions='RequestOptions(query='73.354468,61.278539',
// options=SearchOptions(proximity=Point{type=Point, bbox=null, coordinates=[73.35446755646618, 61.278538981595204]},
// boundingBox=null, countries=null, fuzzyMatch=null, languages=[Language(code='ru')], limit=1, types=null,
// requestDebounce=null, origin=Point{type=Point, bbox=null, coordinates=[73.35446755646618, 61.278538981595204]},
// navigationOptions=null, routeOptions=null, unsafeParameters=null, ignoreIndexableRecords=false,
// indexableRecordsDistanceThresholdMeters=null), proximityRewritten=false, originRewritten=false,
// endpoint='reverse', sessionID='', requestContext=SearchRequestContext(apiType=GEOCODING, keyboardLocale=ru,
// screenOrientation=PORTRAIT, responseUuid=))')]