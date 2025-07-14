![](/readme.files/banner-light.png)

# <img src="/readme.files/icon-512.png" alt="icon" title="imagine" width="40" height="40" align="left" /> News app - Stay tuned

News-app is an android application, built using [**newsapi.org**](https://newsapi.org) API's.

You can view lots of news from different sources across the world.</br>
Check out the [**latest release**](https://github.com/alibardide5124/News-app/releases/latest) to download signed apk.

## Features
News-app features basic feed functionality, including:
- Receive JSON from API and convert to class
- Content pagination
- Built in WebView
- Save favorite articles
It also has a responsive UI for Mobile / Tablet / Desktop screens.

## Repo Structure
```
.
├── app/src/main/
│   ├── java/com/phoenix/newsapp/
│   │   ├── components/ # Shared components
│   │   ├── data/ # Handle data related tasks (Network, Local) 
│   │   ├── screen/ # Directory which stores application screens
│   │   ├── ui/theme/
│   │   ├── AppModules.kt
│   │   ├── Constants.kt
│   │   ├── MainActivity.kt
│   │   ├── NewsApp.kt
│   │   ├── ...
│   ├── res/ # Resources
│   │   ├── drawable/
│   │   ├── mipmap/
│   │   ├── ...
│   ├── AndroidManifest.xml/
├── gradle/
│   └── ...
├── LISENCE
└── README.md
```

## Tech Stack
- **Kotlin** for programming android application
- **Jetpack compose** to design declaritive android ui
- **Retrofit** for network requests
- **Room** for SQLite database
- **Coil** to load network from url

## Setup
- Clone reposity by running `git clone https://github.com/alibardide5124/News-app`
- Install Android Studio and Android SDK
- Build project with Ctrl + F9
- Install Virtual Machine or connect a Physical Device
- Run using Shift + F10

## Support
If you like this application, just support it by joining [**stargazers**](https://github.com/alibardide5124/News-app/stargazers) for this repository
<br/>
And [**follow me**](https://github.com/alibardide5124?tab=followers) for my next creations

## License
News app by [Ali Bardide](https://github.com/alibardide5124) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
