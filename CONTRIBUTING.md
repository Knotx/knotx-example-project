![Knot.x logo](http://knotx.io/img/logo-knotx.png)

# How to contribute to Knot.x
Thank you for taking the time to contribute!
We appreciate all commits and improvements, feel free to join Knot.x community and contribute.

## How to start
Please refer to [README.md](README.md) file in this repository.

## Knot.x Contributor License Agreement
Project License: [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- You will only Submit Contributions where You have authored 100% of the content.
- You will only Submit Contributions to which You have the necessary rights. This means that if You are employed You have received the necessary permissions from Your employer to make the Contributions.
- Whatever content You Contribute will be provided under the Project License(s).

## Commit Messages
When writing a commit message, please follow the guidelines in [How to Write a Git Commit Message](http://chris.beams.io/posts/git-commit/).

## Pull Requests
Please add the following lines to your pull request description:

```

---

I hereby agree to the terms of the Knot.x Contributor License Agreement.
```

## Documentation
All documentation for this repository will be kept in the [`README.md`](README.md) file. If you would like to create a tutorial descibing how to use this module you are most welcome to contribute a [Knot.x tutorial](https://github.com/Knotx/knotx-website#tutorials) to knotx.io(http://knotx.io/tutorials/.

## Changelog
When changing or fixing some important part of Knot.x, please remember to update [Changelog](CHANGELOG.md).
Your entry should be enlisted in `Unreleased` section. It will be moved to appropriate release notes when released.
Please use convention `- [PR-ABC](https://github.com/Knotx/${knotx-repository}/pull/ABC) - short description of the PR.`.

## Upgrade Notes
When changing or fixing anything related with changes in Knot.x configuration (e.g. its structure, new property etc.) or
snippet structure (e.g. change convention of naming services) remember to include detailed description of changes that needs to be done in order to upgrade from the latest version o Knot.x in the `Upgrade notes` section of the issue.
This information will be later included in the [Release Notes blog post](http://knotx.io/blog/).

## Tests naming convention
Tests written in Knot.x should be named with `methodName_whenStateUnderTest_expectBehavior` convention proposed as the first example in [7 Popular Unit Test Naming Conventions](https://dzone.com/articles/7-popular-unit-test-naming).

### Examples:
**Unit tests**
`canServeRequest_whenNoFormIdAndPostAttribute_expectRequestNotServed`

**Integration tests**
In integration tests method name is omitted, and test class name should suggest what part of system we test:
`whenRepositoryDidNotReturnTemplateBody_expectNoSnippetsProcessing`

## Coding Conventions
Below is short list of things that will help us keep Knot.x quality and accept pull requests:
- Follow Google Style Guide code formatting from Knot.x Github, particularly set your IDE `tab size`/`ident` to 2 spaces and `continuation ident` to 4 spaces.
  - [Google Style Guide for Eclipse](https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml)
  - [Google Style Guide for IntelliJ](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)
- write tests (integration and Unit Tests) following defined convention,
- write javadoc, especially for interfaces and abstract methods,
- update [`README.md`](README.md) for any documentation updates,
- when logging use proper levels: `INFO` and `WARNING` should log only very important messages.
