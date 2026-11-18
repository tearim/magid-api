# magid-api

The public contract of the **Magid** interactive-narrative engine: the interfaces, annotations and value types that
the engine implements and that **tagpacks** compile against.

A tagpack is a plugin that adds MagidML tags to Magid (for example `<math-…>`, `<llm-…>` tags that talk to a
language model, or `<i18n-…>`), written in its own repository, with its own dependencies. It depends on `magid-api`
only, never on the engine. The engine checks a pack when the server starts, gives it a class loader of its own, and
a story uses it with one line: `<extend with="the-pack-id"/>`.

## Status

**Version 0.1** (published as `0.1-SNAPSHOT` until its release), in use:

- The engine runs every one of its own tags through this contract; there are no special cases for built-in tags.
- [magid-math](https://github.com/tearim/magid-module-math) is the first tagpack built on it: 61 tags of
  double-precision math, released as v0.1.0 with a demo story.

Until 1.0, a minor version (0.1, 0.2, …) may break compatibility. 1.0 is frozen once two different tagpacks work
without engine changes.

## What it contains

| Package | What a pack gets |
|---|---|
| `…api.tags` | `@MagidXmlTag` (a tag's names and `SemanticType`s); `MagidTag`, the class a pack's tag extends; `TagElement` (the element it reads); `TagContext` (what it runs in: the story model, symbols, expressions, its pack's settings); `Outcome` (how it ends: proceed, break, jump, …); the exceptions a tag throws, with source positions |
| `…api.symbols` | `SymbolPinax`, the story's variables, constants and actors' memories as a pack reads and writes them; `SymbolRef` |
| `…api.som` | the Story Object Model: scenes, choices, narration |
| `…api.actors` | `ActorInterface` |
| `…api.reveries`, `…api.values` | Magid's typed values: `Reverie` (booleans, integers, floats, text) and `BasicTypedValue` |
| `…api.packs` | `PackManifest` (a pack's manifest attributes, and the rules for its id and prefix); `PluginPropsInterface`, a pack's own server settings |
| `…api.expressions` | the story's expression evaluator |
| `…api.morphers` | the contract for tag-name morphers (shorthand syntax) |

A pack keeps what a session must remember in the story's symbols, named with its prefix, so saves need nothing
pack-specific. Settings that don't belong in a story, such as an endpoint, come from the server
(`TagContext.pluginProps()`); secrets never go into symbols.

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

A pack's manifest names it (see `PackManifest`):

```
Magid-Plugin-Type: tag-pack
Magid-Plugin-Id: magid-math
Magid-Plugin-Version: 0.1.0
Magid-Requires-Api: [0.1,0.2)
Magid-Tag-Prefix: math
```

[magid-math](https://github.com/tearim/magid-module-math) is a complete example: the tags, the manifest, unit
tests against a fake context, and a release a server installs with
`magid tagpack install <the release's jar URL>`.

## Authors

Designed by **Mark-Marion Gondelman**, who also wrote the core interfaces (the tag contract, the symbols, the Story
Object Model). The move out of the engine, part of the code, the tests and the documentation: Claude Code
(Anthropic), under his direction. Each file's `@author` tags say who wrote it.

## License

Apache License 2.0, see [LICENSE](LICENSE). This covers `magid-api` only; the Magid engine has its own license.

Copyright 2026 Mark-Marion Gondelman
