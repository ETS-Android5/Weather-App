# Weather-App
An android developed app that displays a variety of weather data for a specified location – current weather, hourly forecast (48 hours), and daily forecast (7 days).

## App Highlights
The app is made up of 2 activities – the home weather screen, and the daily forecast screen.

The units (C/F) can be toggled between imperial and metric by tapping an options-menu icon.

The daily forecast is displayed by tapping the calendar icon in the options-menu.

The location for the displayed weather can be changed by tapping the location icon in the options-menu.

## Application/Behavior Diagrams

### App Main Screen:
![weather7.png](https://github.com/angmadera/images/blob/main/weather7.png?raw=true)

### 7-Day Weather Screen:
![weather8.png](https://github.com/angmadera/images/blob/main/weather8.png?raw=true)

## Internet Data
Weather data for this app will come from OpenWeather (https://openweathermap.org).

## Options Menu
There are three options menu: Change Units, Daily Forecast, and Change Location.

Change Units: Selecting this menu item toggles the selected measurement unit from imperial to metric or
from metric to imperial. When changed, the OneCall API endpoint is called again, specifying the newly selected unit. The change unit menu item’s icon is changed to match the selected unit (°F for imperial, °C for metric).

![weather1.png](https://github.com/angmadera/images/blob/main/weather1.png?raw=true) ![weather2.png](https://github.com/angmadera/images/blob/main/weather2.png?raw=true)

Daily Forecast: Selecting this menu item should open the daily forecast activity (passing the daily forecast data content).

![weather2.png](https://github.com/angmadera/images/blob/main/weather3.png?raw=true)

Change Location: Selecting this menu item displays an AlertDialog that allows the user to enter a new location, as shown below. Upon tapping OK, the specified location is used with a GeoCoder to get the latitude and longitude. The latitude and longitude is then used to call the OneCall API endpoint to get the new weather data.

![weather5.png](https://github.com/angmadera/images/blob/main/weather5.png?raw=true) ![weather6.png](https://github.com/angmadera/images/blob/main/weather6.png?raw=true)

## Weather icons
Weather icons found in the JSON weather data are provided as a string code (04d, 10n, etc). These codes  correspond to image file names. These icon names are used to access these provided images within the project (in the drawable resource folder).

## Time Conversions
Date/Time (“dt”) values are provided as the current time, Unix, UTC. These need to be converted to Date/Time values in local time, then formatted as needed. Note, the “timezone_offset” value (from the JSON data) is needed to do this.

## Handling no-network situations

When the device has no network connection, the app cannot access the OpenWeather API site (and the GeoCoders is not able to function). In those situations (when the app attempts to access the internet without a connection), the home screen’s data/time text view shows “No internet connection”. When there is no network connection, the options-menu selections does not function and displays a Toast message indicating that the function cannot be used when there is no network connection.

## Converting Wind Direction
The wind direction is provided by the API in degrees (0-359 degrees). The app displays this in a more conventional fashion, using compass points (N, SE, W, etc). This is derived by specifying what degrees correspond to these compass points.
