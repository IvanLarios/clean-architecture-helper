# Clean Architecture Plugin

![Version](https://img.shields.io/jetbrains/plugin/v/22234)
![Rating](https://img.shields.io/jetbrains/plugin/r/rating/22234)
[![JetBrains IntelliJ Platform SDK Docs](https://jb.gg/badges/docs.svg)](https://plugins.jetbrains.com/docs/intellij)

## Introduction

Clean Architecture Plugins is an open-source plugin for JetBrains IDEs (currently only supported in IDEA and Android Studio) that helps the developer
productivity when working with hexagonal or clean architectures. This plugin provides a tool for generation of a new module's folder structure with 
just two clicks and a name. It also provides code inspections to warn the user whenever a domain or application layer file has an import that doesn't
follow the principles of a clean architecture, allowing the user to customize the restriction level.

### Contributing

If you feel like adding new features or fixing a bug, feel free to send a pull request. They will be checked periodically.

The relevant project structure is the following:
```
clean-architecture-plugin
├── src/main
│ ├── java
│ │   └── .../cleanarchitectureplugin
│ │       ├── dependency_inspector            Code related to the code inspector
│ │       ├── module_generator                Code related to the module folder structure generator and dialog
│ │       └── settings                        Code related to the settings dialog, state and service.
│ └── resources
│ │       ├── inspectionDescriptions          Html shown when checking the inspection on IntelliJ settings.
│ │       ├── messages                        Bundle with all the displayed messages
│ │       └── META-INF                        Plugin marketplace configuration and icon
├── certs                                     Placeholder files for plugin publish
├── assets                                    Images displayed in the Marketplace page
└── build.gradle.kts                          Kotlin file with the gradle dependencies and configuration
```
