# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2023-08-11
### Added
- Initial commit
- Init project
- NNGM Service and Quality Report Service
- TAPP#SUFFIX configurator
- Expand no language variables in TAPP#SUFFIX configurator.
- single spa and router link variables in TAPP#SUFFIX configurator.
- TEILER_APP# instead of TAPP#
- Teiler UI Configurator
- Teiler APP IS_ACTIVATED
- icon class, icon source url and order to teiler app suffix
- All environment variables defined in TeilerCorsCons
- REST service "apps"
- Test info service
- Imports Map configurator for single spa
- Set Backend URL
- root-config and teiler-ui URLS to single-spa import map
- Single Spa Main JS to TeilerApp
- islocal to TeilerApp
- Config blocks service
- is frontend reachable and is backend reachable
- Dockerfile
- Only one server pro instance instead pro language for teiler ui and teiler apps.
- pythong script: Generate docker variables from .env
- python script: Generate docker variables from .env
- Remove language of router link
- Use amazoncorretto as java docker image
- language to router link if not default language
- in menu
- router link extension
- subroutes
- dockerignore
- Spring 2.7.5
- Log environment initialization
- Timeout in Ping
- Java 19
- Http relative path
- Check URLS

### Fixed
- Filter external links of import map
- Read environment variables files from resources or from directory
- Read environment variables files from resources or from directory.
- check if appidteilerappmap is not null by certains languages.

### Changed
- Rename to develop
