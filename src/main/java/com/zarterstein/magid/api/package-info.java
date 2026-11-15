/**
 * The public contract of the Magid narrative engine.
 * <p>
 * Magid implements these interfaces; tagpacks (plugins that add MagidML tags) compile against them and never against
 * the engine. Every type a pack may use lives under this package, so a pack's class loader can take exactly
 * {@code com.zarterstein.magid.api.*} from the engine and everything else from the pack.
 * </p>
 * <p>
 * Version 0.x: the contract is being extracted from the engine, and minor versions may still break compatibility.
 * </p>
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
package com.zarterstein.magid.api;
