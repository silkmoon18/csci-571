const express = require('express')
const app = express()
const cors = require('cors')
const axios = require('axios')

app.use(cors({
  origin: '*'
}))
app.use(express.json())

const port = process.env.PORT || 3000
const YELP_BUSINESS_URL = 'https://api.yelp.com/v3/businesses'
const YELP_AUTOCOMPLETE_URL = 'https://api.yelp.com/v3/autocomplete'
const YELP_API_KEY = '' // Replace with your key

const GEOCODING_URL = 'https://maps.googleapis.com/maps/api/geocode/json'
const GEOCODE_API_KEY = '' // Replace with your key

const MILES_TO_METERS_FACTOR = 1609.344

let input = {
  keyword: '',
  distance: '',
  category: '',
  location: '',
  lat: '',
  lng: '',
  autoLocated: false
}

app.use(express.static(process.cwd() + "/dist/assignment-8/"));

app.get('/', (req, res) => {
  res.sendFile(process.cwd() + "/dist/assignment-8/index.html")
})

app.get('/search', (req, res) => {
  res.redirect('/');
})

app.get('/bookings', (req, res) => {
  res.redirect('/');
})

//-----------------------------------

function miles_to_meters(miles) {
  return Math.min(Math.floor(parseFloat(miles) * MILES_TO_METERS_FACTOR), 40000)
}

function meters_to_miles(meters) {
  return (meters / MILES_TO_METERS_FACTOR).toFixed(2)
}

function updateLocation() {
  if (input.autoLocated)
    return Promise.resolve()

  let url = GEOCODING_URL + `?address=${input.location.replace(' ', '+')}&key=${GEOCODE_API_KEY}`

  return axios.get(url)
    .then(function (response) {
      let result = response.data['results']
      if (result.length === 0) {
        console.log(`Location ${input.location} not found.`)
        return
      }
      let location = result[0]['geometry']['location']
      input.lat = location['lat'], input.lng = location['lng']
    })
    .catch(function (error) {
      console.log(error)
    })

}

function getSearchResult() {
  let radius = input.distance ? miles_to_meters(input.distance) : ''

  let url = YELP_BUSINESS_URL + `/search?term=${input.keyword}&latitude=${input.lat}&longitude=${input.lng}&categories=${input.category}&radius=${radius}&limit=10`
  let headers = { 'Authorization': 'Bearer ' + YELP_API_KEY }

  return axios.get(url, { headers: headers })
    .then(function (response) {
      return response.data['businesses']
    })
    .catch(function (error) {
      console.log(`Cannot get search results. ${error}`)
    })
}

app.get('/handle_input', (req, res) => {
  console.log(JSON.stringify(req.query))
  let params = JSON.parse(req.query.params)
  input.keyword = params.keyword
  input.distance = params.distance
  input.category = params.category
  input.location = params.location
  input.lat = params.detectedLocation[0], input.lng = params.detectedLocation[1]
  input.autoLocated = params.autoLocated

  updateLocation().then(getSearchResult).then(function (result) {
    let data = []
    if (!result) {
      res.send(data)
      return
    }

    for (let business of result) {
      let item = {}

      item['id'] = business['id']
      item['image'] = business['image_url']
      item['name'] = business['name']
      item['rating'] = business['rating']
      item['distance'] = meters_to_miles(business['distance'])

      data.push(item)
    }
    res.send(data)
  })
})

//-----------------------------------

function getAutocompleteResult(keyword) {
  let url = YELP_AUTOCOMPLETE_URL + `?text=${keyword}`
  let headers = { 'Authorization': 'Bearer ' + YELP_API_KEY }

  return axios.get(url, { headers: headers })
    .then(function (response) {
      return response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

app.get('/handle_autocomplete', (req, res) => {
  let keyword = req.query.keyword

  getAutocompleteResult(keyword).then(function (result) {
    let data = []

    for (let item of result['categories']) {
      data.push(item['title'])
    }
    for (let item of result['terms']) {
      data.push(item['text'])
    }

    res.send(data)
  })
})

//-----------------------------------

function getBusinessDetail(id) {
  let url = YELP_BUSINESS_URL + `/${id}`
  let headers = { 'Authorization': 'Bearer ' + YELP_API_KEY }

  return axios.get(url, { headers: headers })
    .then(function (response) {
      return response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

app.get('/get_business', (req, res) => {
  let id = req.query.id

  getBusinessDetail(id).then(function (result) {
    let data = {}

    data.id = result['id'] ? result['id'] : ''
    data.name = result['name'] ? result['name'] : ''
    data.status = result['is_closed'] ? 'Open Now' : "Closed"
    data.category = result['categories'].length > 0 ? result['categories'].map(cat => cat['title']).join(' | ') : ''
    data.phone = result['display_phone'] ? result['display_phone'] : ''
    data.price = result['price'] ? result['price'] : ''
    data.photos = result['photos'] ? result['photos'] : ''
    data.url = result['url'] ? result['url'] : ''
    data.coord = result['coordinates'] ? result['coordinates'] : ''
    data.address = result['location']['display_address'] ? result['location']['display_address'].join(', ') : ''

    res.send(data)
  })
})

//-----------------------------------

function getReviews(id) {
  let url = `https://api.yelp.com/v3/businesses/${id}/reviews`
  let headers = { 'Authorization': 'Bearer ' + YELP_API_KEY }

  return axios.get(url, { headers: headers })
    .then(function (response) {
      return response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

app.get('/get_reviews', (req, res) => {
  let id = req.query.id

  getReviews(id).then(function (result) {
    let data = []
    result = result.reviews

    for (let review of result) {
      let item = {}
      item.rating = review['rating'] ? review['rating'] : ''
      item.name = review['user']['name'] ? review['user']['name'] : ''
      item.text = review['text'] ? review['text'] : ''
      item.time_created = review['time_created'] ? review['time_created'] : ''

      data.push(item)
    }

    res.send(data)
  })
})


app.get('/test', (req, res) => {
  res.end(JSON.stringify({ result: 'test' }));
})

app.listen(port, () => {
  console.log(`App listening on port ${port}`);
})
