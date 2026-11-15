# magid-api

The public contract of the **Magid** interactive-narrative engine: the interfaces, annotations and value types that
the engine implements and that **tagpacks** compile against.

A tagpack is a plugin that adds MagidML tags to Magid (for example `<llm-…>` tags that talk to a language model,
`<math-…>` or `<i18n-…>`), written in its own repository, with its own dependencies. It depends on `magid-api` only,
never on the engine.

## Status

**0.x, under construction.** The contract is being extracted from the engine step by step. Until 1.0, minor versions
may break compatibility; 1.0 is frozen once two different tagpacks work without engine changes.

## What it contains (when complete)

- the tag annotations (`@MagidXmlTag`) and semantic types;
- the tag contract: a pack tag, the element it reads, the context it runs in, its result;
- the story's symbols as a pack sees them (`SymbolPinax`), symbol references, read-only actor views;
- the Story Object Model (scenes, choices, descriptions, fragments) a pack may change;
- pack configuration and pack state that is saved with the session;
- the exceptions a pack may throw.

No engine code: implementations live in Magid.

## Using it

Releases are published to GitHub Packages (Maven). Coordinates and setup follow with the first release.

## License

Apache License 2.0, see [LICENSE](LICENSE). This covers `magid-api` only; the Magid engine has its own license.

Copyright 2026 Mark-Marion Gondelman
