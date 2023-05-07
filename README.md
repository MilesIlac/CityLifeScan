# CityLifeScan
Teleport Cities' Quality of Life (QoL) data checker app, with Teleport API integration

## General Info
* Currently made in Java (Kotlin version in the works)
* Follows Model-View-Presenter (MVP) architecture pattern in Java Version
* Tools and libraries used: Hilt, Retrofit, Glide, Gson, AnyChart

## Features
* Big thanks to [Teleport](https://developers.teleport.org/api/) for providing the API
* Pre-loads a list of Teleport Cities under the hood. If loading fails, a button will appear for manual reloading
* Upon choosing a Teleport City and pressing the Scan button, information about that Teleport City will load, and is scrollable.
* Information includes:
  * Image of the city (a bigger version of the image can be viewed on click)
  * Basic summary including Teleport Score, city full name and continent, and mayor last recorded by Teleport
  * Clickable list of QoL metrics (e.g. Housing, Cost of Living, Safety), which shows detailed parameters on click
  * Recorded job salaries, visualized using Anychart
