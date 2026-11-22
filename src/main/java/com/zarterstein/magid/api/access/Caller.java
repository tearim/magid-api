package com.zarterstein.magid.api.access;

import java.util.Locale;
import java.util.Optional;

/**
 * Who runs a tag, as far as a tagpack may know: a read-only view of the session's account, built for one pack.
 * A tag reaches it through {@link com.zarterstein.magid.api.tags.TagContext#caller()}.
 * <p>
 * It deliberately leaves out the account's real id, display name, tenant and the integrator's attributes: packs talk
 * to outside services (a model's endpoint, a web request), and none of that should reach them by default.
 * </p>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface Caller {

    /**
     * The caller of contexts without an account (a test's fake, anonymous play without a server): {@link AccessLevel#ANON},
     * the id {@value #NONE_ID}, no locale, no groups.
     */
    Caller NONE = new Caller() {
        @Override
        public AccessLevel level() {
            return AccessLevel.ANON;
        }

        @Override
        public String pseudonymousId() {
            return NONE_ID;
        }

        @Override
        public Optional<Locale> locale() {
            return Optional.empty();
        }

        @Override
        public boolean inGroup(String group) {
            return false;
        }
    };

    /** The {@link #pseudonymousId()} of {@link #NONE}. */
    String NONE_ID = "none";

    /**
     * @return the account's access level; never {@code null}
     */
    AccessLevel level();

    /**
     * An id for this account that is stable across sessions but differs from pack to pack (and from server to
     * server): a pack can recognise a returning user, for quotas or an audit record, but two packs can't link their
     * ids, and the id reveals nothing about the account.
     *
     * @return the pack's id for this account; never {@code null} or blank
     */
    String pseudonymousId();

    /**
     * @return the account's preferred locale, if it has one
     */
    Optional<Locale> locale();

    /**
     * Asks about one group the pack knows by name ({@code verified-adults}, …). There is deliberately no list of all
     * groups: their names may say more about a person than a pack needs to know.
     *
     * @param group the group's id
     * @return whether the account belongs to the group
     */
    boolean inGroup(String group);
}
