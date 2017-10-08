# WalkPast
Is an interactive story game for android that uses sensors and requires the player to move around in real life to get forward in the story.

Thank you Frebers.com for the backgrounds and Lorem Avatar for the characters.

## Project Scope

### Done
* BT-communication to external sensor
* Phone’s internal step counter sensor
* Accessibility taken into consideration during development
* UI based on material design guidelines
* SQLite database
* Clean code
* Fragments
* Connection to some API

### Not done
* Have AR related functionality
* Extra "hardware" on addition to sensors (gps, camera,...)

## Requirements & Dependencies
* MetaWear Sensor Accelerometer 3.1.0 (works without too)
* Minimum Android 5.0 API 21
* Targeted Android 8.0 API 26
* Picasso 2.5.2
* LoremAvatar API

## Installation

### 1. Clone the project
```sh
git clone git@github.com:/tundet/walkpast.git
```
### 2. Configure MetaWear

The project uses MetaWear's accelerometer to move the character, which means that the MetaWear MAC address is needed.

**MetaWear:** Download MetaWear app from Store --> Connect to device --> Check MAC address

**Android Studio:** Go to MainActivity --> find method onServiceConnected() --> insert correct MetaWear MAC address

### 4. Run the project

**Android Studio:** Go to Run --> Run 'MainActivity' --> Select desired device from list

**Note**: Tested only on Samsung Galaxy S5 API 23

## Schedule
https://trello.com/b/tAaesoy9/walkpast  
I decided to use Trello, because last time my team didn't allow me to have a bison in the background. Now I can have a bison in the background.  
The cards are connected to commits done on github.  
Labels are Not Done (red), In Progress (yellow) and Done (green).  
