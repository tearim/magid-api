# magid-api

The public contract of the **Magid** interactive-narrative engine: the interfaces, annotations and value types that
the engine implements and that **tagpacks** compile against.

A tagpack is a plugin that adds MagidML tags to Magid (for example `<llm-…>` tags that talk to a language model,
`<math-…>` or `<i18n-…>`), written in its own repository, with its own dependencies. It depends on `magid-api` only,
never on the engine.

## Status

**Version 0.1, under construction** (published as `0.1-SNAPSHOT` until its release). The contract is being extracted
from the engine step by step. Until 1.0, every minor version (0.1, 0.2, …) may break compatibility; 1.0 is frozen once
two different tagpacks work without engine changes.

Available so far: the tag annotations and semantic types (`…api.tags`), the morpher contract (`…api.morphers`) and
the reveries, Magid's typed values (`…api.reveries`).

## What it contains (when complete)

- the tag annotations (`@MagidXmlTag`) and semantic types;
- the tag contract: a pack tag, the element it reads, the context it runs in, its result;
- the story's symbols as a pack sees them (`SymbolPinax`), symbol references, read-only actor views;
- the Story Object Model (scenes, choices, descriptions, fragments) a pack may change;
- pack configuration and pack state that is saved with the session;
- the exceptions a pack may throw.

No engine code: implementations live in Magid.

## Using it

Published to GitHub Packages (Maven):

```xml
<dependency>
    <groupId>com.zarterstein</groupId>
    <artifactId>magid-api</artifactId>
    <version>0.1-SNAPSHOT</version>
    <scope>provided</scope> <!-- the engine supplies it at run time -->
</dependency>
```

GitHub Packages asks for a token even for public packages: add the repository
`https://maven.pkg.github.com/tearim/magid-api` to your pom, and a `server` with the same id to your Maven
`settings.xml`, holding your GitHub user name and a personal access token with the `read:packages` scope.

## License

Apache License 2.0, see [LICENSE](LICENSE). This covers `magid-api` only; the Magid engine has its own license.

Copyright 2026 Mark-Marion Gondelman
