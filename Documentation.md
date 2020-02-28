# Coldpod

## Project structure:

The project contains the following packages:

1. _di:_ dependency injection framework classes (Dagger)
1. _network:_ network api, responses and data classes
1. _persistence:_ local database (room) and data classes (entities)
1. _repository:_ MVVM repository layer classes
1. _service:_ Media app server class
1. _ui:_ ui layer, fragments and activities
1. _utils:_ utility classes.

---

### _di_

Dagger is used to implement dependency injection. The app consists of App component and main subcomponent.

App Component is used to inject all app level dependencies such as Retrofit, Glide, Database, etc...

Main subComponent is used to inject the ui with subcomponent level dependencies

---

### _network_

Coldpod interacts with Itunes Api to retrieve the top 50 podcasts in the us.

Interaction consists of four requests:

```java
1. getTopPodcasts(..) // to retrieve the top 50 podcasts in the us or any other country

2. getLookResponse(..) // using the id of a specific podcast, we can retrieve the feedUrl, where all podcast episodes are located (this request requires different url, this can be accomplished by annotating the method with @url to override the BASE_URL passed initially to retrofit setup)

3. getRssFeed(..) //to retrieve podcast episodes from the feedUrl retrieved in request (#2) (XML type response)

4. getSearchResponse(..) //to retrieve podcasts based on user query.
```

_DETAILS ON HOW TO PARSE XML TO POJO, PLEASE REFER TO THE FOLLOWING LINK_

[Simple XML Serialization Tutorials](http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php)

#### Data classes:

`getTopPodcasts(..)` :

- ITunesResponse
  - Feed
    - title
    - country
    - List Podcasts
      - artistName
      - id
      - releaseDate
      - name
      - kind
      - copyright
      - artistId
      - contentAdvisoryRating
      - artistUrl
      - artworkUrl
      - genres

`getLookResponse(..)` :

- LookupResponse
  - LookupResult
    - feedUrl

`getRssFeed(..)` :

- RssFeed
  - Channel
    - author
    - language
    - description
    - title
    - ArtworkImage
      - url //element
      - href //attribute
    - category
      - text
    - Items List //podcast episodes
      - mTitle
      - description
      - summary
      - date
      - duration
      - ItemImage
        - href
      - Enclosures
        - url // url of the media
        - type //audio,mp3...
        - length

---

### _persistence_

Database contains two entites:

1. Subscribed Podcast (PodcastEntry)
1. Favorites Podcast Episodes (FavortieEntry)

Whenever the user subscribes to a podcast it will be added to the PodCastEntry entity.

Whenever the user add podcast episode as a favorite, it will be added to the FavoriteEntry table

---

### _repository_

Coldpod contains 3 repositories:

1. MainRepository
1. PodCastDetailRepository
1. PodCastsRepository

**PodCastsRepository** is responsible for fetching the list of PodCasts from the network using the following two request `getTopPodcasts(..)` ,
`getSearchResponse(..)`

**PodCastDetailRepository** is responsible for fetching podcast details and episodes from the network using the following `getLookResponse(..)` , `getRssFeed(..)`.

This repository fetch the database for podCastEntry using the id to toggle the Subscribe button text if the podcast exist in the db

**MainRepository** : this repository does not deal with the network, it is only responsible to fetch the database for subscribed podcasts and favorite episodes
