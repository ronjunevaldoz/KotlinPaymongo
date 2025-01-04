# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.0.2-dev07]

### Changes
- Update to ktor 3.0.1 and kotlin 2.1.0

## [v1.0.2-dev06]

### Fixes
- Fix link webhook response

## [v1.0.2-dev05]

### Fixes
- wrong input in createLink

## [v1.0.2-dev04]

### Added
- new qrph.expired event
- allow null in url webhook creation/update

## [v1.0.2-dev03]

### Changed
- Bump version to v1.0.2-dev03

## [v1.0.2-dev02]

### Changed
- Support link in Resource serializers

## [v1.0.2-dev01]

### Added
- Create a link
- Get link by id
- Get link by reference number
- Archive link
- Unarchive link
- Added new webhook `Event` `link.payment.paid` and `checkout_session.payment.paid`

## [v1.0.1]

### Changed
- Update kotlin version to 2.0.20
- Merge dev02

## [v1.0.0-dev02]

### Changed
- Optimize deprecated content from http response
- Refactor rename Paymongo to PayMongo

## [v1.0.0-dev01] 

### Changed
- change package name
- added compose multiplatform module

## [v1.0.0]

### Added
- Added samples demo project

### Changed
- Upgrade to multiplatform with WASMJS

[v1.0.2-dev02]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.2-dev01...HEAD
[v1.0.2-dev01]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.2-dev01...v1.0.2-dev02
[v1.0.1]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.1...v1.0.2-dev01
[v1.0.0-dev02]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0-dev02...v1.0.1
[v1.0.0-dev01]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0-dev01...v1.0.0-dev02
[v1.0.0]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0...v1.0.0-dev01