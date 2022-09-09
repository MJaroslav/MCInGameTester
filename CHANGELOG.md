# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- [README.md](README.md).
- [CHANGELOG.md](CHANGELOG.md) (this file).

### Changed

- CI gradle block moved to plugin part of project.

### Fixed

- `@WorldShadow` independence from access modifier now works.
- Version tags and GitHub releases now starts with `v`.

## [1.1.0] - 2022-09-01

### Added

- Configuration for game autostopping.
- Special Log4J configuration, that disabling all loggers exclude ModInfo.LOG in test tasks.
- For testServer task added auto EULA accepting.
- For all game test tasks added run directory resetting.
- `expected` parameter in `@Test` for expecting exceptions. 

### Edited

- Rewritten test engine.
- Some code refactoring.

## [1.0.3] - 2022-08-29

### Changed

- Game now just crash on first failed test.

## [1.0.2] - 2022-08-29

### Fixed

- Forgotten version bump in previous release.

## [1.0.1] - 2022-08-29

### Fixed

- Source jar now adds to maven publishing.
- Failed tests not ignoring now after available state on client.

## [1.0.0] - 2022-08-28

### Added

- Test annotations.
- Test running.
- Tasks for running game with test source set.

[unreleased]: https://github.com/MJaroslav/MCInGameTester/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/MJaroslav/MCInGameTester/compare/v1.0.3...v1.1.0
[1.0.3]: https://github.com/MJaroslav/MCInGameTester/compare/v1.0.2...v1.0.3
[1.0.2]: https://github.com/MJaroslav/MCInGameTester/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/MJaroslav/MCInGameTester/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/MJaroslav/MCInGameTester/releases/tag/1.0.0